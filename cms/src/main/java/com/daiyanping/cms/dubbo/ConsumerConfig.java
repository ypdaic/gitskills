package com.daiyanping.cms.dubbo;

import com.daiyanping.cms.dubbo.provider.ProviderService;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.*;


/**
 * @ClassName ConsumerConfig
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-06-24
 * @Version 0.1
 */
@Configuration
@ComponentScan("com.daiyanping.cms.dubbo.consumer")
//@ImportResource("classpath:dubbo/consumer.xml")

@EnableDubbo(scanBasePackages = "com.daiyanping.cms.dubbo.consumer")
@PropertySource("classpath:/dubbo/consumer.properties")
public class ConsumerConfig {

    /**
     * javaconfig的形式进行dubbo配置
     * API使用范围说明：API 仅用于 OpenAPI, ESB, Test, Mock 等系统集成，普通服务提供方或消费方，请采用XML 配置方式使用 Dubbo ↩︎
     * @return
     */
//    @Bean
//    public ProviderService consumer() {
//        // 当前应用配置
//        ApplicationConfig application = new ApplicationConfig();
//        application.setName("demo-consumer");
//
//        // 连接注册中心配置
//        RegistryConfig registry = new RegistryConfig();
//        registry.setAddress("zookeeper://127.0.0.1:2181");
//        registry.setUsername("aaa");
//        registry.setPassword("bbb");
//
//        // 注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接
//
//        // 引用远程服务
//        ReferenceConfig<ProviderService> reference = new ReferenceConfig<ProviderService>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
//        reference.setApplication(application);
//        reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
//        reference.setInterface(ProviderService.class);
//        reference.setVersion("1.0.0");
//
//        // 和本地bean一样使用xxxService
//        ProviderService providerService = reference.get();// 注意：此代理对象内部封装了所有通讯细节，对象较重，请缓存复用
//        return providerService;
//    }
}
