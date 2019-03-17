package com.daiyanping.cms;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

//BeanFactoryAware 接口，当某个类实现该接口时，Spring容器会将BeanFactory注入到该类中
//这也是我们获取Spring容器BeanFactoryAware的一种方式
@Component
public class BeanFactoryAwareTest implements BeanFactoryAware {

	private BeanFactory beanFactory;
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}
}
