package com.daiyanping.cms.ThreadLocal;

import java.util.concurrent.*;

/**
 * @ClassName ThreadLocalOOM
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-19
 * @Version 0.1
 */
public class ThreadLocalOOM {

    private static ExecutorService executorService = new ThreadPoolExecutor(5, 5, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    static class Test {
        private byte[] value = new byte[30 * 1024 * 1024];
    }

    private static ThreadLocal<Test> threadLocal = new ThreadLocal();

    public static void main(String[] args) {

//       for (int i = 0; i < 500; i++) {
//           executorService.submit(() ->{
//
////               new Test();
//               threadLocal.set(new Test());
//           });
//       }


//           executorService.submit(() ->{
//               while (true) {
//
//                   threadLocal.set(new Test());
//                   Test test = threadLocal.get();
//                   System.out.println(test);
//               }
//           });
        new Thread(() -> {
            while (true) {
                  ThreadLocal<Test> threadLocal = new ThreadLocal();
                  threadLocal.set(new Test());
//                  threadLocal.remove();
            }
        }).start();


    }
}
