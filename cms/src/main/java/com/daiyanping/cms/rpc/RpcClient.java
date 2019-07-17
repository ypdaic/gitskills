package com.daiyanping.cms.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @ClassName RpcClient
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-17
 * @Version 0.1
 */
public class RpcClient {

    public static void main(String[] args) {
        Socket socket = null;
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        try {
            socket = new Socket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(9999);
            socket.connect(inetSocketAddress);
            inputStream =  new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("className", RpcClient.class.getName());
//            map.put("methedName", "test");
//            map.put("parameterTypes", null);
//            map.put("args", null);
//            outputStream.writeObject(map);
            outputStream.writeUTF(RpcClient.class.getName());
            outputStream.writeUTF("test");
            outputStream.writeObject(null);
            outputStream.writeObject(null);
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

    public String test() {
        return "这是一个rpc调用测试";
    }


}
