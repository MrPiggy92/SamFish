package chess.pieces;

import chess.Board;
import java.util.ArrayList;

public class Pawn implements Piece {
  String square;
  boolean white;

  public Pawn(String squareParam, boolean isWhite) {
    square = squareParam;
    white = isWhite;
  }

  public String[] GetMoves(Board oldBoard) {
    ArrayList<String> moves = new ArrayList<String>();
    int[] index = oldBoard.posToIndex(square);
    if (white) {
      if (index[0] == 0) return new String[0];
      index[0]--;
      if (oldBoard.getPiece(index) == null) {
        moves.add(square + oldBoard.indexToPos(index));
        if (square.charAt(1) == '2') {
          index[0]--;
          if (oldBoard.getPiece(index) == null) moves.add(square + oldBoard.indexToPos(index));
        }
        if (index[0] == 0) {
          moves.add(square + oldBoard.indexToPos(index) + "q");
          moves.add(square + oldBoard.indexToPos(index) + "r");
          moves.add(square + oldBoard.indexToPos(index) + "b");
          moves.add(square + oldBoard.indexToPos(index) + "n");
        }
      }
      index = oldBoard.posToIndex(square);
      index[0]--;
      if (index[1] != 7) {
        index[1]++;
        if (oldBoard.getPiece(index) != null && !(oldBoard.getPiece(index).isWhite())) {
          moves.add(square + oldBoard.indexToPos(index));
          if (index[0] == 0) {
            moves.add(square + oldBoard.indexToPos(index) + "q");
            moves.add(square + oldBoard.indexToPos(index) + "r");
            moves.add(square + oldBoard.indexToPos(index) + "b");
            moves.add(square + oldBoard.indexToPos(index) + "n");
          }
        }
      }
      index = oldBoard.posToIndex(square);
      index[0]--;
      if (index[1] != 0) {
        index[1]--;
        if (oldBoard.getPiece(index) != null && !(oldBoard.getPiece(index).isWhite())) {
          moves.add(square + oldBoard.indexToPos(index));
          if (index[0] == 0) {
            moves.add(square + oldBoard.indexToPos(index) + "q");
            moves.add(square + oldBoard.indexToPos(index) + "r");
            moves.add(square + oldBoard.indexToPos(index) + "b");
            moves.add(square + oldBoard.indexToPos(index) + "n");
          }
        }
      }
    } else {
      if (index[0] == 7) return new String[0];
      index[0]++;
      if (oldBoard.getPiece(index) == null) {
        moves.add(square + oldBoard.indexToPos(index));
        if (square.charAt(1) == '7') {
          index[0]++;
          if (oldBoard.getPiece(index) == null) moves.add(square + oldBoard.indexToPos(index));
        }
        if (index[0] == 7) {
          moves.add(square + oldBoard.indexToPos(index) + "q");
          moves.add(square + oldBoard.indexToPos(index) + "r");
          moves.add(square + oldBoard.indexToPos(index) + "b");
          moves.add(square + oldBoard.indexToPos(index) + "n");
        }
      }
      index = oldBoard.posToIndex(square);
      index[0]++;
      if (index[1] != 7) {
        index[1]++;
        if (oldBoard.getPiece(index) != null && (oldBoard.getPiece(index).isWhite())) {
          moves.add(square + oldBoard.indexToPos(index));
          if (index[0] == 8) {
            moves.add(square + oldBoard.indexToPos(index) + "q");
            moves.add(square + oldBoard.indexToPos(index) + "r");
            moves.add(square + oldBoard.indexToPos(index) + "b");
            moves.add(square + oldBoard.indexToPos(index) + "n");
          }
        }
      }
      index = oldBoard.posToIndex(square);
      index[0]++;
      if (index[1] != 0) {
        index[1]--;
        if (oldBoard.getPiece(index) != null && (oldBoard.getPiece(index).isWhite())) {
          moves.add(square + oldBoard.indexToPos(index));
          if (index[0] == 8) {
            moves.add(square + oldBoard.indexToPos(index) + "q");
            moves.add(square + oldBoard.indexToPos(index) + "r");
            moves.add(square + oldBoard.indexToPos(index) + "b");
            moves.add(square + oldBoard.indexToPos(index) + "n");
          }
        }
      }
    }
    String[] toReturn = moves.toArray(new String[moves.size()]);
    return toReturn;
  }

  @Override
  public String toString() {
    if (white) {
      return "P";
    } else {
      return "p";
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
    return 1;
  }

  public String getSquare() {
    return square;
  }
}
