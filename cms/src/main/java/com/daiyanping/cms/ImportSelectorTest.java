package com.daiyanping.cms;

import com.daiyanping.cms.entity.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.Set;

/**
 * @ClassName ImportSelectorTest
 * @Description 验证ImportSelector接口的使用
 * @Author daiyanping
 * @Date 2019-03-27
 * @Version 0.1
 */
public class ImportSelectorTest implements ImportSelector, BeanFactoryAware {

    private BeanFactory beanFactory;

    //这里的annotationMetadata获取的是被引入对象上的注解信息
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        //getAnnotationTypes获取配置类的所有注解的完全限定名
        annotationMetadata.getAnnotationTypes().forEach(System.out::println);
        //getAnnotatedMethods根据注解的完全限定名匹配配置类中使用该注解的方法及信息
        Set<MethodMetadata> annotatedMethods = annotationMetadata.getAnnotatedMethods(Bean.class.getName());
        annotatedMethods.forEach(methodMetadata -> {
            String methodName = methodMetadata.getMethodName();
            System.out.println(methodName);
        });
        //获取配置类上指定注解的元注解，递归查找，此处查找的是Configuration的所有注解
        Set<String> metaAnnotationTypes = annotationMetadata.getMetaAnnotationTypes(Configuration.class.getName());

        //获取配置类上指定注解所拥有的属性及值
        MultiValueMap<String, Object> allAnnotationAttributes = annotationMetadata.getAllAnnotationAttributes(EnableImportSelectorTest.class.getName());

        //获取配置类上指定注解所拥有的属性及值
        Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(EnableImportSelectorTest.class.getName());

        System.out.println(beanFactory);
        return new String[]{ImportBeanDefinitionRegistrarTest.class.getName(), ConfigurationTest.class.getName()};
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
