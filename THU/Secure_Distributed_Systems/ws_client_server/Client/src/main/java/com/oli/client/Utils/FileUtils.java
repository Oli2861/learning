package com.oli.client.Utils;

import java.io.*;


public abstract class FileUtils {

    public static void writeFile(String message, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter((filename)))) {
            writer.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendFile(String message, String filename) {
        try (PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter((filename), true)))) {
            printWriter.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFileContents(String filename) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filename);
            return readFromInputStream(inputStream);
        } catch (FileNotFoundException ignored) {
            return null;
        }
    }

    private static String readFromInputStream(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();

        try (inputStream; BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

}
