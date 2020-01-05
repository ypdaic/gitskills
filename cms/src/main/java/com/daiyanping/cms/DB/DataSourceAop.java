package com.daiyanping.cms.DB;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 往期课程咨询芊芊老师  QQ：2130753077 VIP课程咨询 依娜老师  QQ：2133576719
 * 类说明：定义了aop，在进行数据库操作选择主库还是从库
 */
@Aspect
@Component
public class DataSourceAop {
    /*从库的切点,没有标注Master注解，并且方法名为select和get开头的方法，走从库*/
    @Pointcut("!@annotation(com.daiyanping.cms.annotation.Master) " +
            "&& (execution(* com.daiyanping.cms.service..*.select*(..)) " +
            "|| execution(* com.daiyanping.cms.service..*.get*(..))" +
            "|| execution(* com.daiyanping.cms.service..*.find*(..))" +
            "|| execution(* com.daiyanping.cms.service..*.query*(..)))")
    public void slavePointcut() {

    }

    /*主库的切点,或者标注了Master注解或者方法名为insert、update等开头的方法，走主库*/
    @Pointcut("@annotation(com.daiyanping.cms.annotation.Master) " +
            "|| execution(* com.daiyanping.cms.service..*.insert*(..)) " +
            "|| execution(* com.daiyanping.cms.service..*.add*(..)) " +
            "|| execution(* com.daiyanping.cms.service..*.update*(..)) " +
            "|| execution(* com.daiyanping.cms.service..*.edit*(..)) " +
            "|| execution(* com.daiyanping.cms.service..*.delete*(..)) " +
            "|| execution(* com.daiyanping.cms.service..*.remove*(..))")
    public void masterPointcut() {
    }

    @Before("slavePointcut()")
    public void slave() {
        DBThreadLocal.slave();
    }

    @Before("masterPointcut()")
    public void master() {
        DBThreadLocal.master();
    }
}
