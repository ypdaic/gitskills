package com.daiyanping.cms;

import com.daiyanping.cms.mapper.UserMapper;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @ClassName ImportBeanDefinitionRegistrarTest2
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-03-29
 * @Version 0.1
 */
public class ImportBeanDefinitionRegistrarTest2 implements ImportBeanDefinitionRegistrar {

    /**
     * BeanDefinition相当于spring的bean标签
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //直接使用BeanDefinitionBuilder构建beanDefinition
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(UserMapper.class);
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();

        //设置该属性后，就可以自动注入属性了,如果被注入的属性没有先被注入到spring容器中，那么该属性就不会注入了
        beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        //往spring容器中注入该bean，指定bean的名称
        registry.registerBeanDefinition("test", beanDefinition);
    }
}
