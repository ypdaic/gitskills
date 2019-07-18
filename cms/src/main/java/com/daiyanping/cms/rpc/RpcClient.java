package com.daiyanping.cms.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

/**
 * @ClassName RpcClient
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-17
 * @Version 0.1
 */
public class RpcClient {

    private static List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    public static void main(String[] args) {
        CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(new GetService());
        try {
            List o = (List) completableFuture.get();
            list.addAll(o);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        String serverName = "com.daiyanping.cms.rpc.RpcServer";
        String ip = "";
        int port = 0;
        for (Map<String, Object> map : list)
            if (serverName.equals(map.get("serverName"))) {
                ip = (String) map.get("ip");
                port = (int) map.get("port");
            }
//        list.forEach(map -> {
//            map.get
//        });
        Socket socket = null;
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        try {
            socket = new Socket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, port);
            socket.connect(inetSocketAddress);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream =  new ObjectInputStream(socket.getInputStream());
            HashMap<String, Object> map = new HashMap<>();
            map.put("className", RpcClient.class.getName());
            map.put("methodName", "test2");
            map.put("parameterTypes", null);
            map.put("args", null);
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

    static class GetService implements Supplier {

        @Override
        public Object get() {
            Socket socket = null;
            List result = null;
            ObjectOutputStream outputStream = null;
            ObjectInputStream inputStream = null;
            try {
                socket = new Socket();
                InetSocketAddress inetSocketAddress = new InetSocketAddress(9998);
                socket.connect(inetSocketAddress);
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream =  new ObjectInputStream(socket.getInputStream());
                HashMap<String, Object> map = new HashMap<>();
                map.put("isGetServerInfo", true);
                outputStream.writeObject(map);
                outputStream.flush();
                result = (List) inputStream.readObject();
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

            return result;
        }
    }

    public String test() {
        return "这是一个rpc调用测试";
    }

    public void test2() {
        System.out.println("这是一个rpc调用测试");
    }

    public String test3(int a) {
        return a + "";
    }

}
