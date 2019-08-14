package com.daiyanping.cms.springmvc;

import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
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
@Component("/bean-name/test3")
public class BeanNameUrlControllerForServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(getClass().getName());
        resp.getWriter().flush();
    }
}
