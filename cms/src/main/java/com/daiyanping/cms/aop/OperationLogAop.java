package com.daiyanping.cms.aop;


import com.daiyanping.cms.annotation.OperateLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.lang.reflect.Method;

/**
 * @ClassName OperationLogAop
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-16
 * @Version 0.1
 */
@Order(-1)
@Aspect
@Component
@Slf4j
public class OperationLogAop {

    @Autowired
    TransactionTemplate transactionTemplate;

    @Pointcut("@annotation(com.daiyanping.cms.annotation.OperateLog)")
    public void addOperationLogPointcut() {

    }

    @Around(value = "addOperationLogPointcut()")
    public Object addOperationLog(ProceedingJoinPoint joinPoint) {
        String message = getMessage(joinPoint);
//        AccountToken accountInfo = getAccountInfo(joinPoint);
        Object result = transactionTemplate.execute((status) -> {
            Object proceed = null;
            try {
                proceed = joinPoint.proceed();
            } catch (Throwable throwable) {
                log.error("业务操作失败！", throwable);
                throw new RuntimeException("业务操作失败！", throwable);
            }
//            com.sungo.management.server.cms.entity.OperateLog operateLog = new com.sungo.management.server.cms.entity.OperateLog();
//            operateLog.setAccountId(accountInfo.getAccountId());
//            operateLog.setAccountName(accountInfo.getUsername());
//            Date date = new Date();
//            operateLog.setCreateTime(date);
//            operateLog.setUpdateTime(date);
//            operateLog.setEnable(EnableEnum.ENABLE.getValue());
//            operateLog.setOperateDetail(message);
//            operateLog.insert();
            return proceed;
        });

        return result;
    }

    private String getMessage(ProceedingJoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        OperateLog annotation = AnnotationUtils.findAnnotation(method, OperateLog.class);
        return annotation.message();
    }

//    /**
//     * 获取拦截到的请求方法
//     * @param joinPoint 切点
//     * @return AccountToken
//     */
//    private AccountToken getAccountInfo(ProceedingJoinPoint joinPoint) {
//        Signature signature = joinPoint.getSignature();
//        MethodSignature methodSignature = (MethodSignature) signature;
//        Method targetMethod = methodSignature.getMethod();
//        Object target = joinPoint.getTarget();
//        Object[] arguments = joinPoint.getArgs();
//        OperateLog annotation = AnnotationUtils.findAnnotation(targetMethod, OperateLog.class);
//        String spel = annotation.accountInfo();
//        return parse(spel, targetMethod, arguments);
//    }
//
//    public AccountToken parse(String spel, Method method, Object[] args) {
//        //获取被拦截方法参数名列表(使用Spring支持类库)
//        LocalVariableTableParameterNameDiscoverer u =
//                new LocalVariableTableParameterNameDiscoverer();
//        String[] paraNameArr = u.getParameterNames(method);
//        //使用SPEL进行key的解析
//        ExpressionParser parser = new SpelExpressionParser();
//        //SPEL上下文
//        StandardEvaluationContext context = new StandardEvaluationContext();
//        //把方法参数放入SPEL上下文中
//        for (int i = 0; i < paraNameArr.length; i++) {
//            context.setVariable(paraNameArr[i], args[i]);
//        }
//        return parser.parseExpression(spel).getValue(context, AccountToken.class);
//    }
}
