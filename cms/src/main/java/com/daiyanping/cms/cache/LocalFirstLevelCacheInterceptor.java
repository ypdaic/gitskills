package com.daiyanping.cms.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.rabbitmq.client.Channel;
import lombok.Data;
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
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 本地一级缓存
 */
@Data
@Component(value = LocalFirstLevelCacheInterceptor.beanName)
@Scope("prototype")
public class LocalFirstLevelCacheInterceptor extends AbstractCacheInterceptor implements InitializingBean, ChannelAwareMessageListener, BeanFactoryAware {

    protected static final String beanName = "LocalFirstLevelCacheInterceptor";

    private static final String FIRST_LEVEL_CACHE = "FIRST_LEVEL_CACHE";

    private Cache<String, Object> cache;

    private String name;

    private BeanFactory beanFactory;

    private static int increment;

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
        String queueName = FIRST_LEVEL_CACHE + "." + this.name;
        String exchange = FIRST_LEVEL_CACHE + ".EXCHANGE";

        org.springframework.amqp.core.Queue queue = new org.springframework.amqp.core.Queue(queueName);
        TopicExchange topicExchange = new TopicExchange(exchange);

        Binding binding = BindingBuilder.bind(queue).to(topicExchange).with(FIRST_LEVEL_CACHE + "." + name + ".*");

        ((ConfigurableBeanFactory) this.beanFactory)
                .registerSingleton(queueName + ++this.increment , queue);

        ((ConfigurableBeanFactory) this.beanFactory)
                .registerSingleton(exchange + ++this.increment , topicExchange);

        ((ConfigurableBeanFactory) this.beanFactory)
                .registerSingleton(exchange + "." + name + ++this.increment, binding);

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
     * 属性注入
     * @param beanFactory
     * @param name
     */
    public static void setValue(BeanFactory beanFactory, String name) {
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        BeanDefinition beanDefinition = defaultListableBeanFactory.getBeanDefinition(beanName);
        System.out.println("LocalFirstLevelCacheInterceptor的hashcode:" + beanDefinition.hashCode());
        AbstractBeanDefinition abstractBeanDefinition = (AbstractBeanDefinition) beanDefinition;
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.add("name", name);
        abstractBeanDefinition.setPropertyValues(propertyValues);
    }

}
