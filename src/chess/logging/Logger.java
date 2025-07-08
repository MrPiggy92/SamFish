package chess.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Logger {
    File outFile;
    File tempFile;
    String logFolderPath;
    String zipFilePath;
    boolean debug;
    FileWriter writer;
    int counter = 0;
    ArrayList<File> files;
    public Logger (String folderName, boolean debugOn) {
        logFolderPath = folderName;
        files = new ArrayList<File>();
        try {
            String fileName = time(true);
            zipFilePath = folderName + fileName + ".zip";
            outFile = new File(folderName, fileName + ".zip");
            outFile.createNewFile();
            tempFile = File.createTempFile("SamFishLog", ".log");
            tempFile.deleteOnExit();
            writer = new FileWriter(tempFile);
            files.add(tempFile);
        } catch (IOException e) {
            System.err.println("Something went wrong.");
            System.err.println(e);
        }
        debug = debugOn;
    }
    public void output(String msg) {
        //System.out.println(msg);
        System.out.println("[OUTPUT " + time(false) + "] " + msg);
        write("[OUTPUT " + time(false) + "] " + msg);
    }
    public void input(String msg) {
        System.out.println("[INPUT  " + time(false) + "] " + msg);
        write("[INPUT  " + time(false) + "] " + msg);
    }
    public void debug(String msg) {
        if (debug) {
            System.out.println("[DEBUG " + time(false) + "] " + msg);
            write("[DEBUG  " + time(false) + "] " + msg);
        }
    }
    public void info(String msg) {
        System.out.println("[INFO " + time(false) + "] " + msg);
        write("[INFO   " + time(false) + "] " + msg);
    }
    public void warning(String msg) {
        System.out.println("[WARN " + time(false) + "] " + msg);
        write("[WARN   " + time(false) + "] " + msg);
    }
    public void fatal(String msg) {
        System.out.println("[ERROR " + time(false) + "] " + msg);
        write("[ERROR  " + time(false) + "] " + msg);
    }
    private String time (boolean fileName) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter;
        if (fileName) {
            formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy.HH-mm-ss");
        } else {
            formatter = DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss");
        }
        String result = formatter.format(now);
        return result;
    }
    private void write (String msg) {
        try {
            writer.write(msg);
            writer.write("\n");
        } catch (IOException e) {
            System.err.println("Something went wrong.");
            System.err.println(e.getMessage());
        }
    }
    public void newLog() {
        counter ++;
        //System.out.println(counter);
        /*try {
            System.out.println("game" + Integer.toString(counter) + ".log");
            compress(outFile, tempFile, "game" + Integer.toString(counter) + ".log");
        } catch (IOException e) {
            System.err.println("Something went wrong.");
            System.err.println(e.getMessage());
        }*/
        try {
            writer.close();
            tempFile = File.createTempFile("SamFishLog", ".log");
            tempFile.deleteOnExit();
            writer = new FileWriter(tempFile);
            files.add(tempFile);
        } catch (IOException e) {
            System.err.println("Something went wrong.");
            System.err.println(e.getMessage());
        }
    }
    public void close () {
        //System.out.println(files.size());
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
            ZipOutputStream zipOut = new ZipOutputStream(fos)) {
            
            writer.close();

            for (File fileToZip : files) {
                //System.out.println(fileToZip.getAbsolutePath());
                if (!fileToZip.exists() || !fileToZip.isFile()) {
                    System.out.println("Skipping: " + fileToZip.getAbsolutePath() + " (not a valid file)");
                    continue;
                }

                FileInputStream fis = new FileInputStream(fileToZip);
                try {
                    ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                    zipOut.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) >= 0) {
                        zipOut.write(buffer, 0, length);
                    }
                    zipOut.closeEntry();
                } finally {
                    fis.close();
                }
            }
        } catch (IOException e) {
            System.err.println("Something went wrong");
            System.err.println(e.getMessage());
        }
        //System.out.println("Wrote ZIP to: " + zipFilePath);
        //System.out.println("ZIP file size: " + zipFilePath.length());
    }
}
