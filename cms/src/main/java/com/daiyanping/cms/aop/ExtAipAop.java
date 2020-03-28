package com.daiyanping.cms.aop;

import com.daiyanping.cms.annotation.ExtApi;
import com.daiyanping.cms.enums.ExtApiTypeEnum;
import com.daiyanping.cms.result.Result;
import com.daiyanping.cms.service.IExtApiService;
import com.daiyanping.cms.util.BeanUtil;
import com.daiyanping.cms.util.BodyReaderHttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 对外接口aop
 * @author daiyanping
 *
 */
@Aspect
@Component
@Order(2)
@Slf4j
public class ExtAipAop {
	
	@Value("${server.servlet.context-path}")
    String basePath;

	@Autowired
    IExtApiService extApiService;
	
	@Pointcut("@annotation(com.daiyanping.cms.annotation.ExtApi)")
    public void methodPointcut() {
    }

    @Pointcut("@within(com.daiyanping.cms.annotation.ExtApi)")
    public void classPointcut() {
    }
    
    @Around("methodPointcut() || classPointcut()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {
    	ExtApiTypeEnum type = getType(point);
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
        
        // 获取请求地址
        String requestPath = requestWrapper.getRequestURI();
        //把basePath在requestPath中去掉
        requestPath = requestPath.replace(basePath, "");
        // 通过getParameterName获得的请求参数转换为map
        Map<String, Object> urlParamMap = BeanUtil.getParameterByNames(requestWrapper);
        String bodyStr = requestWrapper.getBodyString(request);
        Map<String, String> reqParamMap = new HashMap<>();
        // 获取输入参数
        reqParamMap.put("body", bodyStr);
        Result<Object> result = extApiService.check(type, urlParamMap, requestPath, reqParamMap);
        if (!result.isSuccess()) {
        	return result;
        }
        
        return point.proceed();
    }
    
    /**
     * 获取外部请求其实路径
     * @param joinPoint
     * @return
     */
    private ExtApiTypeEnum getType(JoinPoint joinPoint){
    	ExtApi extApiMethod = GetAnnotationUtil.getAnnotationWithMethod(joinPoint, ExtApi.class);
    	ExtApi extApiClass = GetAnnotationUtil.getAnnotationWithClass(joinPoint, ExtApi.class);
    	if (Objects.isNull(extApiMethod)) {
    	    return extApiClass.type();
        }
    	return extApiMethod.type();
    }
}
