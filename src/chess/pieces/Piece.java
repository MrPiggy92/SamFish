package chess.pieces;

import chess.Board;

public interface Piece {
    void newPos(String newSquare);
    String[] GetMoves(Board oldBoard);
    boolean isWhite();
    int value();
    String getSquare();
}