package com.daiyanping.cms.redis;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

@Data
@AllArgsConstructor
public class MyRedisClient {

    private String ip;

    private int port;

    private int timeout = 5000;

    private String password;

    public static void main(String[] args) {
        MyRedisClient myRedisClient = new MyRedisClient("192.168.140.129", 6379, "test1234");
        myRedisClient.set("nametest2", "daiyanping");
    }

    public MyRedisClient(String ip, int port, String password) {
        this.ip = ip;
        this.port = port;
        this.password = password;
    }

    public Socket connect() {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ip, port), timeout);

            OutputStream outputStream = socket.getOutputStream();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("*2\r\n");
            stringBuilder.append("$4\r\n");
            stringBuilder.append("AUTH\r\n");
            stringBuilder.append("$");
            stringBuilder.append(password.getBytes().length);
            stringBuilder.append("\r\n");
            stringBuilder.append(password);
            stringBuilder.append("\r\n");
            outputStream.write(stringBuilder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return socket;
    }


    public void set(String key, String value) {
        Socket socket = connect();
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("*3\r\n");
            stringBuilder.append("$3\r\n");
            stringBuilder.append("set\r\n");
            stringBuilder.append("$");
            stringBuilder.append(key.getBytes().length);
            stringBuilder.append("\r\n");
            stringBuilder.append(key);
            stringBuilder.append("\r\n");
            stringBuilder.append("$");
            stringBuilder.append(value.getBytes().length);
            stringBuilder.append("\r\n");
            stringBuilder.append(value);
            stringBuilder.append("\r\n");
            outputStream.write(stringBuilder.toString().getBytes());
            byte[] bytes = new byte[1024];
            inputStream.read(bytes);
            System.out.println(new String(bytes));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
