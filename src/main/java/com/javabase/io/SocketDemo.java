package com.javabase.io;

import java.io.*;
import java.net.Socket;

public class SocketDemo {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("192.168.137.1", 8080);
        try(BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter os = new PrintWriter(socket.getOutputStream());
        BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
            String line = sin.readLine();

            while (!line.equals("bye")) {
                os.println(line);
                os.flush();
                System.out.println("service:" + is.readLine());
                line = sin.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(socket != null) {
                socket.close();
            }
        }
    }
}
