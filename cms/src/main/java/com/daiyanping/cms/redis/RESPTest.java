package com.daiyanping.cms.redis;

import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class RESPTest {

    public static void main(String[] args) throws IOException {
        new Thread(new JedisClient()).start();
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(9000));
        Socket accept = serverSocket.accept();
        InputStream inputStream = accept.getInputStream();
        byte[] bytes = new byte[2048];
        inputStream.read(bytes);
        System.out.println(new String(bytes));

        /**
         * 打印结果如下：
         * *3
         * $3
         * SET
         * $4
         * name
         * $10
         * daiyanping
         */

    }

    public static class JedisClient implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Jedis jedis = new Jedis("localhost", 9000);
            jedis.set("name", "daiyanping");
        }
    }
}
