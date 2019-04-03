package com.daiyanping.cms.DB;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;

/**
 * @ClassName DBAspect
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-03
 * @Version 0.1
 */
@Component
@Aspect
public class DBAspect {

    @Pointcut("@annotation(com.wangzhi.springboot.aop.test.Action)")
    public void daoAspect() {
    }

    @After("daoAspect()")
    public void after(JoinPoint joinPoint) {
        System.out.println("方法执行后拦截处理");
        System.out.println("目标方法名为:" + joinPoint.getSignature().getName());
        System.out.println("目标方法所属类的简单类名:" +        joinPoint.getSignature().getDeclaringType().getSimpleName());
        System.out.println("目标方法所属类的类名:" + joinPoint.getSignature().getDeclaringTypeName());
        System.out.println("目标方法声明类型:" + Modifier.toString(joinPoint.getSignature().getModifiers()));
//		//获取传入目标方法的参数
//		Object[] args = joinPoint.getArgs();
//		for (int i = 0; i < args.length; i++) {
//			System.out.println("第" + (i+1) + "个参数为:" + args[i]);
//		}
//		joinPoint.getTarget()与joinPoint.getThis()
//		System.out.println("被代理的对象:" + joinPoint.getTarget());
//		System.out.println("代理对象自己:" + joinPoint.getThis());
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        System.out.println(name);

    }

    @Before("daoAspect()")
    public void before(JoinPoint joinPoint) {
        System.out.println("方法执行前拦截处理");
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        System.out.println(name);

    }
}
