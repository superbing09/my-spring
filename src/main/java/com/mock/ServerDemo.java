package com.mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerDemo {
    private static ExecutorService pool = Executors.newFixedThreadPool(2);
    public static void main(String[] args) {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080);

            System.out.println("socket connect");
            while(true) {
                Socket socket = serverSocket.accept();
                pool.execute(new NSocket(socket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class NSocket implements Runnable {
    Socket socket;

    public NSocket(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(socket.getOutputStream());

            BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
            String line = in.readLine();
            while (line != null) {
                out.println("i rec you command" + line);
                out.flush();
                System.out.println("client :" + line);
                line = in.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("exit");
    }
}
