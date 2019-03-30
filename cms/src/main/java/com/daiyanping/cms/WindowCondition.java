package com.daiyanping.cms;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

//满足这个条件才去创建bean,条件一定要判断好，否则会有意向不到的问题
public class WindowCondition implements Condition {
	@Override
	public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
		if (conditionContext.getEnvironment().getProperty("os.name").equals("Windows 7")) {
			return true;
		} else {
			return false;
		}

	}
}
