package com.daiyanping.cms.mymvc;

import com.daiyanping.cms.mymvc.annotation.MyComponentScan;
import com.daiyanping.cms.mymvc.annotation.MyController;
import com.daiyanping.cms.mymvc.annotation.MyRequestMapping;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName DispatchServlet
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-08-06
 * @Version 0.1
 */
@MyComponentScan({"com.daiyanping.cms.mymvc"})
public class DispatchServlet extends HttpServlet {

    private Map<String, Object> controller = new HashMap<String, Object>();

    @Override
    public void init() throws ServletException {
        ClassLoader classLoader = getClass().getClassLoader();
        Class<? extends DispatchServlet> clazz = this.getClass();
        MyComponentScan annotation = clazz.getAnnotation(MyComponentScan.class);
        String[] strings = annotation.value();
        if (strings.length >= 0) {
            for (String basePackage : strings) {

                URL resource = classLoader.getResource(basePackage.replace(".", "/"));
                String filePath = resource.getFile();
                doScan(filePath, basePackage);

            }
        }
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    private void doScan(String filePath, String packageName) {
        File file = new File(filePath);
        if (file.isDirectory()) {

            File[] files = file.listFiles();
            if (files.length >= 0) {
                for (File file2 : files) {
                    String packageName2 = "";
                    if (file2.isDirectory()) {
                        packageName2 = file2.getName();
                    }
                    if (StringUtils.hasLength(packageName2)) {

                        doScan(file2.getAbsolutePath(), packageName + "." + packageName2);
                    } else {
                        doScan(file2.getAbsolutePath(), packageName);
                    }

                }
            }
        } else {
            try {
                Class<?> clazz = getClass().getClassLoader().loadClass(packageName + "." + new File(filePath).getName().replace(".class", ""));
                MyController myController = clazz.getAnnotation(MyController.class);
                if (myController != null) {
                    MyRequestMapping myRequestMapping = clazz.getAnnotation(MyRequestMapping.class);
                    String value = myRequestMapping.value();
                    controller.put(value, clazz.getConstructor(null).newInstance());
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }


    }

}
