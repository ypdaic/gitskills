package com.daiyanping.cms.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.daiyanping.cms.dubbo.provider.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

import javax.annotation.PostConstruct;

/**
 * @ClassName ProviderConfig
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-06-24
 * @Version 0.1
 */
@Configuration
// xml形式进行dubbo配置
//@ImportResource("classpath:dubbo/provider.xml")
@ComponentScan("com.daiyanping.cms.dubbo.provider")
@EnableDubbo(scanBasePackages = "com.daiyanping.cms.dubbo.provider")
@PropertySource("classpath:/dubbo/provider.properties")
public class ProviderConfig {

//    @Autowired
//    ProviderService providerService;
//
//    /**
//     * javaconifg 的形式配置dubbo
//     * API使用范围说明：API 仅用于 OpenAPI, ESB, Test, Mock 等系统集成，普通服务提供方或消费方，请采用XML 配置方式使用 Dubbo ↩︎
//     */
//    @PostConstruct
//    public void provider() {
//        // 当前应用配置
//        ApplicationConfig application = new ApplicationConfig();
//        application.setName("demo-provider");
//
//        // 连接注册中心配置
//        RegistryConfig registry = new RegistryConfig();
//        registry.setAddress("zookeeper://127.0.0.1:2181");
//        registry.setUsername("aaa");
//        registry.setPassword("bbb");
//
//        // 服务提供者协议配置
//        ProtocolConfig protocol = new ProtocolConfig();
//        protocol.setName("dubbo");
//        protocol.setPort(12345);
//        protocol.setThreads(200);
//
//        // 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口
//
//        // 服务提供者暴露服务配置
//        ServiceConfig<ProviderService> service = new ServiceConfig<ProviderService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
//        service.setApplication(application);
//        service.setRegistry(registry); // 多个注册中心可以用setRegistries()
//        service.setProtocol(protocol); // 多个协议可以用setProtocols()
//        service.setInterface(ProviderService.class);
//        service.setRef(providerService);
//        service.setVersion("1.0.0");
//
//        // 暴露及注册服务
//        service.export();
//    }
}
