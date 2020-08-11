package com.daiyanping.cms.agent;

import java.lang.instrument.Instrumentation;

/**
 * @ClassName AgentApp
 * @Description TODO
 * @Author daiyanping
 * @Date 2020/8/11
 * @Version 0.1
 */
public class AgentApp {

    /**
     * 在main 执行之前的修改
     * @param agentOps
     * @param inst
     */
    public static void premain(String agentOps, Instrumentation inst) {
        System.out.println("===========enter premain===========");
        inst.addTransformer(new Agent());
    }

    public static void agentmain(String agentOps, Instrumentation inst) {
        System.out.println("===========enter agentmain===========");
    }
}
