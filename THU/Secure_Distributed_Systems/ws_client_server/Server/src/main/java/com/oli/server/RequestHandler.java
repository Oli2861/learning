package com.oli.server;

import com.oli.server.utils.FileUtils;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestHandler implements Runnable{
    Logger logger = new Logger(this.getClass().getSimpleName());
    private final Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;

    public RequestHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // Initialize PrintWriter for sending responses.
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            // Initialize BufferedReader to read incoming messages.
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String request;
            while ((request = bufferedReader.readLine()) != null) {
                // Exit command closes connection.
                if ("exit".equals(request)) {
                    printWriter.println("Closing connection");
                    break;
                }

                // Serve request.
                String response = serveRequest(request);
                printWriter.println(response);
            }

            // Close resources.
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Closes all the resources.
     */
    public void close() throws IOException {
        bufferedReader.close();
        printWriter.close();
        socket.close();
    }

    /**
     * Handles incoming requests
     *
     * @param request Incoming message.
     */
    private String serveRequest(String request) throws IOException {
        logger.log(request);

        if (isFileRequest(request)) {
            // Log file request & serve file contents.
            logger.log("File requested. Request:" + request);
            String file = FileUtils.getFileContents(request);
            return file != null ? file : "file not found";
        } else {
            // Let the client know that the request cannot be served.
            logger.log("Unhandled request. Request:" + request);
            return "Unhandled request " + new Date();
        }
    }

    /**
     * Checks whether the request is asking for a file (includes .txt.).
     *
     * @param request The received request.
     * @return Whether the request is asking for a file.
     */
    private Boolean isFileRequest(String request) {
        Pattern filePattern = Pattern.compile(".txt$");
        Matcher matcher = filePattern.matcher(request);
        return matcher.find();
    }
}
