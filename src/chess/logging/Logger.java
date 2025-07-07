package chess.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    File outFile;
    File outFolder;
    boolean debug;
    FileWriter writer;
    int counter = 0;
    public Logger (String folderName, boolean debugOn) {
        try {
            String fileName = time(true);
            //System.out.println(folderName + fileName + "/game1.log");
            outFolder = new File(folderName, fileName);
            outFolder.mkdirs();
            outFile = new File(folderName, fileName + "/game" + Integer.toString(counter) + ".log");
            outFile.createNewFile();
            writer = new FileWriter(outFile);
        } catch (IOException e) {
            System.err.println("Something went wrong.");
            System.err.println(e);
        }
        debug = debugOn;
    }
    public void output(String msg) {
        System.out.println(msg);
        write("[OUTPUT " + time(false) + "] " + msg);
    }
    public void input(String msg) {
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
            System.err.println(e);
        }
    }
    public void newLog() {
        counter ++;
        try {
            outFile = new File(outFolder.getAbsolutePath(), "game" + Integer.toString(counter) + ".log");
            outFile.createNewFile();
            writer = new FileWriter(outFile);
        } catch (IOException e) {
            System.err.println("Something went wrong.");
            System.err.println(e);
        }
    }
}
