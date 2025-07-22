package chess.syzygy;

import chess.logging.Logger;
//import chess.Board;
import java.io.IOException;
import java.util.Scanner;

public class WDLIndexer {
    public static int probe (String FEN, Logger logger, boolean white) {
        Process process;
        FEN += white ? " w - - 2 2" : " b - - 0 0";
        System.out.println(FEN);
        try {
            process = new ProcessBuilder("/home/arco/Documents/Java/SamFish/fathom/src/apps/fathom.linux", "--path=/home/arco/syzygy", /*"\"" + */FEN /*+ "\""*/)
                .redirectErrorStream(true)
                .start();
        } catch (IOException e) {
            //logger.fatal(e.getMessage());
            System.err.println(e);
            return -1;
        }

        Scanner sc = new Scanner(process.getInputStream());
        String line;
        int result = 0;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            if (line.startsWith("[WDL")) {
                String[] parts = line.split("\"");
                switch (parts[1]) {
                    case "Loss":
                        result = -2;
                        break;
                    case "BlessedLoss":
                        result = -1;
                        break;
                    case "Draw":
                        result = 0;
                        break;
                    case "CursedWin":
                        result = 1;
                        break;
                    case "win":
                        result = 2;
                        break;
                    default:
                        result = 0;
                        break;
                }
                break;
            }
        }
        sc.close();
        return result;
    }
}