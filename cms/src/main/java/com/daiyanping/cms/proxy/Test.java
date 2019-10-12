package com.daiyanping.cms.proxy;

import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Proxy;

/**
 * @ClassName Test
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-10-12
 * @Version 0.1
 */
public class Test {

    public static void main(String[] args) {
//        test2();
//        test3();
//        test4();
        test5();

    }

    /**
     * jdk动态代理，只能代理接口，所以创建处理的实例只能转型为接口
     */
    public static void test1() {
        TestServiceImpl testService = new TestServiceImpl("a", "b");
        TestServerImplJDKProxy testServerImplProxy = new TestServerImplJDKProxy(testService);
        ITestService testService1 = (ITestService) Proxy.newProxyInstance(TestServiceImpl.class.getClassLoader(), new Class[]{ITestService.class}, testServerImplProxy);
    }

    /**
     * cglib动态代理，代理非接口，实际是生成的子类, 所以可以转型为TestServiceImpl
     */
    public static void test2() {
        TestServiceImpl testService = new TestServiceImpl("a", "b");
        TestServerImplCglibProxy testServerImplCglibProxy = new TestServerImplCglibProxy(testService);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(TestServiceImpl.class);
        enhancer.setInterfaces(new Class<?>[] {ITestService.class});
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        enhancer.setCallbackTypes(new Class[]{MethodInterceptor.class});
        enhancer.setCallback(testServerImplCglibProxy);
        TestServiceImpl o = (TestServiceImpl) enhancer.create();
        o.say();


    }

    /**
     * 将jdk动态代理生成的类保存在硬盘上,生成的代理类如下
     * //
     * // Source code recreated from a .class file by IntelliJ IDEA
     * // (powered by Fernflower decompiler)
     * //
     *
     * import com.daiyanping.cms.proxy.ITestService;
     * import java.lang.reflect.InvocationHandler;
     * import java.lang.reflect.Method;
     * import java.lang.reflect.Proxy;
     * import java.lang.reflect.UndeclaredThrowableException;
     *
     * public final class TestJdkProxy extends Proxy implements ITestService {
     *     private static Method m1;
     *     private static Method m2;
     *     private static Method m3;
     *     private static Method m0;
     *
     *     public TestJdkProxy(InvocationHandler var1) throws  {
     *         super(var1);
     *     }
     *
     *     public final boolean equals(Object var1) throws  {
     *         try {
     *             return (Boolean)super.h.invoke(this, m1, new Object[]{var1});
     *         } catch (RuntimeException | Error var3) {
     *             throw var3;
     *         } catch (Throwable var4) {
     *             throw new UndeclaredThrowableException(var4);
     *         }
     *     }
     *
     *     public final String toString() throws  {
     *         try {
     *             return (String)super.h.invoke(this, m2, (Object[])null);
     *         } catch (RuntimeException | Error var2) {
     *             throw var2;
     *         } catch (Throwable var3) {
     *             throw new UndeclaredThrowableException(var3);
     *         }
     *     }
     *
     *     public final void say() throws  {
     *         try {
     *             super.h.invoke(this, m3, (Object[])null);
     *         } catch (RuntimeException | Error var2) {
     *             throw var2;
     *         } catch (Throwable var3) {
     *             throw new UndeclaredThrowableException(var3);
     *         }
     *     }
     *
     *     public final int hashCode() throws  {
     *         try {
     *             return (Integer)super.h.invoke(this, m0, (Object[])null);
     *         } catch (RuntimeException | Error var2) {
     *             throw var2;
     *         } catch (Throwable var3) {
     *             throw new UndeclaredThrowableException(var3);
     *         }
     *     }
     *
     *     static {
     *         try {
     *             m1 = Class.forName("java.lang.Object").getMethod("equals", Class.forName("java.lang.Object"));
     *             m2 = Class.forName("java.lang.Object").getMethod("toString");
     *             m3 = Class.forName("com.daiyanping.cms.proxy.ITestService").getMethod("say");
     *             m0 = Class.forName("java.lang.Object").getMethod("hashCode");
     *         } catch (NoSuchMethodException var2) {
     *             throw new NoSuchMethodError(var2.getMessage());
     *         } catch (ClassNotFoundException var3) {
     *             throw new NoClassDefFoundError(var3.getMessage());
     *         }
     *     }
     * }
     */
    public static void test3() {
        Class<TestServiceImpl> testServiceClass = TestServiceImpl.class;
        String proxyName = "TestJdkProxy";
        //生成class的字节数组，此处生成的class与proxy.newProxyInstance中生成的class除了代理类的名字不同，其它内容完全一致
        byte[] classFile = ProxyGenerator.generateProxyClass(proxyName, testServiceClass.getInterfaces());
        String path = testServiceClass.getResource(".").getPath();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path + proxyName + ".class");
            fos.write(classFile);//保存到磁盘
            fos.flush();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * cglib动态代理生成的class 保存到硬盘
     */
    public static void test4() {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/Users/daiyanping/git-clone-repository/gitskills/cms/src/main/java/com/daiyanping/cms/proxy");
        TestServiceImpl testService = new TestServiceImpl("s", "b");
        TestServerImplCglibProxy testServerImplCglibProxy = new TestServerImplCglibProxy(testService);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(TestServiceImpl.class);
        enhancer.setInterfaces(new Class<?>[] {ITestService.class});
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        enhancer.setCallbackTypes(new Class[]{MethodInterceptor.class});
        enhancer.setCallback(testServerImplCglibProxy);
        TestServiceImpl o = (TestServiceImpl) enhancer.create();
        o.say();


    }

    /**
     *  如果被代理的类没有无参构造函数，就必须使用create(Class[] argumentTypes, Object[] arguments)进行创建
     */
    public static void test5() {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/Users/daiyanping/git-clone-repository/gitskills/cms/src/main/java/com/daiyanping/cms/proxy");
        TestServiceImpl testService = new TestServiceImpl("s", "b");
        TestServerImplCglibProxy testServerImplCglibProxy = new TestServerImplCglibProxy(testService);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(TestServiceImpl.class);
        enhancer.setInterfaces(new Class<?>[] {ITestService.class});
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        enhancer.setCallbackTypes(new Class[]{MethodInterceptor.class});
        enhancer.setCallback(testServerImplCglibProxy);
        TestServiceImpl o = (TestServiceImpl) enhancer.create(new Class[]{String.class, String.class}, new Object[]{"a", "b"});
        o.say();


    }

}
