package com.daiyanping.cms.redis;

import com.daiyanping.cms.cache.RedisCacheManagerInterceptor;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.transaction.TransactionAwareCacheManagerProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;

import static io.lettuce.core.ReadFrom.SLAVE_PREFERRED;

/**
 * @ClassName RedisConfig
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-15
 * @Version 0.1
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
@ComponentScan("com.daiyanping.cms.redis")
@Import({NoConfigAnnotation.class})
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

    @Autowired
    RedisCacheManagerInterceptor redisCacheManagerInterceptor;

    /**
     * springboot 自动配置默认会配置RedisCacheManager，但是默认使用JdkSerializationRedisSerializer进行序列化
     * 不符合JSON场景，这就需要我们自定义配置
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory, RedissonClient redissonClient, ThreadPoolTaskScheduler TaskScheduler) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(cacheProperties.getRedis().getTimeToLive())
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
                .disableCachingNullValues();

        // 查看使用builder(redisConnectionFactory)创建的DefaultRedisCacheWriter的代码发现DefaultRedisCacheWriter可以支持分布式锁操作,
//        加锁就是创建一个key+ "~lock" 解锁就是删除key+ "~lock",其他需要锁定的操作通过while循环判断key+ "~lock"是否存在,存在就睡眠一段
//        时间,但是该功能默认是关闭的,页启动不了,只能说是一个简单的分布式锁
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisConnectionFactory);
        builder = builder.cacheDefaults(config);
        //获取初始化缓存库名称
        List<String> cacheNames = this.cacheProperties.getCacheNames();
        if (!cacheNames.isEmpty()) {
            builder.initialCacheNames(new LinkedHashSet<>(cacheNames));
        }

        // 对每个缓存空间应用不同的配置
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        configMap.put("timeGroup", config);
        configMap.put("user", config.entryTtl(Duration.ofSeconds(120)));

        builder.withInitialCacheConfigurations(configMap);

        RedisCacheManager redisCacheManager = builder
                .transactionAware()
                .build();
//        RedisCacheManagerProxy redisCacheManagerProxy = new RedisCacheManagerProxy(redisCacheManager, redissonClient, TaskScheduler);
////
////        Enhancer enhancer = new Enhancer();
////        enhancer.setSuperclass(RedisCacheManager.class);
////        enhancer.setInterfaces(new Class<?>[] {CacheManager.class});
////        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
////        enhancer.setCallbackTypes(new Class[]{MethodInterceptor.class});
////        enhancer.setCallback(redisCacheManagerProxy);
////        RedisCacheManager redisCacheManager2 = (RedisCacheManager) enhancer.create(new Class[]{RedisCacheWriter.class, RedisCacheConfiguration.class}, new Object[]{new MyRedisCacheWriter(), config});
////
////        return redisCacheManager2;
        ProxyFactory factory = new ProxyFactory();
        factory.setExposeProxy(true);
        redisCacheManagerInterceptor.setRedisCacheManager(redisCacheManager);
        factory.addAdvisor(new DefaultPointcutAdvisor(redisCacheManagerInterceptor));
        factory.setTarget(redisCacheManager);
        redisCacheManager = (RedisCacheManager) factory.getProxy(RedisCacheManager.class.getClassLoader());

        return redisCacheManager;
    }

    /**
     * 本地缓存，并且支持事务
     * @return
     */
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager concurrentMapCacheManager = new ConcurrentMapCacheManager();
        concurrentMapCacheManager.setAllowNullValues(true);
        concurrentMapCacheManager.setCacheNames(Collections.singletonList("test"));
        TransactionAwareCacheManagerProxy transactionAwareCacheManagerProxy = new TransactionAwareCacheManagerProxy();
        transactionAwareCacheManagerProxy.setTargetCacheManager(concurrentMapCacheManager);
        return transactionAwareCacheManagerProxy;
    }

    /**
     * 自定义key的生成
     * @return
     */
    public KeyGenerator keyGenerator(){
        KeyGenerator keyGenerator = new KeyGenerator() {
//            target表示业务的实例，method表示业务的方法，params表示业务参数
            @Override
            public Object generate(Object target, Method method, Object... params) {
                return null;
            }
        };

        return keyGenerator;
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
     * lua脚本
     * 在应用程序上下文中配置DefaultRedisScript的单个实例是理想的，以避免在每次执行脚本时重新计算脚本的SHA1。
     * @return
     */
    @Bean
    public RedisScript<Boolean> script() {

        ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("checkandset.lua"));
        try {
            return RedisScript.of(scriptSource.getScriptAsString(), Boolean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RedisScript.of("", Boolean.class);
    }

    @Bean
    public LettuceClientConfigurationBuilderCustomizer builderCustomizers() {
        return new LettuceClientConfigurationBuilderCustomizer() {

            /**
             * 设置先访问从机，如果从机没有就读取主机
             * @param clientConfigurationBuilder
             */
            @Override
            public void customize(LettuceClientConfiguration.LettuceClientConfigurationBuilder clientConfigurationBuilder) {
                clientConfigurationBuilder.readFrom(SLAVE_PREFERRED);
            }
        };
    }

    /**
     * lua脚本
     * 在应用程序上下文中配置DefaultRedisScript的单个实例是理想的，以避免在每次执行脚本时重新计算脚本的SHA1。
     * @return
     */
    @Bean
    public RedisScript<Boolean> retryCountCheckScript() {

        ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("lua/retryCountCheck.lua"));
        try {
            return RedisScript.of(scriptSource.getScriptAsString(), Boolean.class);
        } catch (IOException e) {
            log.error("lua 脚本加载失败", e);
        }
        return RedisScript.of("", Boolean.class);
    }

    @Autowired
    private MessageListener myMessageListener;

    /**
     * redis 发布订阅
     * @param redisConnectionFactory
     * @param taskExecutor
     * @return
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory, ThreadPoolTaskExecutor taskExecutor) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        redisMessageListenerContainer.setTaskExecutor(taskExecutor);
        PatternTopic patternTopic = PatternTopic.of("channel*");
//        ChannelTopic channel = ChannelTopic.of("channel");
        redisMessageListenerContainer.addMessageListener(myMessageListener, patternTopic);
        return redisMessageListenerContainer;
    }

}
