package com.oli.client;

import com.oli.client.Utils.FileUtils;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;

    /**
     * Connect to the server.
     * @param ip Ip address of the server to connect to.
     * @param port Port of the server to connect to.
     */
    public void connect(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        printWriter = new PrintWriter(socket.getOutputStream(), true);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Send a message to the server.
     * @param message The message to be sent.
     * @return The received response.
     */
    public String sendMessage(String message) throws IOException {
        printWriter.println(message);
        String response = bufferedReader.readLine();
        FileUtils.writeFile(response, "response.txt");
        return response;
    }

    /**
     * Close the connection & close all resources.
     */
    public void disconnect() throws IOException {
        bufferedReader.close();
        printWriter.close();
        socket.close();
    }

    /**
     * Start the client and send some requests.
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Go!");
        Client client = new Client();
        try {
            client.connect("127.0.0.1", 7000);
        } catch (IOException e) {
            e.printStackTrace();
            client.disconnect();
        }

        String response = client.sendMessage("test.txt");
        System.out.println(response);

        response = client.sendMessage("Hey!");
        System.out.println(response);

        response = client.sendMessage("exit");
        System.out.println(response);
        client.disconnect();
    }
}
