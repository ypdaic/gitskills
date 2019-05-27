package com.daiyanping.cms;

import com.daiyanping.cms.entity.User;
import com.daiyanping.cms.redis.RedisConfig;
import com.daiyanping.cms.redis.RedisTemplateForTransactional;
import com.daiyanping.cms.service.IUserService;
import com.daiyanping.cms.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.Ordered;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.support.collections.DefaultRedisSet;
import org.springframework.data.redis.support.collections.DefaultRedisZSet;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisTests
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-15
 * @Version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//@ContextConfiguration(classes = {RedisConfig.class, MybatisMapperScanTest.class})
@ContextConfiguration(classes = {MybatisMapperScanTest.class, RedisConfig.class})
//开启自动配置，排除springjdbc自动配置
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
//开启缓存 如果想要支持事物，缓存必须比事物后加载，这就是设置order，缓存的大于事物的order，这个order会用于后续advisors的排序，所以要想支持事物，缓存的调用链必须在事物的调用链中
//否则事物都结束了，即使缓存开启了事物支持，也是无效的
@EnableCaching(order = 2)
@EnableTransactionManagement
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    //@Qualifier注解用于存在多个相同类型的bean时，注入指定名称的bean
    @Autowired
    @Qualifier("service1")
    private IUserService userService;

    private final int threadCount = 1;
    private CountDownLatch countDownLatch = new CountDownLatch(threadCount);

    @Autowired
    RedisTemplateForTransactional redisTemplateForTransactional;

    @Autowired
    RedisSerializer keySerializer;

    @Autowired
    RedisScript<Boolean> script;

    @Test
    public void test() {
        List<User> users = userService.getAll();
        redisTemplate.boundValueOps("user").set(users, 10, TimeUnit.MINUTES);
        List<User> list = (List<User>) redisTemplate.boundValueOps("user").get();
        System.out.println(list);

        redisTemplate.boundListOps("user2").leftPush(users);
        List<User> user2 = (List<User>) redisTemplate.boundListOps("user2").leftPop();
        redisTemplate.boundListOps("user2").leftPush(user2, users);
//        System.out.println(user2);

    }

    @Test
    public void test3() {
//        Set<String> test = redisTemplate.keys("test");
//        System.out.println(test);
        Boolean delete = redisTemplate.delete("test");
        System.out.println(delete);
//        redisTemplate.boundValueOps("test:2").set("sssss");

    }

    /**
     * 使用缓存解决数据库压力，比如突发大量请求，如果都去请求数据库，就会将
     * 数据库搞挂掉，需要使用缓存解决该问题
     */
    @Test
    public void test2() {
//        for (int i = 0; i < threadCount; i++) {
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    countDownLatch.countDown();
//                    try {
//                        countDownLatch.await();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    User userById = userService.getUserById("1");
//                }
//            });
//
//            thread.start();
//
//        }
//
//        try {
//            Thread.sleep(1000 * 60 * 5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        User userById = userService.getUser("1");
    }

    /**
     * 测试RedisTemplate的事物功能
     *
     */
    @Test
    public void test4() {
//        redisTemplateForTransactional.test();
//        redisTemplateForTransactional.test2();
        redisTemplateForTransactional.test3();
    }

    /**
     * 测试RedisTemplate的executePipelined(管道)功能,返回每个命令的执行结果
     * key,value 必须等序列化
     * 管道模式并不能支持事物，
     * 流水线用于发出命令而不立即请求响应，而是在结束时 虽然有点类似于MULTI，但流水线并不能保证原子性 - 它只是在发出大量命令时（例如在批处理场景中）尝试提高性能。
     *
     * 管道就是一次性向Redis发送多个命令，然后redis一起执行
     *
     */
    @Test
    public void test5() {
        List list = redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                System.out.println(redisTemplate.getValueSerializer().serialize("sdfsfs"));
                System.out.println("sdfsfs".getBytes());
                connection.set("test_for_pipeline".getBytes(), redisTemplate.getValueSerializer().serialize("sdfsfs"));
                connection.set("test_for_pipeline2:".getBytes(), "sdfsfs".getBytes());
                return null;
            }
        });
        System.out.println(list);
    }

    /**
     * 测试RedisTemplate不使用spring的事物管理，且RedisTemplate不开启事物功能，只使用Redis的事物功能，使用RedisCallback方式，
     * 由于RedisCallback方式使用的是RedisConnectionUtils.getConnection获取连接，不是使用RedisConnectionUtils.bindConnection，RedisConnectionUtils.bindConnection
     * 方式会将连接绑定到spring事物threadLocal中，但RedisCallback中的操作都是使用的同一个连接(客户端)，我们在同一个连接(客户端)上开启redis事物是ok的
     *
     *
     */
    @Test
    public void test6() {
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                try {

                    connection.multi();
                    connection.set("test_for_pipeline".getBytes(), redisTemplate.getValueSerializer().serialize("sdfsfs"));
                    int a = 1/0;
                    connection.set("test_for_pipeline2:".getBytes(), redisTemplate.getValueSerializer().serialize("少时诵诗书ssssddddd所"));
                    connection.exec();
                } catch (Exception e) {
                    connection.discard();
                }
                return null;
            }
        });
    }

    /**
     * 测试RedisTemplate不使用spring的事物管理，且RedisTemplate不开启事物功能，只使用Redis的事物功能，使用SessionCallback方式
     * 在使用SessionCallback时，由于其使用的是RedisOperations，而不是RedisCallback的RedisConnection，我们的每个命令都是开启一个
     * 新的连接去执行，但SessionCallback方式使用的是RedisConnectionUtils.bindConnection获取连接，不是使用RedisConnectionUtils.getConnection获取，
     * 此时将连接绑定到spring事物threadLocal中后续获取新的连接都是同一个连接
     *
     *
     */
    @Test
    public void test7() {
        redisTemplate.execute(new SessionCallback() {

            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                try {
                    operations.multi();
                    operations.boundValueOps("test_for_pipeline").set("sdfsffffs少时诵诗书kksss");
                    int a = 1/0;
                    operations.boundValueOps("test_for_pipeline2").set("sdfsfffssssssfssssskkkk");
                    operations.exec();
                } catch (Exception e) {
                    operations.discard();
                }
                return null;
            }
        });
    }

    /**
     * 测试RedisTemplate不使用spring的事物管理，且RedisTemplate不开启事物功能，只使用Redis的事物功能，使用RedisOperations方式
     * 这种模式是允许的，一个jedis在开启事物后，退出，jedis不允许这种情况出现，代码在BinaryJedis 764行
     * 如果开启RedisTemplate事物功能，虽然可以正常执行，但这种存在内存泄漏问题，连接永远不会关闭(非连接池场景)，且不会从spring事物中释放，存在内存泄漏问题
     *
     * 这种操作时非法的
     */
    @Test
    public void test8() {
        redisTemplate.multi();
        BoundValueOperations test_for_pipeline = redisTemplate.boundValueOps("test_for_pipeline");
        test_for_pipeline.set("sfsfsfsssssssss");
        BoundValueOperations test_for_pipeline2 = redisTemplate.boundValueOps("test_for_pipeline2");
        test_for_pipeline2.set("fdddddddddd");
        redisTemplate.discard();
    }

    /**
     * 结合test4,test5,test6，test7，test8可以得出结论，在使用RedisTemplate时无论是使用execute(RedisCallback)方式，还是使用execute(SessionCallback)方式都是在同一个连接（客户端）上进行，不管是否开启了RedisTemplate事物开关
     * 如果是使用RedisOperations方式，RedisOperations的每个方法最后都是调用的execute(RedisCallback)，所以RedisOperations的每个方法都是在新的连接（客户端）上进行
     * 这个是无法保证redis事物的（也不允许使用这种方式运行），也无法进行管道模式，想要使用事物就只能通过execute(SessionCallback)方式，或者开启Redistemplate的事物开关并同时开启spring事物，否则会导致内存泄漏
     *
     */

    /**
     * RedisConnectionFactory. getConvertPipelineAndTxResults的作用，转换管道，事物的执行结果，其实现JedisConnectionFactory，LettuceConnectionFactory默认为false
     */

    /**
     * RedisTemplate lua脚本支持，写一个检查并设置的方法
     */
    @Test
    public void checkAndSet() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("test");
        Integer[] integers = {1, 2};
        Object execute = redisTemplate.execute(script, arrayList, integers);
        System.out.println(execute);
    }

    /**
     * RedisTemplate
     */
    @Test
    public void test9() {
        DefaultRedisZSet<String> strings = new DefaultRedisZSet<String>("skill_groups_id:", redisTemplate);
        strings.add("1", System.currentTimeMillis());
        strings.add("2", System.currentTimeMillis());
        strings.add("3", System.currentTimeMillis());
        /**
         * 只能获取value
         */
        Set<String> strings1 = strings.rangeByScore(0, System.currentTimeMillis());
        System.out.println(strings1);

        /**
         * 获取得分和value
         */
        Set<ZSetOperations.TypedTuple<String>> typedTuples = strings.rangeByScoreWithScores(0, System.currentTimeMillis());
        typedTuples.forEach(typedTuple -> {
            System.out.println(typedTuple.getScore());
            System.out.println(typedTuple.getValue());
        });

        strings.remove("2");
    }
}
