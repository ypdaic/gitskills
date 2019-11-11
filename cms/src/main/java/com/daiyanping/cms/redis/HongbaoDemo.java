package com.daiyanping.cms.redis;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

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
    private final static String HAS_GRAP_HONG_BAO = "has_grap_hong_bao";

    // 用户记录
    private final static String USER = "user:%d";

    // 抢得红包的用户信息
    private final static String USER_HONG_BAO_INFO = "user_hong_bao_info";



    static final ExecutorService hongbaoPoolExecutor = Executors.newFixedThreadPool(4);

    static final ExecutorService grabhongbaoExecutor = Executors.newFixedThreadPool(100);

    public static void main(String[] args) {
        JedisShardInfo jedisShardInfo = new JedisShardInfo("192.168.1.5", 6379);
        jedisShardInfo.setPassword("test1234");
        Jedis jedis = new Jedis(jedisShardInfo);

    }

    

    public static void initHongbaoPool(int hongbaoCount, Jedis jedis) {
        

        List<CompletableFuture> completableFutures = new ArrayList<>();

        for (int i = 0; i < hongbaoCount; i++) {


            Hongbao hongbao = new Hongbao();
            hongbao.setId(i);
            hongbao.setMoney(new BigDecimal(i));
            CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {

                jedis.lpush(HONG_BAO_POOL, JSONObject.toJSONString(hongbao));
                return null;
            }, hongbaoPoolExecutor);
            completableFutures.add(completableFuture);

        }

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
    

    private static void grabHongbao(int userCount, Jedis jedis) {
        List<CompletableFuture> completableFutures = new ArrayList<>();
        for (int i = 0; i < userCount; i++) {
            User user = new User();
            user.setId(i);
            CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {
                ArrayList<String> keyList = new ArrayList<>();
                keyList.add(HONG_BAO_POOL);
                keyList.add(HAS_GRAP_HONG_BAO);
                keyList.add(USER_HONG_BAO_INFO);

                ArrayList<String> argsList = new ArrayList<>();
                argsList.add(String.format(USER, user.getId().toString()));
                argsList.add(user.getId().toString());
                long time = new Date().getTime();
                argsList.add(String.valueOf(time));

                jedis.eval("", keyList, argsList);
                return null;
            }, grabhongbaoExecutor);
            completableFutures.add(completableFuture);
            
        }
        
    }
}
