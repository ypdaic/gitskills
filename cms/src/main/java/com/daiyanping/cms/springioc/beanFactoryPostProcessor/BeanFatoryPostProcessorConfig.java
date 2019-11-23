package com.daiyanping.cms.springioc.beanFactoryPostProcessor;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MyBeanFactoryPostProcessor.class)
@ComponentScan("com.daiyanping.cms.springioc.beanFactoryPostProcessor")
public class BeanFatoryPostProcessorConfig {
}
