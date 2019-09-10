package com.daiyanping.ribbon;

import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RibbonConfig
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-10
 * @Version 0.1
 *
 * 而外单独为每个服务添加RibbonConfig，但是要确保和Application不在同一个包中，否则该配置是所有服务共享的
 */
@Configuration
public class RibbonConfig {

    @Bean
    public IRule ribbonRule() { // 其中IRule就是所有规则的标准
        return new com.netflix.loadbalancer.RoundRobinRule(); // 轮询的访问策略
    }
}
