package com.daiyanping.cms;

import com.daiyanping.cms.mapper.UserMapper;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AssignableTypeFilter;

/**
 * @ClassName ImportBeanDefinitionRegistrarTest3
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-03-29
 * @Version 0.1
 */
public class ImportBeanDefinitionRegistrarTest3 implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner = new ClassPathBeanDefinitionScanner(registry);
        //AssignableTypeFilter  类 类型过滤器
        classPathBeanDefinitionScanner.addIncludeFilter(new AssignableTypeFilter(UserMapper.class));
        classPathBeanDefinitionScanner.scan("com.daiyanping.cms.mapper");

    }
}
