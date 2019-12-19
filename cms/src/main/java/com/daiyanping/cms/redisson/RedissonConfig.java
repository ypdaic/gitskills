package com.daiyanping.cms.redisson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName RedissonConfig
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-05-30
 * @Version 0.1
 */
@Configuration
@EnableCaching
public class RedissonConfig {

    @Bean
    public RedissonClient redisson() throws IOException {
//        Config config = Config.fromYAML(new ClassPathResource("redisson_dev_config.yml").getInputStream());
        Config config = Config.fromYAML(new ClassPathResource("redisson/redisson-single-config.yml").getInputStream());
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

    @Bean
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
        return new RedissonConnectionFactory(redisson);
    }

    /**
     * @Bean注解默认使用方法名作为bean的name，如果想自定义bean name 就必须指定
     *
     * RedisConnectionFactory由spring 自动配置模块注入进来
     */
    @Bean("redisTemplate")
    public RedisTemplate getRedisTemplate(RedisConnectionFactory redissonConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redissonConnectionFactory);
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

    @Bean
    public RedisSerializer keySerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    public RedisSerializer valueSerializer() {
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

    /**
     * Redisson 提供的springcache的支持, 提供两种缓存机制,过期和不过期,默认是不过期的,如果要过期的需要提供CacheConfig
     * 并为每个缓存空间提供一个CacheConfig,RedissonCache提供的同步机制是分布式锁,而redis通过的同步机制是本地锁
     * @param redissonConnectionFactory
     * @param redisson
     * @return
     */
    @Bean
    public RedissonSpringCacheManager redisCacheManager(RedissonConnectionFactory redissonConnectionFactory, RedissonClient redisson) {
        Map<String, CacheConfig> cacheConfigHashMap = new HashMap<>();
        cacheConfigHashMap.put("test", cacheConfig());
        RedissonSpringCacheManager redissonSpringCacheManager = new RedissonSpringCacheManager(redisson, cacheConfigHashMap);
//        redissonSpringCacheManager.setCacheNames();
        return redissonSpringCacheManager;

    }

    @Bean
    public CacheConfig cacheConfig() {
        CacheConfig cacheConfig = new CacheConfig();
        cacheConfig.setTTL(1000 * 60 * 60 * 12);
        cacheConfig.setMaxIdleTime(0);
        return cacheConfig;
    }
}
