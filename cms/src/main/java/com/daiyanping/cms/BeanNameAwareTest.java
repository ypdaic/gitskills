package com.daiyanping.cms;

import lombok.Data;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;

@Data
@Component
//实现BeanNameAware接口后，可以获取该bean在spring容器中的名称
public class BeanNameAwareTest implements BeanNameAware {

	private String name;

	@Override
	public void setBeanName(String name) {
		this.name = name;
	}
}
