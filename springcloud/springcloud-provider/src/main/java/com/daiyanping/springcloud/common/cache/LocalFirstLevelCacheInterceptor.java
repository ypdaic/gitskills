package com.daiyanping.springcloud.common.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.rabbitmq.client.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executor;

/**
 * 本地一级缓存
 * @author daiyanping
 */
@Slf4j
@Data
@Scope("prototype")
public class LocalFirstLevelCacheInterceptor extends AbstractCacheInterceptor implements InitializingBean, ChannelAwareMessageListener, BeanFactoryAware {

    protected static final String beanName = "LocalFirstLevelCacheInterceptor";

    private static final String FIRST_LEVEL_CACHE = "FIRST_LEVEL_CACHE";

    /**
     * 一级缓存
     */
    private Cache<String, Object> cache;

    /**
     * 缓存名称
     */
    private String cacheName;

    /**
     * bean 工厂
     */
    private BeanFactory beanFactory;

    /**
     * 对应的交换器
     */
    private String exchange;

    /**
     * 对应队列
     */
    private String queueName;

    /**
     * 路由key
     */
    private String routingKey;

    /**
     * 清空缓存对应的key
     */
    private String clearKey;

    private static int increment;

    private static int beanNameIncrement;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier("applicationTaskExecutor")
    ThreadPoolTaskExecutor executor;

    public LocalFirstLevelCacheInterceptor() {
        this.cache = Caffeine.newBuilder()
                // 自定义过期策略
                .expireAfter(new MyExpiry(this))
                .maximumSize(1000)
                .build();
    }

    @Override
    protected boolean matchMethod(Method method) {
        String name = method.getName();
        return "get".equals(name) || "evict".equals(name) || "clear".equals(name);
    }

    @Override
    protected Object invokeMethod(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        String name = method.getName();
        if (!"clear".equals(name)) {
            Object[] objects = invocation.getArguments();
            String key = (String) objects[0];
            switch (name) {
                case "get":

                    return cache.get(key, key2 -> {
                        try {
                            return invocation.proceed();
                        } catch (Throwable throwable) {
                            log.error("缓存获取异常", throwable);
                            throw new RuntimeException("缓存获取异常");
                        }
                    });
                case "evict":
                    rabbitTemplate.convertAndSend(exchange, "", key);
                    return null;
            }
        } else {
            rabbitTemplate.convertAndSend(exchange, "", clearKey);
        }

        return null;
    }

    /**
     * DefaultSingletonBeanRegistry的getSingleton方法 会持有singletonObjects对象的锁，
     * main 线程 在 getBean
     *
     * 和  AbstractMessageListenerContainer 中的 SimpleAsyncTaskExecutor 线程池
     */
    @Override
    public void afterPropertiesSet() {
        String random = UUID.randomUUID().toString();
        int i = ++increment;
        String queueName = FIRST_LEVEL_CACHE + "." + this.cacheName + "." + random;
        String exchange = FIRST_LEVEL_CACHE + ".EXCHANGE" + "." + cacheName;
        String bindingName = exchange + "." + cacheName + i;

        this.exchange = exchange;
        this.queueName = queueName;
        this.clearKey = queueName + "." + "clear" + "." + System.currentTimeMillis();

        org.springframework.amqp.core.Queue queue = new org.springframework.amqp.core.Queue(queueName, false, false, true);
        FanoutExchange fanoutExchange = new FanoutExchange(exchange, false, true);

        Binding binding = BindingBuilder.bind(queue).to(fanoutExchange);

        ((ConfigurableBeanFactory) this.beanFactory)
                .registerSingleton(queueName, queue);

        ((ConfigurableBeanFactory) this.beanFactory)
                .registerSingleton(exchange, fanoutExchange);


        ((ConfigurableBeanFactory) this.beanFactory)
                .registerSingleton(bindingName, binding);

        RabbitListenerContainerFactory rabbitListenerContainerFactory = (RabbitListenerContainerFactory) beanFactory.getBean("rabbitListenerContainerFactory");
        AmqpAdmin amqpAdmin = (AmqpAdmin) beanFactory.getBean("amqpAdmin");

        AbstractMessageListenerContainer listenerContainer = (AbstractMessageListenerContainer) rabbitListenerContainerFactory.createListenerContainer();
        listenerContainer.setupMessageListener(this);

        // 队列名称要和容器中的队列名称一样，否则会有问题
        listenerContainer.setQueueNames(queueName);
        listenerContainer.setAmqpAdmin(amqpAdmin);

        executor.execute(() -> {
            listenerContainer.start();
        });

    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        byte[] body = message.getBody();
        String s = new String(body);
        if (s.equals(clearKey)) {
            cache.invalidateAll();
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    private static class MyExpiry implements Expiry {

        private LocalFirstLevelCacheInterceptor localFirstLevelCacheInterceptor;

        public MyExpiry(LocalFirstLevelCacheInterceptor localFirstLevelCacheInterceptor) {
            this.localFirstLevelCacheInterceptor = localFirstLevelCacheInterceptor;
        }

        @Override
        public long expireAfterCreate(@NonNull Object key, @NonNull Object value, long currentTime) {
            long firstCacheTime = localFirstLevelCacheInterceptor.getFirstCacheTime();
            localFirstLevelCacheInterceptor.cleanThreadLocal();
            return currentTime + firstCacheTime;

        }

        @Override
        public long expireAfterUpdate(@NonNull Object key, @NonNull Object value, long currentTime, @NonNegative long currentDuration) {
            return currentDuration;
        }

        // 直接返回currentDuration表示不启用读过期设置
        @Override
        public long expireAfterRead(@NonNull Object key, @NonNull Object value, long currentTime, @NonNegative long currentDuration) {
            return currentDuration;
        }
    }

    /**
     * 注入一级缓存的BeanDefinition
     * @param beanFactory
     * @param cacheName
     */
    public static String registerBeanDefinition(BeanFactory beanFactory, String cacheName) {
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
//        CachingMetadataReaderFactory cachingMetadataReaderFactory = new CachingMetadataReaderFactory();
//        MetadataReader metadataReader = null;
//        String name1 = LocalFirstLevelCacheInterceptor.class.getName();
//        String classpathAllUrlPrefix = ResourceLoader.CLASSPATH_URL_PREFIX;
//        String replace = name1.replace(".", "/");
//        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
//        Resource resource = pathMatchingResourcePatternResolver.getResource(classpathAllUrlPrefix + replace + ".class");
//
//        try {
//            metadataReader = cachingMetadataReaderFactory.getMetadataReader(resource);
//        } catch (IOException e) {
//            log.error("一级缓存初始化失败", e);
//            throw new RuntimeException("一级缓存初始化失败");
//        }
//        ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
//        sbd.setResource(resource);
//        sbd.setSource(resource);
//        //一定要设置为prototype 否则main 线程和 AbstractMessageListenerContainer 中的 SimpleAsyncTaskExecutor 线程池中的线程会形成等待
//        sbd.setScope("prototype");

        AnnotatedGenericBeanDefinition annotatedBeanDefinition = new AnnotatedGenericBeanDefinition(LocalFirstLevelCacheInterceptor.class);
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.add("cacheName", cacheName);
        annotatedBeanDefinition.setPropertyValues(propertyValues);
        String name = beanName + ++beanNameIncrement;
        defaultListableBeanFactory.registerBeanDefinition(name, annotatedBeanDefinition);
        return name;

    }


}
