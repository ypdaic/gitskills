package com.daiyanping.cms.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName RpcServer
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-17
 * @Version 0.1
 */
public class RpcServer {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.submit(new RegistServer());
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(9999);
            serverSocket.bind(inetSocketAddress);
            System.out.println("RPC服务端已启动");
            while (true) {
                // 接受客户端的连接，这里是阻塞的
                Socket socket = serverSocket.accept();
                executorService.submit(new ServerTask(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class ServerTask implements Runnable {

        Socket socket;

        public ServerTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

                ObjectInputStream inputStream = null;
                ObjectOutputStream outputStream = null;
                try {

                    inputStream =  new ObjectInputStream(socket.getInputStream());
                    outputStream = new ObjectOutputStream(socket.getOutputStream());

                    Map<String, Object> object = (Map<String, Object>) inputStream.readObject();
                    String className = (String) object.get("className");
                    String methodName = (String) object.get("methodName");
                    Class[] parameterTypes = (Class[]) object.get("parameterTypes");
                    Object[] args = (Object[]) object.get("args");

                    ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
                    Class<?> clazz = systemClassLoader.loadClass(className);

                    Method method = null;
                    if (parameterTypes != null) {

                        method = clazz.getMethod(methodName, parameterTypes);
                    } else {
                        method = clazz.getMethod(methodName);
                    }
                    Object result = null;
                    if (args != null) {
                        result  = method.invoke(clazz.newInstance(), args);
                    } else {
                        result  = method.invoke(clazz.newInstance());
                    }

                    outputStream.writeObject(result);
                    outputStream.flush();

                } catch (Exception e) {
                    e.printStackTrace();


                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    static class RegistServer implements Runnable {

        @Override
        public void run() {

            Socket socket = null;
            ObjectOutputStream outputStream = null;
            ObjectInputStream inputStream = null;
            try {
                socket = new Socket();
                InetSocketAddress inetSocketAddress = new InetSocketAddress(9998);
                socket.connect(inetSocketAddress);
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());
                HashMap<String, Object> map = new HashMap<>();
                map.put("className", RpcServer.class.getName());
                map.put("ip", "127.0.0.1");
                map.put("port", 9999);
                outputStream.writeObject(map);
                outputStream.flush();
                String result = (String) inputStream.readObject();
                System.out.println(result);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
