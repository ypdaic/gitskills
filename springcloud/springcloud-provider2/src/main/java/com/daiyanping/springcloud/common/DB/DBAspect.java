package com.daiyanping.springcloud.common.DB;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @ClassName DBAspect
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-03
 * @Version 0.1
 */
@Aspect
// @Order注解用于控制bean的加载顺序，数字越小，级别越高，这里给这个注解，用于保证在spring事物管理获取连接时已经切换数据源了
@Order(0)
@Component
public class DBAspect {

    private Logger logger = LoggerFactory.getLogger(DBAspect.class);

    /**
     * 如果类中的某个属性使用了ajc$开头，则在这个类上使用@Aspect注解时无效的
     */
//    private String ajc$Test = "";

    // @Pointcut  定义切入点
    //@annotation  只支持在方法上使用了指定注解，才能被拦截
    @Pointcut("@annotation(com.daiyanping.springcloud.common.DB.DB)")
    public void daoAspect() {

    }

    //引用切入点
//    @After("daoAspect()")
    //匿名切入点 表示拦截com.daiyanping.cms.service.impl包及其子包任何方法
    @After("execution(* com.daiyanping.springcloud.provider.service.impl..*.*(..))")
    public void after(JoinPoint joinPoint) {
        DBThreadLocal.cleanDBType();
    }

    //    @Before("daoAspect()")
    @Before("execution(* com.daiyanping.springcloud.provider.service.impl..*.*(..))")
        public void before(JoinPoint joinPoint) {
        //获取被代理对象 这里是ServiceImpl的实例
        Object target = joinPoint.getTarget();
        //获取被对象的class
        Class<?> aClass = target.getClass();
        //获取类上的指定注解
        DB db = (DB) aClass.getAnnotation(DB.class);
        if (db != null) {

            if (db.value().getDbName().equals(DBTypeEnum.DB1.getDbName())) {
                logger.info("开始切换数据源：{}", DBTypeEnum.DB1.getDbName());
                DBThreadLocal.setDBType(DBTypeEnum.DB1);
            }
            if (db.value().getDbName().equals(DBTypeEnum.DB2.getDbName())) {
                logger.info("开始切换数据源：{}", DBTypeEnum.DB2.getDbName());
                DBThreadLocal.setDBType(DBTypeEnum.DB2);
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

                if (annotation.value().getDbName().equals(DBTypeEnum.DB1.getDbName())) {
                    logger.info("开始切换数据源：{}", DBTypeEnum.DB1.getDbName());
                    DBThreadLocal.setDBType(DBTypeEnum.DB1);
                }
                if (annotation.value().getDbName().equals(DBTypeEnum.DB2.getDbName())) {
                    logger.info("开始切换数据源：{}", DBTypeEnum.DB2.getDbName());
                    DBThreadLocal.setDBType(DBTypeEnum.DB2);
                }
            }
        } catch (NoSuchMethodException e) {
            logger.error("数据源切换失败！", e);
        }
    }

}