package chess.frontend;

import chess.*;
import chess.eval.Eval;
import chess.logging.Logger;
import java.util.Arrays;

public class UCI implements Frontend {
  String input;
  String[] parts;
  String move;
  Logger logger;

  public UCI(Logger prevLogger) {
    logger = prevLogger;
  }

  public boolean run(
      Board board,
      Eval evaluator,
      boolean whitesTurn,
      Bot bot,
      String input,
      boolean debug) { // returns true for exit, false for continue
    parts = input.split(" ");
    if (parts[0].equals("uci")) {
      logger.output("id name SamFish");
      logger.output("id author Sam");
      logger.output("uciok");
    } else if (parts[0].equals("isready")) {
      logger.output("readyok");
    } else if (parts[0].equals("ucinewgame")) {
      // logger.debug(board.toString());
      logger.debug(board.genFEN());
      logger.debug(board.kingSquare(whitesTurn));
      String kingPos = board.kingSquare(whitesTurn);
      try {
        for (String nextMove : board.newBoardWithmove(move).nextPositions(!whitesTurn, false)) {
          if (kingPos == null) {
            logger.debug(kingPos);
            logger.debug(board.toString());
          }
          if (nextMove != null && kingPos != null) {
            if (nextMove.charAt(2) == kingPos.charAt(0)
                && nextMove.charAt(3) == kingPos.charAt(1)) {
              logger.debug("king");
              logger.debug(nextMove);
            }
          }
        }
      } catch (Exception e) {
        logger.warning("Got error " + e.toString() + " while trying to find move to take king.");
      }
      logger.debug(board.genFEN());
      bot.reset(true);
    } else if (parts[0].equals("go")) {
      String bestmove = "";
      if (parts[1].equals("movetime")) {
        bestmove = evaluator.findMove(board, whitesTurn, null, Integer.parseInt(parts[2]), bot);
      } else if (parts[1].equals("wtime")) {
        if (whitesTurn) {
          bestmove =
              evaluator.findMove(
                  board,
                  whitesTurn,
                  null,
                  Integer.parseInt(parts[2]) / Integer.parseInt(parts[6]) - 10,
                  bot);
        } else {
          bestmove =
              evaluator.findMove(
                  board,
                  whitesTurn,
                  null,
                  Integer.parseInt(parts[4]) / Integer.parseInt(parts[6]) - 10,
                  bot);
        }
      } else if (parts[1].equals("depth")) {
        //System.out.println(board);
        bestmove = evaluator.findMove(board, whitesTurn, Integer.parseInt(parts[2]), null, bot);
      }
      logger.output("bestmove " + bestmove);
    } else if (parts[0].equals("position")) {
      bot.reset(false);
      if (parts.length > 2 && parts[1].equals("startpos")) {
        String[] moves = Arrays.copyOfRange(parts, 3, parts.length);
        for (String move : moves) {
          bot.move(move);
        }
      } else if (parts.length > 2) {
        String fen = parts[2];
        boolean newWhitesTurn = parts[3].equals("w");
        bot.loadFen(fen, newWhitesTurn);
      }
      logger.info(Integer.toString(parts.length));
      if (parts.length > 9 && parts[8].equals("moves")) {
        logger.debug("fen & moves");
        String[] moves = Arrays.copyOfRange(parts, 9, parts.length);
        for (String move : moves) {
          bot.move(move);
        }
      }
      logger.debug(board.genFEN());
    } else if (parts[0].equals("print")) {
      logger.output(board.toString());
      //logger.output(board.genFEN());
    }
    System.out.println("");
    System.out.flush();
    return false;
  }
}
/*
 * Commands to support:
 * uci DONE
 * isready DONE
 * setoption NOT SUPPORTED
 * ucinewgame DONE
 * position DONE
 * go PARTIAL (depth, wtime btime movestogo, movetime, not yet winc binc etc., nodes, inf)
 * stop NOT YET
 * ponderhit NOT SUPPORTED
 * quit DONE
 */
