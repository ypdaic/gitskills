package com.daiyanping.cms.spring.aop.aspectj;

import com.daiyanping.cms.spring.annotation.TargetSource;
import com.daiyanping.cms.spring.datasource.DynamicDataSourceHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(-1)
public class AspectDs {

    @Around(value = "@annotation(targetSource)",argNames = "joinPoint,targetSource")
    public Object xx(ProceedingJoinPoint joinPoint, TargetSource targetSource) {

        System.out.println("========AspectDs.xx");
        String value = targetSource.value();

        if(value != null && !"".equals(value)) {
            System.out.println("数据源切换" + value);
            DynamicDataSourceHolder.getLocal().set(value);
        } else {
            DynamicDataSourceHolder.getLocal().set("ds1");
        }
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return proceed;
    }
}
