package com.daiyanping.cms.aop;

import com.alibaba.fastjson.JSONObject;
import com.daiyanping.cms.annotation.Resubmit;
import com.daiyanping.cms.vo.BaseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName ResubmitAop
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-10-12
 * @Version 0.1
 */
@Aspect
@Slf4j
public class ResubmitAop {

    private final Map<String, Object> lockMap = new ConcurrentHashMap();

    private static final Object PRESENT = new Object();

    //使用guava
//    private Cache<String, Object> cache = CacheBuilder.newBuilder().expireAfterWrite(20,  TimeUnit.SECONDS)
//                                                                   .build();

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Pointcut("@annotation(com.daiyanping.cms.annotation.Resubmit)")
    public void reSubmitPointcut() {

    }

    @Around(value = "reSubmitPointcut()")
    public Object reSubmitCheck(ProceedingJoinPoint joinPoint) {
        String operationKey = getOperationKey(joinPoint);
        boolean success = tryLock(operationKey);
        if (!success) {
            return "重复提交";
        } else {
            Object result = null;
            try {
                result = joinPoint.proceed();
            } catch (Throwable throwable) {
                log.error("业务执行失败!", throwable);
                throw new RuntimeException("业务执行失败!", throwable);
            } finally {
                unLock(joinPoint, operationKey);
            }

            return result;
        }
    }

    private String getOperationKey(ProceedingJoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Object[] arguments = joinPoint.getArgs();
        Resubmit resubmit = AnnotationUtils.findAnnotation(targetMethod, Resubmit.class);
        String spel = resubmit.key();
        BaseDto parse = SpelExpressionUtil.parse(spel, targetMethod, arguments, BaseDto.class);
        return new String(DigestUtils.sha256(JSONObject.toJSONString(parse).getBytes()));
    }

    private boolean tryLock(String operationKey){
        return Objects.isNull(lockMap.putIfAbsent(operationKey, PRESENT));
    }

    private void unLock(ProceedingJoinPoint joinPoint, String operationKey){
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Object[] arguments = joinPoint.getArgs();
        Resubmit resubmit = AnnotationUtils.findAnnotation(targetMethod, Resubmit.class);
        long delaySeconds = resubmit.resuInterval();
        taskScheduler.schedule(() -> {
            lockMap.remove(operationKey);
        }, new Date(System.currentTimeMillis() + delaySeconds * 1000l));
    }


}
