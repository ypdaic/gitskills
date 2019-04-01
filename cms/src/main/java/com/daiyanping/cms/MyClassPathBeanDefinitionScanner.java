package com.daiyanping.cms;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * @ClassName MyClassPathBeanDefinitionScanner
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-03-29
 * @Version 0.1
 */
public class MyClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

    public MyClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    /**
     * 注册那些使用了某些注解的bean将被加载，那些bean不被加载，这里是使用MyMapper注解的类将被加载
     */
    protected void registerFilters() {
        //AnnotationTypeFilter 注解类型过滤器
        addIncludeFilter(new AnnotationTypeFilter(MyMapper.class));
    }

    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }
}
