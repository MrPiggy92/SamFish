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

  public Playable(Logger prevLogger, Bot bot) {
    logger = prevLogger;
    logger.info("Playable frontend initialised.");
    System.out.print(" > ");
    bot.move("a1a1");
  }

  public boolean run(
      Board board,
      Eval evaluator,
      boolean whitesTurn,
      Bot bot,
      String move,
      boolean debug) { // returns true for exit, false for continue
    moved = false;
    bot.move(move);
    move = evaluator.findMove(board, !whitesTurn, 2, null, bot, null);
    bot.move(move);
    logger.output(move);

    logger.output(board.toString());

    if (board.whiteWon(whitesTurn, false) == 0) {
      logger.output("White won!");
      return true;
    } else if (board.whiteWon(whitesTurn, false) == 1) {
      logger.output("Black won!");
      return true;
    } else if (board.whiteWon(whitesTurn, false) == -2) {
      logger.output("Draw!");
      return true;
    }
    System.out.print(" > ");
    return false;
  }

  public boolean stopSearch() {
    return false;
  }
}
