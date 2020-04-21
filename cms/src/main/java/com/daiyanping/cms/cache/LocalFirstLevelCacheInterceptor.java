package com.daiyanping.cms.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.rabbitmq.client.Channel;
import lombok.Data;
import org.aopalliance.intercept.MethodInvocation;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 本地一级缓存
 */
@Data
@Component
@Scope("prototype")
public class LocalFirstLevelCacheInterceptor extends AbstractCacheInterceptor implements InitializingBean, ChannelAwareMessageListener
{

    private Cache<String, Object> cache;

    private String name;

    private static String FIRST_LEVEL_CACHE = "first_level_cache";

    public LocalFirstLevelCacheInterceptor(String name) {
        this.cache = Caffeine.newBuilder()
                // 自定义过期策略
                .expireAfter(new MyExpiry())
                .maximumSize(3)
                .build();
        this.name = name;
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
        Queue name = new Queue("name");
        RabbitListenerContainerFactory rabbitListenerContainerFactory = (RabbitListenerContainerFactory) ApplicationContextProvider.getBean("rabbitListenerContainerFactory");
        AmqpAdmin amqpAdmin = (AmqpAdmin) ApplicationContextProvider.getBean("amqpAdmin");
        AbstractMessageListenerContainer listenerContainer = (AbstractMessageListenerContainer) rabbitListenerContainerFactory.createListenerContainer();
        listenerContainer.setupMessageListener(this);

        listenerContainer.setQueueNames(FIRST_LEVEL_CACHE + this.name);
        listenerContainer.setAmqpAdmin(amqpAdmin);
        listenerContainer.set

        listenerContainer.start();
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

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

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name)))
    public void removeCache() {

    }
}
