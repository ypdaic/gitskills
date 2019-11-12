package com.daiyanping.cms.redis;

import com.alibaba.fastjson.JSONObject;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName HongbaoDemo
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-11-11
 * @Version 0.1
 */
public class HongbaoDemo {

    // 红包池key
    private final static String HONG_BAO_POOL = "hong_bao_poll";

    // 已抢得红包用户key
    private final static String HAS_GRAB_HONG_BAO = "has_grab_hong_bao";

    // 用户记录
    private final static String USER = "user:%s";

    // 抢得红包的用户信息
    private final static String USER_HONG_BAO_INFO = "user_hong_bao_info";

    // 重复抢
    private final static String GRAB_REPEAT = "GRAB_REPEAT";

    // 抢红包成功
    private final static String GRAB_SUCCESS = "GRAB_SUCCESS";

    // 抢红包失败
    private final static String GRAB_FAIL = "GRAB_FAIL";

    // 红包初始初始化线程池
    static final ExecutorService hongbaoPoolExecutor = Executors.newFixedThreadPool(4);

    // 抢红包线程池
    static final ExecutorService grabhongbaoExecutor = Executors.newFixedThreadPool(100);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // jedis 多线程不安全，需要使用连接池，这里不使用连接池，使用lettuce
        RedisURI redisUrl = RedisURI.Builder.redis("127.0.0.1", 6379).build();
        ClientResources clientResoure = DefaultClientResources.create();
        RedisClient redisClient = RedisClient.create(clientResoure, redisUrl);
        // 初始化红包池
        initHongbaoPool(100, redisClient);

        // 抢红包
        grabHongbao(200, redisClient);
    }


    /**
     * 初始化红包
     * @param hongbaoCount
     * @param redisClient
     */
    public static void initHongbaoPool(int hongbaoCount, RedisClient redisClient) {
        List<CompletableFuture> completableFutures = new ArrayList<>();
        for (int i = 0; i < hongbaoCount; i++) {
            Hongbao hongbao = new Hongbao();
            hongbao.setId(i);
            hongbao.setMoney(new BigDecimal(i));
            CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {
                RedisAsyncCommands<String, String> async = redisClient.connect().async();
                async.lpush(HONG_BAO_POOL, JSONObject.toJSONString(hongbao));
                return null;
            }, hongbaoPoolExecutor);
            completableFutures.add(completableFuture);

        }
        // 等待初始化完成
        for (CompletableFuture completableFuture : completableFutures) {
            try {
                completableFuture.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * 抢红包
     * @param userCount
     * @param redisClient
     */
    private static void grabHongbao(int userCount, RedisClient redisClient) throws ExecutionException, InterruptedException {
        List<CompletableFuture> completableFutures = new ArrayList<>(userCount);

        for (int i = 0; i < userCount; i++) {
            User user = new User();
            user.setId((int) (Math.random() * userCount));
            CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {
                ArrayList<String> keyList = new ArrayList<>();
                keyList.add(HONG_BAO_POOL);
                keyList.add(HAS_GRAB_HONG_BAO);
                keyList.add(USER_HONG_BAO_INFO);

                ArrayList<String> argsList = new ArrayList<>();
                argsList.add(String.format(USER, user.getId().toString()));
                argsList.add(user.getId().toString());
                long time = new Date().getTime();
                argsList.add(String.valueOf(time));
                RedisAsyncCommands<String, String> async = redisClient.connect().async();
                RedisFuture future = async.eval(
                        "local value = redis.call('sismember',KEYS[2], ARGV[1]);" +
                                "if value == 1 then" +
                                "    return 'GRAB_REPEAT';" +
                                "else" +
                                "    local hongBao = redis.call('rpop', KEYS[1]);" +
                                "    if hongBao then" +
                                "        local hongbaoJson = cjson.decode(hongBao);" +
                                "        hongbaoJson['userId'] = ARGV[2];" +
                                "        redis.call('zadd', KEYS[3], ARGV[3], cjson.encode(hongbaoJson));" +
                                "        redis.call('sadd', KEYS[2], ARGV[1]);" +
                                "        return 'GRAB_SUCCESS';" +
                                "    else" +
                                "        return 'GRAB_FAIL';" +
                                "    end;" +
                                "end;", ScriptOutputType.STATUS, keyList.toArray(new String[keyList.size()]), argsList.toArray(new String[argsList.size()]));
                String result = "";
                try {
                    result = (String) future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                // 打印抢红包结果
                switch (result) {
                    case GRAB_REPEAT:
                        System.out.println("用户：" + user.getId() + "  重复抢红包");
                        break;
                    case GRAB_SUCCESS:
                        System.out.println("用户：" + user.getId() + "  抢红包成功");
                        break;
                    case GRAB_FAIL:
                        System.out.println("用户：" + user.getId() + "  抢红包失败");
                        break;
                }

                return null;

            }, grabhongbaoExecutor);

            completableFutures.add(completableFuture);

        }

        // 等待抢红包完成
        for (CompletableFuture completableFuture : completableFutures) {
            try {
                completableFuture.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n\n---------------抢得的红包及用户信息--------------\n\n");
        RedisAsyncCommands<String, String> async = redisClient.connect().async();

        RedisFuture<List<String>> zrangebyscore = async.zrange(USER_HONG_BAO_INFO, 0, -1);
        List<String> userInfos = null;
        try {
            userInfos = zrangebyscore.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // 打印抢得红包的用户信息
        userInfos.forEach(userInfo -> {
            Hongbao hongbao = JSONObject.parseObject(userInfo, Hongbao.class);
            System.out.println("红包id:" + hongbao.getId() + "  红包金额:" + hongbao.getMoney() + "  用户id:" + hongbao.getUserID());

        });

    }


}
