package com.daiyanping.cms;

import com.daiyanping.cms.entity.User;
import org.springframework.context.annotation.*;

/**
 * @ClassName AppConfig
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-03-27
 * @Version 0.1
 */
@Configuration //表明此类是配置类
@EnableImportSelectorTest(testValue = 1)
@PropertySource("classpath:application.yml")
public class AppConfig {

//    @Bean
//    @Profile("dev")
//    public User getUser() {
//        User user = new User();
//        return user;
//    }

}
