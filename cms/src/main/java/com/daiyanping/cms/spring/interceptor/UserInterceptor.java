package com.daiyanping.cms.spring.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("======UserInterceptor用户权限校验=========");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("========UserInterceptor修改modelAndView======");
//        HttpSession session = request.getSession();
//        if(modelAndView != null && session != null) {
//            String modifyViewName = modelAndView.getViewName() + "_" + session.getAttribute("language");
//            modelAndView.setViewName(modifyViewName);
//        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("========UserInterceptor资源释放======");
    }
}
