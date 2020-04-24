package com.daiyanping.cms.cache;

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
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import sungo.util.exception.CommonException;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 本地一级缓存
 */
@Slf4j
@Data
@Scope("prototype")
public class LocalFirstLevelCacheInterceptor extends AbstractCacheInterceptor implements InitializingBean, ChannelAwareMessageListener, BeanFactoryAware {

    protected static final String beanName = "LocalFirstLevelCacheInterceptor";

    private static final String FIRST_LEVEL_CACHE = "FIRST_LEVEL_CACHE";

    private Cache<String, Object> cache;

    private String cacheName;

    private BeanFactory beanFactory;

    private static int increment;

    private static int beanNameIncrement;

    public LocalFirstLevelCacheInterceptor() {
        this.cache = Caffeine.newBuilder()
                // 自定义过期策略
                .expireAfter(new MyExpiry())
                .maximumSize(1000)
                .build();
    }

    @Override
    protected boolean matchMethod(Method method) {
        return "get".equals(method.getName());
    }

    @Override
    protected Object invokeMethod(MethodInvocation invocation) throws Throwable {
        Object[] objects = invocation.getArguments();
        String key = (String) objects[0];
        return cache.get(key, key2 -> {
            try {
                return invocation.proceed();
            } catch (Throwable throwable) {
                throw new RuntimeException("缓存获取异常");
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String queueName = FIRST_LEVEL_CACHE + "." + this.cacheName;
        String exchange = FIRST_LEVEL_CACHE + ".EXCHANGE";

        org.springframework.amqp.core.Queue queue = new org.springframework.amqp.core.Queue(queueName);
        TopicExchange topicExchange = new TopicExchange(exchange);

        Binding binding = BindingBuilder.bind(queue).to(topicExchange).with(FIRST_LEVEL_CACHE + "." + cacheName + ".*");

        ((ConfigurableBeanFactory) this.beanFactory)
                .registerSingleton(queueName + ++this.increment , queue);

        ((ConfigurableBeanFactory) this.beanFactory)
                .registerSingleton(exchange + ++this.increment , topicExchange);

        ((ConfigurableBeanFactory) this.beanFactory)
                .registerSingleton(exchange + "." + cacheName + ++this.increment, binding);

        RabbitListenerContainerFactory rabbitListenerContainerFactory = (RabbitListenerContainerFactory) ApplicationContextProvider.getBean("rabbitListenerContainerFactory");
        AmqpAdmin amqpAdmin = (AmqpAdmin) ApplicationContextProvider.getBean("amqpAdmin");

        AbstractMessageListenerContainer listenerContainer = (AbstractMessageListenerContainer) rabbitListenerContainerFactory.createListenerContainer();
        listenerContainer.setupMessageListener(this);

        listenerContainer.setQueueNames(queueName);
        listenerContainer.setAmqpAdmin(amqpAdmin);

        listenerContainer.start();
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        byte[] body = message.getBody();
        cache.invalidate(new String(body));
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    private static class MyExpiry implements Expiry {

        @Override
        public long expireAfterCreate(@NonNull Object key, @NonNull Object value, long currentTime) {
            return currentTime + 1000 * 60  * 10;
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
        CachingMetadataReaderFactory cachingMetadataReaderFactory = new CachingMetadataReaderFactory();
        MetadataReader metadataReader = null;
        String name1 = LocalFirstLevelCacheInterceptor.class.getName();
        String classpathAllUrlPrefix = ResourceLoader.CLASSPATH_URL_PREFIX;
        String replace = name1.replace(".", "/");
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource resource = pathMatchingResourcePatternResolver.getResource(classpathAllUrlPrefix + replace + ".class");

        try {
            metadataReader = cachingMetadataReaderFactory.getMetadataReader(resource);
        } catch (IOException e) {
            log.error("已经缓存初始化失败", e);
            throw new CommonException("xxxxx");
        }
        ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
        sbd.setResource(resource);
        sbd.setSource(resource);

        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.add("cacheName", cacheName);
        String name = beanName + ++beanNameIncrement;
        defaultListableBeanFactory.registerBeanDefinition(name, sbd);
        return name;

    }

}
