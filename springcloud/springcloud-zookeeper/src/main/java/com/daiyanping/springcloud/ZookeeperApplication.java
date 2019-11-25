package com.daiyanping.springcloud;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;

/**
 * @ClassName EurekaApplication
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-09
 * @Version 0.1
 */
@SpringBootConfiguration
/**
 * 存在eureka依赖，但是又不使用，需要排除掉，否则启动失败
 */
@EnableAutoConfiguration(exclude = {EurekaClientAutoConfiguration.class})
// 安全注册
public class ZookeeperApplication implements ApplicationRunner {

    @Autowired
    CuratorFramework curatorFramework;

    public static void main(String[] args) {
        SpringApplication.run(ZookeeperApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath("/test2", "test3".getBytes());
        byte[] test2s = curatorFramework.getData().forPath("/test2");
        System.out.println(new String(test2s));
    }
}
