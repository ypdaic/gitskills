package com.daiyanping.cms.mymvc;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;

/**
 * @ClassName MyTomcat
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-08-06
 * @Version 0.1
 */
public class MyTomcat {

    static  final String DEFAULT_PROTOCOL = "org.apache.coyote.http11.Http11NioProtocol";

    public static void main(String[] args) {

        Tomcat tomcat = new Tomcat();
        tomcat.setHostname("localhost");
        tomcat.setBaseDir(".");

        Connector connector = new Connector(DEFAULT_PROTOCOL);//设置协议，默认就是这个协议connector.setURIEncoding(“UTF-8”);//设置编码
        connector.setPort(8089);//设置端口
        tomcat.getService().addConnector(connector);

        Context ctx = tomcat.addContext("mymvc",null);//网络访问路径

//        Wrapper dispatchServlet = tomcat.addServlet(ctx, "dispatchServlet", DispatchServlet.class.getName());//配置servlet
        Wrapper dispatchServlet = tomcat.addServlet(ctx, "dispatchServlet", new DispatchServlet());

        ctx.addServletMappingDecoded("/", "dispatchServlet");//配置servlet映射路径

        StandardServer server = (StandardServer)tomcat.getServer();//添加监听器，不知何用

        Tomcat.FixContextListener fixContextListener = new Tomcat.FixContextListener();

        server.addLifecycleListener(fixContextListener);

//        ctx.setParentClassLoader(Thread.currentThread().getContextClassLoader());
//
//        WebappLoader loader = new WebappLoader(ctx.getParentClassLoader());
//        loader.setLoaderClass(TomcatEmbeddedWebappClassLoader.class.getName());
//        loader.setDelegate(true);
//        ctx.setLoader(loader);

//        Wrapper dispatchServlet = tomcat.addServlet("/", "dispatchServlet", DispatchServlet.class.getName());
//        dispatchServlet.addMapping("/mymvc");
        dispatchServlet.setLoadOnStartup(-1);
        dispatchServlet.setAsyncSupported(true);
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }

        tomcat.getServer().await();

    }

}
