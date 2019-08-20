package com.daiyanping.cms.redis;

import com.daiyanping.cms.entity.User;
import org.springframework.context.annotation.Bean;

/**
 * @ClassName NoConfigAnnotation
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-08-20
 * @Version 0.1
 */
public class NoConfigAnnotation {

    @Bean
    public User User() {
        return new User();
    }
}
