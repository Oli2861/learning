package com.oli.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Server {
    private ServerSocket serverSocket;
    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(4);
    private boolean isRunning = true;

    /**
     * Start the server and forwards incoming request to the request handler.
     */
    public void start(int port) throws IOException {
        // Create server socket
        serverSocket = new ServerSocket(port);

        while(isRunning){
            // Listen for connections.
            Socket socket = serverSocket.accept();
            RequestHandler requestHandler = new RequestHandler(socket);
            scheduledThreadPoolExecutor.execute(requestHandler);
        }

    }

    public void close() throws IOException {
        scheduledThreadPoolExecutor.shutdown();
        serverSocket.close();
        isRunning = false;
    }

    /**
     * Main function to start the server.
     */
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        try {
            server.start(7000);
        } catch (IOException e) {
            e.printStackTrace();
            server.close();
        }
    }

}

