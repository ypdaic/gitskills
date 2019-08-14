package com.daiyanping.cms.springmvc;

import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName BeanNameUrlControllerForHttpRequestHandler
 * @Description 在spring 3.1以前需通过如下方式实现Controller层
 * @Author daiyanping
 * @Date 2019-08-14
 * @Version 0.1
 */
@Component("/bean-name/test2")
public class BeanNameUrlControllerForHttpRequestHandler implements HttpRequestHandler {


    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println(getClass().getName());
        response.getWriter().flush();
    }
}
