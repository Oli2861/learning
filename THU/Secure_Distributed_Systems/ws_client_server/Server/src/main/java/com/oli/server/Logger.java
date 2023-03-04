package com.oli.server;

import com.oli.server.utils.FileUtils;

import java.util.Date;

public class Logger {
    private final String tag;

    public Logger(String tag) {
        this.tag = tag;
    }

    /**
     * Log a message to the console and a log file.
     *
     * @param message The message to be logged.
     */
    public void log(String message) {
        String logEntry = String.format("%s\t%-20s\t%s", new Date().toString(), tag, message);
        System.out.println(logEntry);
        FileUtils.appendFile(logEntry, "log.txt");
    }
}
