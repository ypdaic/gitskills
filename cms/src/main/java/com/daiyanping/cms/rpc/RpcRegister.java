package com.daiyanping.cms.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName RpcRegister
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-18
 * @Version 0.1
 */
public class RpcRegister {

    private static ConcurrentList<Map<String, Object>> serverList = new ConcurrentList<>();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(9998);
            serverSocket.bind(inetSocketAddress);
            System.out.println("RPC注册中心已启动");
            while (true) {
                // 接受客户端的连接，这里是阻塞的
                Socket socket = serverSocket.accept();
                executorService.submit(new RpcServer.ServerTask(socket));
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
                if (object.containsKey("isGetServerInfo")) {
//                    Object serverName = object.get("serverName");
//                    List<Map<String, Object>> maps = RpcRegister.serverList.get();
//                    for (Map<String, Object> map : maps) {
//                        if (serverName.equals(map.get(serverName))) {
//                            outputStream.writeObject(map);
//                            outputStream.flush();
//                        }
//                    }
                    outputStream.writeObject(RpcRegister.serverList.get());
                            outputStream.flush();
                } else {

                    RpcRegister.serverList.set(object);
                    outputStream.writeUTF("服务注册成功");
                    outputStream.flush();
                }


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
