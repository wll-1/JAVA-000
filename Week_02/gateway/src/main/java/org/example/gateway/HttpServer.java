package org.example.gateway;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(40);
        try (ServerSocket serverSocket = new ServerSocket(8082)){
            while (true){
                final Socket socket = serverSocket.accept();
                //service(socket);
//                new Thread(() -> {
//                    service(socket);
//                }).start();
                executorService.execute(() -> {service(socket);});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void service(Socket socket) {
        try(PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){
            Thread.sleep(20);
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type:text/html;charset=utf-8");
            out.println();
            out.write("hello");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}