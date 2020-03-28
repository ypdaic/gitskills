package com.daiyanping.cms.util;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * bean的工具类,包含的方法有 <br>
 * 1)map与bean 的互转 <br>
 * <p>
 * 11)实例化对象 <br>
 * 12)获得申明的方法和变量  <br>
 * 13)属性赋值
 * <p>
 * javaBean与Map<String,Object>互转利用到了java的内省（Introspector）和反射（reflect）机制。 其思路为：
 * 通过类 Introspector 来获取某个对象的 BeanInfo 信息， 然后通过 BeanInfo
 * 来获取属性的描述器PropertyDescriptor， 再利用属性描述器获取某个属性对应的 getter/setter 方法，
 * 然后通过反射机制来getter和setter。 org.springframework.beans.BeanUtils
 */
public class BeanUtil extends org.springframework.beans.BeanUtils {
    private BeanUtil() {
    }

    /**
     * 通过getParameterName获得的请求参数转换为map
     *
     * @param request
     * @return
     */
    public static Map<String, Object> getParameterByNames(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Enumeration<String> paraNames = request.getParameterNames();
        while (paraNames.hasMoreElements()) {
            String key = paraNames.nextElement();
            String value = request.getParameter(key);
            map.put(key, value);
        }
        return map;
    }


    /**
     * 实例化对象
     *
     * @param clazz 类
     * @return 对象
     */
    public static <T> T newInstance(Class<?> clazz) {
        return (T) instantiate(clazz);
    }

    /**
     * 实例化对象
     *
     * @param clazzStr 类名
     * @return 对象
     */
    public static <T> T newInstance(String clazzStr) {
        try {
            Class<?> clazz = Class.forName(clazzStr);
            return newInstance(clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取Bean的属性
     *
     * @param bean
     * @param propertyName 属性名
     * @return 属性值
     */
    public static Object getProperty(Object bean, String propertyName) {
        PropertyDescriptor pd = getPropertyDescriptor(bean.getClass(), propertyName);
        if (pd == null) {
            throw new RuntimeException(
                    "Could not read property '" + propertyName + "' from bean PropertyDescriptor is null");
        }
        Method readMethod = pd.getReadMethod();
        if (readMethod == null) {
            throw new RuntimeException("Could not read property '" + propertyName + "' from bean readMethod is null");
        }
        if (!readMethod.isAccessible()) {
            readMethod.setAccessible(true);
        }
        try {
            return readMethod.invoke(bean);
        } catch (Throwable ex) {
            throw new RuntimeException("Could not read property '" + propertyName + "' from bean", ex);
        }
    }

    /**
     * 设置Bean属性
     *
     * @param bean
     * @param propertyName 属性名
     * @param value        属性值
     */
    public static void setProperty(Object bean, String propertyName, Object value) {
        PropertyDescriptor pd = getPropertyDescriptor(bean.getClass(), propertyName);
        if (pd == null) {
            throw new RuntimeException(
                    "Could not set property '" + propertyName + "' to bean PropertyDescriptor is null");
        }
        Method writeMethod = pd.getWriteMethod();
        if (writeMethod == null) {
            throw new RuntimeException("Could not set property '" + propertyName + "' to bean writeMethod is null");
        }
        if (!writeMethod.isAccessible()) {
            writeMethod.setAccessible(true);
        }
        try {
            writeMethod.invoke(bean, value);
        } catch (Throwable ex) {
            throw new RuntimeException("Could not set property '" + propertyName + "' to bean", ex);
        }
    }

    /**
     * copy 对象属性到另一个对象（类型，名称相同），不使用Convert即不支持类型转换
     *
     * @param src   原数据
     * @param clazz 目标对象类名
     * @return T
     */
    public static <T> T copy(Object src, Class<T> clazz) {
        BeanCopier copier = BeanCopier.create(src.getClass(), clazz, false);

        T to = newInstance(clazz);
        copier.copy(src, to, null);
        return to;
    }

    /**
     * 拷贝对象
     * spring的 BeanUtils.copyProperties(Object src, Object dist) 也能达到一样的效果
     *
     * @param src  源对象
     * @param dist 需要赋值的对象
     */
    public static void copy(Object src, Object dist) {
        BeanCopier copier = BeanCopier.create(src.getClass(), dist.getClass(), false);

        copier.copy(src, dist, null);
    }

    /**
     * 将对象装成map形式
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> beanToMap(Object obj) {
        //出现加不了obj 之外的key, 重新定义map接收即可
//		return BeanMap.create(obj);
        Map<String, Object> map = new HashMap();
        BeanMap beanMap = BeanMap.create(obj);
        map.putAll(beanMap);
        return map;
    }

    /**
     * 将map 转为 bean
     *
     * @param beanMap
     * @param objClazz
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> beanMap, Class<T> objClazz) {
        T bean = BeanUtil.newInstance(objClazz);
        PropertyDescriptor[] beanPds = getPropertyDescriptors(objClazz);
        for (PropertyDescriptor propDescriptor : beanPds) {
            String propName = propDescriptor.getName();
            // 过滤class属性
            if (propName.equals("class")) {
                continue;
            }
            if (beanMap.containsKey(propName)) {
                Method writeMethod = propDescriptor.getWriteMethod();
                if (null == writeMethod) {
                    continue;
                }
                Object value = beanMap.get(propName);
                if (!writeMethod.isAccessible()) {
                    writeMethod.setAccessible(true);
                }
                try {
                    writeMethod.invoke(bean, value);
                } catch (Throwable e) {
                    throw new RuntimeException("Could not set property '" + propName + "' to bean", e);
                }
            }
        }
        return bean;
    }

}
