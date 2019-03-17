package com.daiyanping.cms;

import lombok.Data;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.stereotype.Component;

@Data
@Component
//实现BeanClassLoaderAware接口，用于获取该bean的类装载器
public class BeanClassLoaderAwareTest implements BeanClassLoaderAware {

	private ClassLoader classLoader;

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
}
