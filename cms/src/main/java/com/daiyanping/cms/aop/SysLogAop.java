package com.daiyanping.cms.aop;

import com.alibaba.fastjson.JSONObject;
import com.daiyanping.cms.util.BeanUtil;
import com.daiyanping.cms.util.BodyReaderHttpServletRequestWrapper;
import com.daiyanping.cms.util.CusAccessObjectUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @description：AOP 系统日志
 */
@Order(1)
@Aspect
@Component
public class SysLogAop {
    private static Logger LOGGER = LoggerFactory.getLogger(SysLogAop.class);

    @Value("${server.servlet.context-path}")
    String basePath;


    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void cutController() {
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void cutRestController() {
    }

    @Around("cutController() || cutRestController()")  //满足1种就会进
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {
        LOGGER.debug("进入SysLogAop 。。。。。。");
        String strMethodName = point.getSignature().getName();
        String strClassName = point.getTarget().getClass().getName();
        Object[] params = point.getArgs();
        StringBuilder strMessage = new StringBuilder();
        String clientIp = "";
        String requestPath = "";
        Map<String, String> reqParamMap = new HashMap<>();

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(request);

        //检测所有请求，暂时屏蔽该处判断
        Map<String, Object> urlParamMap = null;
        if (params != null && params.length > 0) {
            if (requestWrapper.getMethod().equalsIgnoreCase(RequestMethod.HEAD.name())) {
                return null;
            }
            if (strMethodName.equals("error")) {
                LOGGER.debug("404请求");
                return null;
            }
            clientIp = CusAccessObjectUtil.getIpAddress(requestWrapper);// 获取请求ip
            requestPath = requestWrapper.getRequestURI();// 获取请求地址
            //把basePath在requestPath中去掉
//            requestPath = requestPath.replace(basePath, "");

            // 通过getParameterName获得的请求参数转换为map
            urlParamMap = BeanUtil.getParameterByNames(request);
            reqParamMap.put("urlParam", JSONObject.toJSONString(urlParamMap));
            String bodyStr = requestWrapper.getBodyString(request);
            // 获取输入参数
            reqParamMap.put("body", bodyStr);
            strMessage.append("[类名]:").append(strClassName);
            strMessage.append("[方法]:").append(strMethodName);
            strMessage.append("[请求IP]:").append(clientIp);
            strMessage.append("[请求路径]:").append(requestPath);
            //有单独的"请求参数" 字符
//            strMessage.append("[请求参数]:").append(reqParamMap);
        }
        long beg = System.currentTimeMillis();

        LOGGER.debug(strMessage.toString());
        LOGGER.debug("[请求参数]:{}", reqParamMap);
        Object result = point.proceed();
        String resultStr = JSONObject.toJSONString(result);
        LOGGER.debug("[请求处理时长]:{}, [返回结果]:{}", (System.currentTimeMillis() - beg), resultStr);
        //保存log
        return result;
    }
}
