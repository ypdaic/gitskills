package com.daiyanping.cms.springmvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.handler.SimpleServletHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @ClassName MyDelegatingWebMvcConfiguration
 * @Description 自己调整HandlerMapping加载顺序，得去掉@EnableWebMvc注解
 * @Author daiyanping
 * @Date 2019-08-14
 * @Version 0.1
 */
@Configuration
public class MyDelegatingWebMvcConfiguration extends DelegatingWebMvcConfiguration {

    /**
     * 调整加载顺序先于RequestMappingHandlerMapping加载
     * @return
     */
    @Override
    public BeanNameUrlHandlerMapping beanNameHandlerMapping() {
        BeanNameUrlHandlerMapping beanNameUrlHandlerMapping = super.beanNameHandlerMapping();
        beanNameUrlHandlerMapping.setOrder(0);
//        beanNameUrlHandlerMapping.
        return beanNameUrlHandlerMapping;
    }

    @Override
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping requestMappingHandlerMapping = super.requestMappingHandlerMapping();
        requestMappingHandlerMapping.setOrder(2);
        return requestMappingHandlerMapping;
    }

    /**
     * SimpleServletHandlerAdapter 默认不是自动注入的，需要自己注入
     * @return
     */
    @Bean
    public SimpleServletHandlerAdapter simpleServletHandlerAdapter() {
        SimpleServletHandlerAdapter simpleServletHandlerAdapter = new SimpleServletHandlerAdapter();
        return simpleServletHandlerAdapter;
    }



}
