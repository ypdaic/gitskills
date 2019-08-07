package com.daiyanping.cms.mymvc;

import com.daiyanping.cms.mymvc.annotation.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

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
    private Map<String, Object> service = new HashMap<String, Object>();
    private List<Class<?>> classList = new ArrayList<Class<?>>();
    private Map<String, Object> mapping = new HashMap<>();

    @Override
    public void init() throws ServletException {
        try {

            ClassLoader classLoader = getClass().getClassLoader();
            Class<? extends DispatchServlet> clazz = this.getClass();
            MyComponentScan annotation = clazz.getAnnotation(MyComponentScan.class);
            String[] strings = annotation.value();
            if (strings.length > 0) {
                for (String basePackage : strings) {

                    URL resource = classLoader.getResource(basePackage.replace(".", "/"));
                    String filePath = resource.getFile();
                    doScan(filePath, basePackage);

                }
            }
            if (!CollectionUtils.isEmpty(classList)) {

                    getBeans(classList);
                    mergeBean(controller);
                    mergeBean(service);
                    getMapping(controller);

            }
            super.init();
        } catch (Exception e) {

        } finally {

        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String contextPath = getServletContext().getContextPath();
        String mapperPath = requestURI.replaceAll(contextPath, "");
        Map<String, Object> methodAndBean = (Map<String, Object>) mapping.get(mapperPath);
        Method method = (Method) methodAndBean.get("method");
        Object bean = methodAndBean.get("bean");
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Object[] args = new Object[method.getParameterCount()];
        if (parameterAnnotations.length > 0) {

            for (int i = 0; i < parameterAnnotations.length; i++) {
                    Annotation[] parameterAnnotation = parameterAnnotations[i];
                    if (parameterAnnotation.length > 0) {
                        Annotation annotation = parameterAnnotation[0];
                        if (MyRequestParameter.class.isAssignableFrom(annotation.annotationType())) {
                            MyRequestParameter myRequestParameter = (MyRequestParameter) annotation;
                            String value = myRequestParameter.value();
                            String parameter = req.getParameter(value);
                            args[i] = parameter;
                        }

                    }
            }

        }

        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length > 0) {
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                if (HttpServletRequest.class.isAssignableFrom(parameterType)) {
                    args[i] = req;
                }
                if (HttpServletResponse.class.isAssignableFrom(parameterType)) {
                    args[i] = resp;
                }
            }
        }

        try {
            String method1 = req.getMethod();
            System.out.println(method1);
            Object result = method.invoke(bean, args);
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter writer = resp.getWriter();
            writer.println(result);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private void doScan(String filePath, String packageName) {
        File file = new File(filePath);
        if (file.isDirectory()) {

            File[] files = file.listFiles();
            if (files.length > 0) {
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
                classList.add(clazz);


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    private void getBeans (List<Class<?>> classList) throws Exception {
        for (Class<?> clazz : classList) {
            MyController myController = clazz.getAnnotation(MyController.class);
            if (myController != null) {
                MyRequestMapping myRequestMapping = clazz.getAnnotation(MyRequestMapping.class);
                if (myRequestMapping != null) {

                    String value = myRequestMapping.value();
                    controller.put(value, clazz.getConstructor(null).newInstance());
                }
            }

            MyService myService = clazz.getAnnotation(MyService.class);
            if (myService != null) {
                String value = myService.value();
                service.put(value, clazz.getConstructor(null).newInstance());
            }
        }
    }

    private void mergeBean(Map<String, Object> beans) throws Exception{
        Set<Map.Entry<String, Object>> entries = beans.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            Object bean = next.getValue();
            Field[] fields = bean.getClass().getDeclaredFields();
            if (fields.length > 0) {
                for (Field field : fields) {
                    MyQualifier myQualifier = field.getAnnotation(MyQualifier.class);
                    if (myQualifier != null) {
                        String value = myQualifier.value();
                        Object controllerImpl = controller.get(value);
                        if (controllerImpl != null) {
                            field.setAccessible(true);
                            field.set(bean, controllerImpl);
                        }
                        Object serviceImpl = service.get(value);
                        if (serviceImpl != null) {
                            field.setAccessible(true);
                            field.set(bean, serviceImpl);
                        }
                    }
                }
            }

        }

    }

    private void getMapping(Map<String, Object> beans) {
        Set<Map.Entry<String, Object>> entries = beans.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            Object bean = next.getValue();
            String key = next.getKey();
            Method[] declaredMethods = bean.getClass().getDeclaredMethods();
            if (declaredMethods.length > 0) {
                for (Method method : declaredMethods) {
                    MyRequestMapping myRequestMapping = method.getAnnotation(MyRequestMapping.class);
                    if (myRequestMapping != null) {
                        String value = myRequestMapping.value();
                        HashMap<String, Object> methodAndBean = new HashMap<String, Object>();
                        methodAndBean.put("bean", bean);
                        methodAndBean.put("method", method);
                        mapping.put(key + value, methodAndBean);
                    }
                }
            }

        }
    }

}
