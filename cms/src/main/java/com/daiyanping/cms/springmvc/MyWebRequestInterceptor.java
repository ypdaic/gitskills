package com.daiyanping.cms.springmvc;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import java.util.Map;

/**
 * @ClassName MyWebRequestInterceptor
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-08-12
 * @Version 0.1
 */
public class MyWebRequestInterceptor implements WebRequestInterceptor {
    @Override
    public void preHandle(WebRequest request) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();
        System.out.println("打印参数" + parameterMap);
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {

    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {

    }
}
