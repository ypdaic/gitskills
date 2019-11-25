package com.daiyanping.cms.springioc.importBeanDefinitionRegistrar;

import com.daiyanping.cms.springioc.beanFactoryPostProcessor.MyBeanFactoryPostProcessor;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan("com.daiyanping.cms.springioc.importBeanDefinitionRegistrar")
@Import(MyImportBeanDefinitionRegistrar.class)
public class ImportBeanDefinitionRegistrarConfig {

    /**
     * 以bean注解的形式导入无效
     * @return
     */
    @Bean
    public ImportBeanDefinitionRegistrar importBeanDefinitionRegistrar() {
        return new MyImportBeanDefinitionRegistrar();
    }
}
