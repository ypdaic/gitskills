package com.daiyanping.cms;

import com.alibaba.fastjson.JSONObject;
import com.daiyanping.cms.entity.User;
import com.daiyanping.cms.redis.RedisConfig;
import com.daiyanping.cms.scheduledTask.ScheduledService;
import com.daiyanping.cms.scheduledTask.ScheduledTaskConfig;
import com.daiyanping.cms.scheduledTask.SessionOverview;
import com.daiyanping.cms.scheduledTask.SessionOverviewEvent;
import com.daiyanping.cms.service.IUserService;
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
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Calendar;
import java.util.Date;
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
@ContextConfiguration(classes = {ScheduledTaskConfig.class, RedisConfig.class, MybatisMapperScanTest.class})
//开启自动配置，排除springjdbc自动配置
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@EnableCaching
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
public class ScheduledTaskTests {

    @Autowired
    private ApplicationContext applicationContext;

    private Executor executor = Executors.newFixedThreadPool(5);

    @Autowired
    private ScheduledService scheduledService;

    @Autowired
    private RedisTemplate redisTemplate;

    //引入spring的调度任务管理器，由自动配置提供
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    @Qualifier("service1")
    private IUserService userService;

    @Test
    public void test() throws InterruptedException {
        SessionOverview sessionOverview = new SessionOverview();
        sessionOverview.setSkillGroups(2);
        sessionOverview.setAllocatedSeatsCount(1);
        SessionOverviewEvent sessionOverviewEvent = new SessionOverviewEvent(this, sessionOverview);
        applicationContext.publishEvent(sessionOverviewEvent);
//        Thread.sleep(1000 * 60 * 5);
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

    @Test
    public void test4() throws InterruptedException {
       Thread thread = new Thread(() -> {
           System.out.println("开始往缓存中插入数据");
            redisTemplate.boundValueOps("test").set("aaaa");
           User user = userService.getUserById("1");

       });
       taskScheduler.schedule(thread, new Date(getNextDayZeroTime()));
       Thread.sleep(1000 * 60 * 5);
    }

    /**
     *  获得明天凌晨的时间time
     * @return
     */
    private long getNextDayZeroTime()
    {
        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_MONTH, 1);
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.add(Calendar.MINUTE, 1);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    @Test
    public void test5() {
        long nextDayZeroTime = getNextDayZeroTime();
        Date date = new Date();
        long a = nextDayZeroTime - date.getTime();
        float b = 1000l;
        float c = a/b/60/60;
        System.out.println(c);
    }

    /**
     * 根据给定的time获得昨天凌晨的时间
     * 1、time=null,默认系统时间的昨天凌晨时间
     * 2、time!=null,获得给定time的所在天的前一天的凌晨时间
     *
     * @return 昨天凌晨的时间
     */
    private static long getYesterdayZeroTimeByTime(Long time) {
        Calendar calendar = Calendar.getInstance();
        if (time != null) {
            calendar.setTimeInMillis(time);
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    @Test
    public void test6() {
//        scheduledService.transactionManagementTest();
        try {
            Thread.sleep(1000 * 60 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
