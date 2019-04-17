package com.daiyanping.cms.scheduledTask;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @ClassName ScheduledTaskConfig
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-17
 * @Version 0.1
 */
@Configuration
@ComponentScan(value = {"com.daiyanping.cms.scheduledTask"})
public class ScheduledTaskConfig {
}
