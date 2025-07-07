package chess;

import java.util.Arrays;

public class Main {
    static String[] columnLetters = {"a", "b", "c", "d", "e", "f", "g", "h"};
    public static void main (String[] args) {
        boolean debug = Arrays.asList(args).contains("debug");
        boolean UCI = Arrays.asList(args).contains("UCI");
        String outFile = args[args.length-1];
        Bot bot = new Bot(debug, UCI, outFile);
        bot.run();
    }
}
