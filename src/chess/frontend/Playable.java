package chess.frontend;

import chess.Board;
import chess.Bot;
import chess.eval.Eval;
import chess.logging.Logger;

public class Playable implements Frontend {
    String input;
    int depth;
    boolean moved;
    String move;
    Logger logger;
    public Playable (Logger prevLogger) {
        logger = prevLogger;
        logger.info("Playable frontend initialised.");
        System.out.print(" > ");
    }
    public boolean run (Board board, Eval evaluator, boolean whitesTurn, Bot bot, String move, boolean debug) { // returns true for exit, false for continue
        moved = false;
        board.move(move);
        move = evaluator.findMove(board, whitesTurn, 2, null);
        board.move(move);
        logger.output(move);

        logger.output(board.toString());

        if (board.whiteWon() == 0) {
            logger.output("White won!");
            return true;
        } else if (board.whiteWon() == 1) {
            logger.output("Black won!");
            return true;
        } else if (board.whiteWon() == -2) {
            logger.output("Draw!");
            return true;
        }
        System.out.print(" > ");
        return false;
    }
}