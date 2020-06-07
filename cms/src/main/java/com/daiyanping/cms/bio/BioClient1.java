package com.daiyanping.cms.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class BioClient1 {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 9090);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String next = scanner.next();
            socket.getOutputStream().write(next.getBytes());
        }

    }
}
