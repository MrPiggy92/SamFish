package chess.frontend;

import chess.*;
import chess.eval.Eval;

public interface Frontend {
  boolean run(
      Board board, Eval evaluator, boolean whitesTurn, Bot bot, String input, boolean debug);

  boolean stopSearch();
}
