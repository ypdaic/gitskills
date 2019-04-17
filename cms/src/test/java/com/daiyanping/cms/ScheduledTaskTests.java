package com.daiyanping.cms;

import com.alibaba.fastjson.JSONObject;
import com.daiyanping.cms.redis.RedisConfig;
import com.daiyanping.cms.scheduledTask.ScheduledService;
import com.daiyanping.cms.scheduledTask.ScheduledTaskConfig;
import com.daiyanping.cms.scheduledTask.SessionOverview;
import com.daiyanping.cms.scheduledTask.SessionOverviewEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @ClassName ScheduledTaskTests
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-17
 * @Version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {ScheduledTaskConfig.class, RedisConfig.class})
//开启自动配置，排除springjdbc自动配置
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@EnableCaching
public class ScheduledTaskTests {

    @Autowired
    private ApplicationContext applicationContext;

    private Executor executor = Executors.newFixedThreadPool(5);

    @Autowired
    private ScheduledService scheduledService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() throws InterruptedException {
        SessionOverview sessionOverview = new SessionOverview();
        sessionOverview.setSkillGroups(2);
        sessionOverview.setAllocatedSeatsCount(1);
        SessionOverviewEvent sessionOverviewEvent = new SessionOverviewEvent(this, sessionOverview);
        applicationContext.publishEvent(sessionOverviewEvent);
        Thread.sleep(1000 * 60 * 5);
    }

    @Test
    public void test2() {
        applicationContext.publishEvent(new MyApplicationEvent(this));
    }

    @PostConstruct
    public void init() {
        System.out.println("前置执行");
        Set keys = redisTemplate.keys(ScheduledService.SESSION_OVERVIEW.replace("%s", "*"));
        redisTemplate.delete(keys);
        SimpleApplicationEventMulticaster simpleApplicationEventMulticaster = (SimpleApplicationEventMulticaster) applicationContext.getBean(AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME);
        simpleApplicationEventMulticaster.setTaskExecutor(executor);
    }

    class MyApplicationEvent extends ApplicationEvent {

        /**
         * Create a new ApplicationEvent.
         *
         * @param source the object on which the event initially occurred (never {@code null})
         */
        public MyApplicationEvent(Object source) {
            super(source);
        }
    }

    @Test
    public void test3() {
        JSONObject sessonOverview = scheduledService.getSessonOverview();
        System.out.println(sessonOverview);

    }
}
