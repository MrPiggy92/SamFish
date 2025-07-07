package chess.pieces;

import chess.Board;
import java.util.ArrayList;

public class Rook implements Piece {
    String square;
    boolean white;
    public Rook(String squareParam, boolean isWhite) {
        square = squareParam;
        white = isWhite;
    }
    public String[] GetMoves(Board oldBoard) {
        ArrayList<String> moves = new ArrayList<String>();
        int[] index = oldBoard.posToIndex(square);
        while (true) {
            if (index[0] == 7) break;
            index[0] ++;
            if (oldBoard.getPiece(index) == null) {
                moves.add(square + oldBoard.indexToPos(index));
            } else if (oldBoard.getPiece(index).isWhite() != white) {
                moves.add(square + oldBoard.indexToPos(index));
                break;
            } else {
                break;
            }
        }
        index = oldBoard.posToIndex(square);
        while (true) {
            if (index[0] == 0) break;
            index[0] --;
            if (oldBoard.getPiece(index) == null) {
                moves.add(square + oldBoard.indexToPos(index));
            } else if (oldBoard.getPiece(index).isWhite() != white) {
                moves.add(square + oldBoard.indexToPos(index));
                break;
            } else {
                break;
            }
        }
        index = oldBoard.posToIndex(square);
        while (true) {
            if (index[1] == 7) break;
            index[1] ++;
            if (oldBoard.getPiece(index) == null) {
                moves.add(square + oldBoard.indexToPos(index));
            } else if (oldBoard.getPiece(index).isWhite() != white) {
                moves.add(square + oldBoard.indexToPos(index));
                break;
            } else {
                break;
            }
        }
        index = oldBoard.posToIndex(square);
        while (true) {
            if (index[1] == 0) break;
            index[1] --;
            if (oldBoard.getPiece(index) == null) {
                moves.add(square + oldBoard.indexToPos(index));
            } else if (oldBoard.getPiece(index).isWhite() != white) {
                moves.add(square + oldBoard.indexToPos(index));
                break;
            } else {
                break;
            }
        }
        String[] toReturn = moves.toArray(new String[moves.size()]);
        return toReturn;
    }
    @Override
    public String toString() {
        if (white) {
            return "R";
        } else {
            return "r";
        }
    }
    @Override
    public void newPos(String newSquare) {
        square = newSquare;
    }
    public boolean isWhite() {return white;}
    public int value() {return 5;}
    public String getSquare() {return square;}
}