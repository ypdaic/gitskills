package com.daiyanping.cms.springioc.beanFactoryPostProcessor;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
//@Import(MyBeanFactoryPostProcessor.class)
@ComponentScan("com.daiyanping.cms.springioc.beanFactoryPostProcessor")
public class BeanFatoryPostProcessorConfig {

    /**
     * 以bean注解的形式可以导入，也可以使用Import的形式导入
     * @return
     */
    @Bean
    BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return new MyBeanFactoryPostProcessor();
    }
}
