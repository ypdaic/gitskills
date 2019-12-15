package com.daiyanping.cms.spring.targetSource;

import org.springframework.aop.target.AbstractBeanFactoryBasedTargetSource;

public class MyTargetSource extends AbstractBeanFactoryBasedTargetSource {
    @Override
    public Object getTarget() throws Exception {
        return getBeanFactory().getBean(getTargetBeanName());
    }
}
