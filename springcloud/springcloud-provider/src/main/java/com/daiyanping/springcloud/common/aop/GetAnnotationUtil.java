package com.daiyanping.springcloud.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @ClassName GetAnnotationUtil
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-11-01
 * @Version 0.1
 */
public class GetAnnotationUtil {

    public static <A extends Annotation> A getAnnotation(ProceedingJoinPoint joinPoint, Class<A> clazz) {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return AnnotationUtils.findAnnotation(method, clazz);
    }

    public static <A extends Annotation> A getAnnotation(JoinPoint joinPoint, Class<A> clazz) {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return AnnotationUtils.findAnnotation(method, clazz);
    }


    public static <A extends Annotation> A getAnnotationWithMethod(JoinPoint joinPoint, Class<A> clazz) {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return AnnotationUtils.findAnnotation(method, clazz);
    }

    public static <A extends Annotation> A getAnnotationWithClass(JoinPoint joinPoint, Class<A> clazz) {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return AnnotationUtils.findAnnotation(method.getDeclaringClass(), clazz);
    }
}
