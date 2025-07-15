package chess.pieces;

import chess.Board;
import java.util.ArrayList;

public class King implements Piece {
  String square;
  boolean white;

  public King(String squareParam, boolean isWhite) {
    square = squareParam;
    white = isWhite;
  }

  public String[] GetMoves(Board oldBoard) {
    ArrayList<String> moves = new ArrayList<String>();
    int[] index = oldBoard.posToIndex(square);
    if (index[0] != 7) {
      index[0]++;
      if (oldBoard.getPiece(index) == null || oldBoard.getPiece(index).isWhite() != white) {
        moves.add(square + oldBoard.indexToPos(index));
      }
    }
    index = oldBoard.posToIndex(square);
    if (index[0] != 0) {
      index[0]--;
      if (oldBoard.getPiece(index) == null || oldBoard.getPiece(index).isWhite() != white) {
        moves.add(square + oldBoard.indexToPos(index));
      }
    }
    index = oldBoard.posToIndex(square);
    if (index[1] != 0) {
      index[1]--;
      if (oldBoard.getPiece(index) == null || oldBoard.getPiece(index).isWhite() != white) {
        moves.add(square + oldBoard.indexToPos(index));
      }
    }
    index = oldBoard.posToIndex(square);
    if (index[1] != 7) {
      index[1]++;
      if (oldBoard.getPiece(index) == null || oldBoard.getPiece(index).isWhite() != white) {
        moves.add(square + oldBoard.indexToPos(index));
      }
    }
    index = oldBoard.posToIndex(square);
    if (index[0] != 7 && index[1] != 7) {
      index[0]++;
      index[1]++;
      if (oldBoard.getPiece(index) == null || oldBoard.getPiece(index).isWhite() != white) {
        moves.add(square + oldBoard.indexToPos(index));
      }
    }
    index = oldBoard.posToIndex(square);
    if (index[0] != 7 && index[1] != 0) {
      index[0]++;
      index[1]--;
      if (oldBoard.getPiece(index) == null || oldBoard.getPiece(index).isWhite() != white) {
        moves.add(square + oldBoard.indexToPos(index));
      }
    }
    index = oldBoard.posToIndex(square);
    if (index[0] != 0 && index[1] != 7) {
      index[0]--;
      index[1]++;
      if (oldBoard.getPiece(index) == null || oldBoard.getPiece(index).isWhite() != white) {
        moves.add(square + oldBoard.indexToPos(index));
      }
    }
    index = oldBoard.posToIndex(square);
    if (index[0] != 0 && index[1] != 0) {
      index[0]--;
      index[1]--;
      if (oldBoard.getPiece(index) == null || oldBoard.getPiece(index).isWhite() != white) {
        moves.add(square + oldBoard.indexToPos(index));
      }
    }
    String[] toReturn = moves.toArray(new String[moves.size()]);
    return toReturn;
  }

  @Override
  public String toString() {
    if (white) {
      return "K";
    } else {
      return "k";
    }
  }

  @Override
  public void newPos(String newSquare) {
    square = newSquare;
  }

  public boolean isWhite() {
    return white;
  }

  public int value() {
    return 0;
  }

  public String getSquare() {
    return square;
  }
}
