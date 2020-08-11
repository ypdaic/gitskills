package com.daiyanping.cms.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @ClassName Agent
 * @Description TODO
 * @Author daiyanping
 * @Date 2020/8/11
 * @Version 0.1
 */
public class Agent implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        String loadName = className.replaceAll("/", ".");
        if (className.endsWith("MainRun")) {
            try {
                // javassist 完成字节码增强（打印方法的执行时间<纳秒>）
                CtClass ctClass = ClassPool.getDefault().get(loadName);
                // 获取hello方法的字节码
                CtMethod hello = ctClass.getDeclaredMethod("hello");
                hello.addLocalVariable("_begin", CtClass.longType);
                // 加入时间统计且打印
                hello.insertBefore("_begin = System.nanoTime();");
                hello.insertAfter("System.out.println(System.nanoTime() - _begin);");
                // 字节码返回
                return ctClass.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return classfileBuffer;
    }
}
