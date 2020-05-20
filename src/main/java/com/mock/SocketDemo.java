package com.mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketDemo {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8080);
        BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter os = new PrintWriter(socket.getOutputStream());

        BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));


        String line = sin.readLine();

        while (!line.equals("bye")) {

            os.println(line);
            os.flush();
            System.out.println("service:" + is.readLine());
            line = sin.readLine();
        }

    }
}
