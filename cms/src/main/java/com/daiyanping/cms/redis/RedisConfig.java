package com.daiyanping.cms.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @ClassName RedisConfig
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-15
 * @Version 0.1
 */
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
@ComponentScan("com.daiyanping.cms.redis")
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
        // 设置Hash数据类型的value的序列化采用jsonRedisSerializer。
        redisTemplate.setHashValueSerializer(valueSerializer());
        // RedisTemplate开启事物支持，将会以Redis的事物方式提交数据，并结合spring的事物管理，这个和CacheManager的事物是不一样的，CacheManager只依赖Spring的事物，数据的提交并不支持事物，
        // 但基于Redis的缓存框架，基本上提交数据是单个命令，并不需要Redis事物的支持，RedisCacheManager使用DefaultRedisCacheWriter去操作Redis并不是使用RedisTemplate去操作Redis
        redisTemplate.setEnableTransactionSupport(true);
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

        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisConnectionFactory);
        builder = builder.cacheDefaults(config);
        //获取初始化缓存库名称
        List<String> cacheNames = this.cacheProperties.getCacheNames();
        if (!cacheNames.isEmpty()) {
            builder.initialCacheNames(new LinkedHashSet<>(cacheNames));
        }

        RedisCacheManager redisCacheManager = builder
                .transactionAware()
                .build();
        //初始化缓存库
        redisCacheManager.afterPropertiesSet();

        return redisCacheManager;
    }

    private RedisSerializer keySerializer() {
        return new StringRedisSerializer();
    }

    private RedisSerializer valueSerializer() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        //所有的属性都可以访问到（从private 到public)
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //通过 objectMapper.enableDefaultTyping() 方法设置
        //
        //即使使用 Object.class 作为 jcom.fasterxml.jackson.databind.JavaType 也可以实现相应类型的序列化和反序列化
        //
        //好处：只定义一个序列化器就可以了（通用）
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }

}
