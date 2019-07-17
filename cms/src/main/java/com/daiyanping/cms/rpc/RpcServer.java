package com.daiyanping.cms.rpc;

import java.io.*;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
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
//                    ObjectInputStream.GetField getField = inputStream.readFields();
                    String className = inputStream.readUTF();
//                    String className = (String) getField.get("className", null);
                    ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
                    Class<?> aClass = systemClassLoader.loadClass(className);
//                    String methedName = (String) getField.get("methedName", null);
                    String methedName = inputStream.readUTF();
                    Class<?> parameterTypes = (Class<?>) inputStream.readObject();
                    Object object = inputStream.readObject();
//                    Object[] args = (Object[]) getField.get("args", null);
//
//                    String[] parameterTypes = (String[]) getField.get("parameterTypes", null);
//                    Class[] parameterTypesClasses = null;
//                    if (parameterTypes != null) {
//
//                        parameterTypesClasses = new Class[parameterTypes.length];
//
//                        for (int i = 0; i < parameterTypes.length; i++) {
//                            Class<?> parameterTypeClass = systemClassLoader.loadClass(parameterTypes[i]);
//                            parameterTypesClasses[i] = parameterTypeClass;
//                        }
//                    }

                    Method method = aClass.getMethod(methedName, parameterTypes);
                    Object result = method.invoke(aClass.getInterfaces(), object);
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
}
