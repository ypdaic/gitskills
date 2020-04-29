package com.daiyanping.springcloud.common.aop;

import com.daiyanping.springcloud.common.annotation.PrintRequestTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * 请求时长aop
 * @author daiyanping
 */
@Order(-1)
@Aspect
@Component
@Slf4j
public class PrintRequestTimeAop {

    @Pointcut("@annotation(com.daiyanping.springcloud.common.annotation.PrintRequestTime)")
    public void methodPointcut() {
    }

    @Pointcut("@within(com.daiyanping.springcloud.common.annotation.PrintRequestTime)")
    public void classPointcut() {
    }

    /**
     * 打印请求处理时长
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "methodPointcut() || classPointcut()")
    public Object printRequestTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();

        if (!Modifier.isPublic(signature.getModifiers())) {
            return joinPoint.proceed();

        } else {
            long printErrorTime = getPrintErrorTime(joinPoint);
            long printInfoTime = getPrintInfoTime(joinPoint);
            boolean error = isError(joinPoint);
            long startTime = System.currentTimeMillis();
            try {
                return joinPoint.proceed();
            } finally {
                long endTime = System.currentTimeMillis();
                long runTime = endTime - startTime;

                String sign = signature.toString();
                if (error) {
                    if (runTime > printErrorTime) {

                        log.error("执行方法: {}, 执行时长: {}ms", sign, runTime);
                    }
                } else {
                    if (runTime > printInfoTime) {

                        log.info("执行方法: {}, 执行时长: {}ms", sign, runTime);
                    }
                }

            }
        }

    }

    private long getPrintErrorTime(ProceedingJoinPoint joinPoint) {
        PrintRequestTime annotationMethod = GetAnnotationUtil.getAnnotation(joinPoint, PrintRequestTime.class);
        PrintRequestTime annotationClass = GetAnnotationUtil.getAnnotationWithClass(joinPoint, PrintRequestTime.class);
        if (Objects.nonNull(annotationClass) && Objects.isNull(annotationMethod)) {
            return annotationClass.printError();
        }
        return annotationMethod.printError();

    }

    private long getPrintInfoTime(ProceedingJoinPoint joinPoint) {
        PrintRequestTime annotationMethod = GetAnnotationUtil.getAnnotation(joinPoint, PrintRequestTime.class);
        PrintRequestTime annotationClass = GetAnnotationUtil.getAnnotationWithClass(joinPoint, PrintRequestTime.class);
        if (Objects.nonNull(annotationClass) && Objects.isNull(annotationMethod)) {
            return annotationClass.printInfo();
        }
        return annotationMethod.printInfo();
    }

    private boolean isError(ProceedingJoinPoint joinPoint) {
        PrintRequestTime annotationMethod = GetAnnotationUtil.getAnnotation(joinPoint, PrintRequestTime.class);
        PrintRequestTime annotationClass = GetAnnotationUtil.getAnnotationWithClass(joinPoint, PrintRequestTime.class);
        if (Objects.nonNull(annotationClass) && Objects.isNull(annotationMethod)) {
            return annotationClass.isError();
        }
        return annotationMethod.isError();
    }

}
