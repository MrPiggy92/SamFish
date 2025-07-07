package chess;

import chess.eval.Eval;
import chess.logging.Logger;
import chess.frontend.*;
import java.util.Scanner;

public class Bot {
    Board board;
    Eval evaluator;
    Frontend frontend;
    boolean whitesTurn;
    Scanner scanner = new Scanner(System.in);
    boolean debug;
    String input;
    boolean useFormatting;
    Logger logger;
    String loggerOutFile;
    boolean useUCI;
    public Bot (boolean debugOn, boolean UCI, String outFile) {
        logger = new Logger(outFile, debugOn);
        loggerOutFile = outFile;
        useFormatting = !UCI;
        useUCI = UCI;
        evaluator = new Eval(logger);
        if (UCI) {
            frontend = new UCI(logger);
            board = new Board(false, logger);
        } else {
            frontend = new Playable(logger);
            board = new Board(true, logger);
        }
        whitesTurn = true;
        debug = debugOn;
    }
    public void move (String move) {
        board.move(move);
        whitesTurn = !whitesTurn;
    }
    public void reset (boolean newLogger) {
        if (newLogger) logger.newLog();
        board = new Board(useFormatting, logger);
        whitesTurn = true;
    }
    public void run () {
        while (scanner.hasNextLine()) {
            input = scanner.nextLine();
            logger.input(input);
            if (input.equals("quit") || input.equals("exit")) break;
            if (frontend.run(board, evaluator, whitesTurn, this, input, debug)) {
                break;
            }
        }
    }
}