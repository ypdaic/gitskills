package com.daiyanping.cms.DB;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @ClassName DBAspect
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-03
 * @Version 0.1
 */
@Aspect
@Component
public class DBAspect {

    @Autowired
    private DBThreadLocal dbThreadLocal;

    // @Pointcut  定义切入点
    //@annotation  只支持在方法上使用了指定注解，才能被拦截
    @Pointcut("@annotation(com.daiyanping.cms.DB.DB)")
    public void daoAspect() {

    }

    //引用切入点
//    @After("daoAspect()")
    //匿名切入点 表示拦截com.daiyanping.cms.service.impl包及其子包任何方法
    @After("execution(* com.daiyanping.cms.service.impl..*.*(..))")
    public void after(JoinPoint joinPoint) {
        dbThreadLocal.cleanDBType();
    }

    //    @Before("daoAspect()")
    @Before("execution(* com.daiyanping.cms.service.impl..*.*(..))")
    public void before(JoinPoint joinPoint) {
        //获取被代理对象 这里是ServiceImpl的实例
        Object target = joinPoint.getTarget();
        //获取被对象的class
        Class<?> aClass = target.getClass();
        //获取类上的指定注解
        DB db = (DB) aClass.getAnnotation(DB.class);
        if (db != null) {

            if (db.DB().getDbName().equals(DBTypeEnum.TEST.getDbName())) {
                System.out.println("开始切换数据源：" + DBTypeEnum.TEST.getDbName());
                dbThreadLocal.setDBType(DBTypeEnum.TEST);
            }
            if (db.DB().getDbName().equals(DBTypeEnum.TEST2.getDbName())) {
                System.out.println("开始切换数据源：" + DBTypeEnum.TEST2.getDbName());
                dbThreadLocal.setDBType(DBTypeEnum.TEST2);
            }
        }

        String name = joinPoint.getSignature().getName();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class[] parameterTypes = signature.getParameterTypes();
        try {
            // 获取被调用的方法
            Method method = aClass.getMethod(name, parameterTypes);
            // 获取方法上的指定注解
            DB annotation = method.getAnnotation(DB.class);
            if (annotation != null) {

                if (annotation.DB().getDbName().equals(DBTypeEnum.TEST.getDbName())) {
                    System.out.println("开始切换数据源：" + DBTypeEnum.TEST.getDbName());
                    dbThreadLocal.setDBType(DBTypeEnum.TEST);
                }
                if (annotation.DB().getDbName().equals(DBTypeEnum.TEST2.getDbName())) {
                    System.out.println("开始切换数据源：" + DBTypeEnum.TEST.getDbName());
                    dbThreadLocal.setDBType(DBTypeEnum.TEST2);
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

}