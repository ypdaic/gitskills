package com.daiyanping.cms.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;

/**
 * @ClassName RedisConfig
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-15
 * @Version 0.1
 */
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class RedisConfig {


    /**
     * @Bean注解默认使用方法名作为bean的name，如果想自定义bean name 就必须指定
     *
     * RedisConnectionFactory由spring 自动配置模块注入进来
     */
    @Bean("redisTemplate")
    public RedisTemplate getRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // JdkSerializationRedisSerializer 使用JDK的序列化方式，也就是使用ObjectInputStream,ObjectOutStream进行序列化，
        // 并且序列化的对象必须实现Serializable接口，在存储内容时，除了属性的内容外还存了其它内容在里面，总长度长，且不容易阅读。
//        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();

        // GenericJackson2JsonRedisSerializer可以自己定义ObjectMapper,并且序列化的对象中会带上类信息
//        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();


        // 设置值（value）的序列化采用JdkSerializationRedisSerializer
        redisTemplate.setValueSerializer(valueSerializer());
//        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
        // 设置键（key）的序列化采用StringRedisSerializer。
        redisTemplate.setKeySerializer(keySerializer());

        // 设置Hash数据类型的key的序列化采用StringRedisSerializer。
        redisTemplate.setHashKeySerializer(keySerializer());
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Autowired
    private CacheProperties cacheProperties;

    /**
     * springboot 自动配置默认会配置RedisCacheManager，但是默认使用JdkSerializationRedisSerializer进行序列化
     * 不符合JSON场景，这就需要我们自定义配置
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(cacheProperties.getRedis().getTimeToLive())
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
                .disableCachingNullValues();

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();

        return redisCacheManager;
    }

    private RedisSerializer keySerializer() {
        return new StringRedisSerializer();
    }

    private RedisSerializer valueSerializer() {
        return new Jackson2JsonRedisSerializer(Object.class);
    }

}
