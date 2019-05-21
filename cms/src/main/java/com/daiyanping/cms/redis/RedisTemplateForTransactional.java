package com.daiyanping.cms.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName RedisTemplateForTransactional
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-05-21
 * @Version 0.1
 */
@Service
public class RedisTemplateForTransactional {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 验证存在spring事物，且开启RedisTemplate事物，看事物是否生效
     * 验证发现事物生效了
     */
    @Transactional
    public void test() {
        BoundValueOperations test_for_transactional = redisTemplate.boundValueOps("test_for_transactional");
        test_for_transactional.increment();
        test_for_transactional.increment();
        test_for_transactional.increment();
        test_for_transactional.increment();
        int a = 1/0;
    }

    /**
     * 验证不存在spring事物，仅开启RedisTemplate事物，看事物是否生效
     * 验证发现RedisTemplate事物并没有生效
     */
    public void test2() {
        BoundValueOperations test_for_transactional = redisTemplate.boundValueOps("test_for_transactional");
        test_for_transactional.increment();
        test_for_transactional.increment();
        test_for_transactional.increment();
        test_for_transactional.increment();
        int a = 1/0;
    }


}
