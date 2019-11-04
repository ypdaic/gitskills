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
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ResubmitAop
 * @Description TODO 请求提交时间间隔aop
 * @Author daiyanping
 * @Date 2019-10-12
 * @Version 0.1
 */
@Order(-2)
@Aspect
@Slf4j
public class ResubmitAopWithRedis {

    @Autowired
    RedisTemplate redisTemplate;

    @Pointcut("@annotation(com.daiyanping.cms.annotation.Resubmit)")
    public void reSubmitPointcut() {

    }

    /**
     * 提交时间间隔检查
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "reSubmitPointcut()")
    public Object reSubmitCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        String operationKey = getOperationKey(joinPoint);
        long resuInterval = getResuInterval(joinPoint);
        boolean success = trySubmit(operationKey, resuInterval);
        if (!success) {
            return null;
        } else {
            return joinPoint.proceed();
        }
    }

    /**
     * 获取缓存key
     * @param joinPoint
     * @return
     */
    private String getOperationKey(ProceedingJoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Object[] arguments = joinPoint.getArgs();
        Resubmit resubmit = AnnotationUtils.findAnnotation(targetMethod, Resubmit.class);
        String spel = resubmit.key();
        BaseDto parse = SpelExpressionUtil.parse(spel, targetMethod, arguments, BaseDto.class);
        return DigestUtils.md5Hex(JSONObject.toJSONString(parse).getBytes());
    }

    /**
     * 获取提价间隔
     * @param joinPoint
     * @return
     */
    private long getResuInterval(ProceedingJoinPoint joinPoint){
        Resubmit resubmit = GetAnnotationUtil.getAnnotation(joinPoint, Resubmit.class);
        return resubmit.resuInterval();
    }

    /**
     * 尝试提交
     * @param operationKey
     * @param resuInterval
     * @return
     */
    private boolean trySubmit(String operationKey, long resuInterval){
        BoundValueOperations boundValueOperations = redisTemplate.boundValueOps(operationKey);
        return boundValueOperations.setIfAbsent(new byte[0], resuInterval, TimeUnit.SECONDS);
    }



}
