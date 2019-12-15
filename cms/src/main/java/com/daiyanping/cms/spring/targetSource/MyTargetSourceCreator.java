package com.daiyanping.cms.spring.targetSource;

import org.springframework.aop.framework.autoproxy.target.AbstractBeanFactoryBasedTargetSourceCreator;
import org.springframework.aop.target.AbstractBeanFactoryBasedTargetSource;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class MyTargetSourceCreator extends AbstractBeanFactoryBasedTargetSourceCreator {
    @Override
    protected AbstractBeanFactoryBasedTargetSource createBeanFactoryBasedTargetSource(Class<?> beanClass, String beanName) {

        if (getBeanFactory() instanceof ConfigurableListableBeanFactory) {
            BeanDefinition definition =
                    ((ConfigurableListableBeanFactory) getBeanFactory()).getBeanDefinition(beanName);
            if(definition.getAttribute("girlProxy")!= null &&
                    Boolean.valueOf(definition.getAttribute("girlProxy").toString())) {
                return new MyTargetSource();
            }
        }

        return null;
    }
}
