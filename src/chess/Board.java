package chess;

import java.util.Arrays;
import java.util.ArrayList;
import chess.pieces.*;
import chess.logging.Logger;

public class Board {
    Piece[][] board = new Piece[][] {{null,null,null,null,null,null,null,null},
    {null,null,null,null,null,null,null,null},
    {null,null,null,null,null,null,null,null},
    {null,null,null,null,null,null,null,null},
    {null,null,null,null,null,null,null,null},
    {null,null,null,null,null,null,null,null},
    {null,null,null,null,null,null,null,null},
    {null,null,null,null,null,null,null,null}};
    static final String textBoard = """
       +---+---+---+---+---+---+---+---+
     8 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
       +---+---+---+---+---+---+---+---+
     7 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
       +---+---+---+---+---+---+---+---+
     6 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
       +---+---+---+---+---+---+---+---+
     5 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
       +---+---+---+---+---+---+---+---+
     4 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
       +---+---+---+---+---+---+---+---+
     3 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
       +---+---+---+---+---+---+---+---+
     2 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
       +---+---+---+---+---+---+---+---+
     1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
       +---+---+---+---+---+---+---+---+
         a   b   c   d   e   f   g   h""";
    String repr;
    ArrayList<String> moves = new ArrayList<String>();
    boolean printFormatting;
    Logger logger;
    public Board (boolean formatting, Logger prevLogger) {
        printFormatting = formatting;
        logger = prevLogger;
        for (int i = 0; i < 8; i ++) {
            for (int j = 0; j < 8; j ++) {
                if (i == 0) {
                    if (j == 0 || j == 7) {
                        board[i][j] = new Rook(Main.columnLetters[j] + (8-i), false);
                    } else if (j == 1 || j == 6) {
                        board[i][j] = new Knight(Main.columnLetters[j] + (8-i), false);
                    } else if (j == 2 || j == 5) {
                        board[i][j] = new Bishop(Main.columnLetters[j] + (8-i), false);
                    } else if (j == 3) {
                        board[i][j] = new Queen(Main.columnLetters[j] + (8-i), false);
                    } else {
                        board[i][j] = new King(Main.columnLetters[j] + (8-i), false);
                    }
                } else if (i == 1) {
                    board[i][j] = new Pawn(Main.columnLetters[j] + (8-i), false);
                } else if (i == 6) {
                    board[i][j] = new Pawn(Main.columnLetters[j] + (8-i), true);
                } else if (i == 7) {
                    if (j == 0 || j == 7) {
                        board[i][j] = new Rook(Main.columnLetters[j] + (8-i), true);
                    } else if (j == 1 || j == 6) {
                        board[i][j] = new Knight(Main.columnLetters[j] + (8-i), true);
                    } else if (j == 2 || j == 5) {
                        board[i][j] = new Bishop(Main.columnLetters[j] + (8-i), true);
                    } else if (j == 3) {
                        board[i][j] = new Queen(Main.columnLetters[j] + (8-i), true);
                    } else {
                        board[i][j] = new King(Main.columnLetters[j] + (8-i), true);
                    }
                }
            }
        }
    }
    @Override
    public String toString() {
        repr = "";
        repr += textBoard;
        for (int i = 0; i < 8; i ++) {
            for (int j = 0; j < 8; j ++) {
                if (board[i][j] == null) {
                    repr = repr.replaceFirst("0", " ");
                } else if (printFormatting) {
                    if (board[i][j].isWhite()) {
                        repr = repr.replaceFirst("0", "\u001B[1m" + board[i][j].toString()+"\u001B[22;39m");
                    } else {
                        repr = repr.replaceFirst("0", "\u001B[1;31m" + board[i][j].toString()+"\u001B[22;39m");
                    }
                } else {
                    repr = repr.replaceFirst("0", board[i][j].toString());
                }
            }
        }
        return repr;
    }
    public void move(String move) {
        if (move == null) return;
        moves.add(move);
        if (move.equals("e1g1") || move.equals("e1c1") || move.equals("e8g8") || move.equals("e8c8")) {
            Piece king; Piece rook;
            switch (move) {
                case "e1g1":
                    king = board[7][4];
                    if (king == null) break;
                    king.newPos("g1");
                    board[7][6] = king;
                    board[7][4] = null;
                    rook = board[7][7];
                    if (rook == null) break;
                    rook.newPos("f1");
                    board[7][5] = rook;
                    board[7][7] = null;
                    break;
                case "e1c1":
                    king = board[7][4];
                    if (king == null) break;
                    king.newPos("c1");
                    board[7][2] = king;
                    board[7][4] = null;
                    rook = board[7][0];
                    if (rook == null) break;
                    rook.newPos("d1");
                    board[7][3] = rook;
                    board[7][0] = null;
                    break;
                case "e8g8":
                    king = board[0][4];
                    if (king == null) break;
                    king.newPos("g8");
                    board[0][6] = king;
                    board[0][4] = null;
                    rook = board[0][7];
                    if (rook == null) break;
                    rook.newPos("f8");
                    board[0][5] = rook;
                    board[0][7] = null;
                    break;
                case "e8c8":
                    king = board[0][4];
                    if (king == null) break;
                    king.newPos("c8");
                    board[0][2] = king;
                    board[0][4] = null;
                    rook = board[0][0];
                    if (rook == null) break;
                    rook.newPos("d8");
                    board[0][3] = rook;
                    board[0][0] = null;
                    break;
            
                default:
                    break;
            }
        } else {
            try {
                Piece piece = board[8-((int)move.charAt(1)-'0')][Arrays.binarySearch(Main.columnLetters, ""+move.charAt(0))];
                if (move.length() == 5) {
                    board[8-((int)move.charAt(1)-'0')][Arrays.binarySearch(Main.columnLetters, ""+move.charAt(0))] = null;
                    switch (move.charAt(4)) {
                        case 'q':
                            board[8-((int)move.charAt(3)-'0')][Arrays.binarySearch(Main.columnLetters, ""+move.charAt(2))] = new Queen((move.charAt(2)+"")+(move.charAt(3)+""), piece.isWhite());
                            break;
                        case 'r':
                            board[8-((int)move.charAt(3)-'0')][Arrays.binarySearch(Main.columnLetters, ""+move.charAt(2))] = new Rook((move.charAt(2)+"")+(move.charAt(3)+""), piece.isWhite());
                            break;
                        case 'b':
                            board[8-((int)move.charAt(3)-'0')][Arrays.binarySearch(Main.columnLetters, ""+move.charAt(2))] = new Bishop((move.charAt(2)+"")+(move.charAt(3)+""), piece.isWhite());
                            break;
                        case 'n':
                            board[8-((int)move.charAt(3)-'0')][Arrays.binarySearch(Main.columnLetters, ""+move.charAt(2))] = new Knight((move.charAt(2)+"")+(move.charAt(3)+""), piece.isWhite());
                            break;
                        default:
                            board[8-((int)move.charAt(3)-'0')][Arrays.binarySearch(Main.columnLetters, ""+move.charAt(2))] = new Pawn((move.charAt(2)+"")+(move.charAt(3)+""), piece.isWhite());
                            break;
                    }
                    return;
                }
                piece.newPos((move.charAt(2)+"")+(move.charAt(3)+""));
                if (piece.value() == 1 && board[8-((int)move.charAt(3)-'0')][Arrays.binarySearch(Main.columnLetters, ""+move.charAt(2))] == null && move.charAt(0) != move.charAt(2)) {
                    String takePos = new String(new char[]{move.charAt(1), move.charAt(3)});
                    int[] takeIndex = posToIndex(takePos);
                    board[takeIndex[0]][takeIndex[1]] = null;
                    return;
                }
                board[8-((int)move.charAt(3)-'0')][Arrays.binarySearch(Main.columnLetters, ""+move.charAt(2))] = piece;
                board[8-((int)move.charAt(1)-'0')][Arrays.binarySearch(Main.columnLetters, ""+move.charAt(0))] = null;
            } catch (Exception e) {
                logger.fatal("Would crash now");
                logger.fatal(this.toString());
                logger.fatal(move);
                logger.fatal(e.toString());
            }
            /*Piece piece = board[8-((int)move.charAt(1)-'0')][Arrays.binarySearch(Main.columnLetters, ""+move.charAt(0))];
            piece.newPos((move.charAt(2)+"")+(move.charAt(3)+""));
            if (piece.value() == 1 && board[8-((int)move.charAt(3)-'0')][Arrays.binarySearch(Main.columnLetters, ""+move.charAt(2))] == null && move.charAt(0) != move.charAt(2)) {
                String takePos = new String(new char[]{move.charAt(1), move.charAt(3)});
                int[] takeIndex = posToIndex(takePos);
                board[takeIndex[0]][takeIndex[1]] = null;
                return;
            }
            //logger.debug(piece.toString());
            //logger.debug((move.charAt(3)-'0'));
            board[8-((int)move.charAt(3)-'0')][Arrays.binarySearch(Main.columnLetters, ""+move.charAt(2))] = piece;
            board[8-((int)move.charAt(1)-'0')][Arrays.binarySearch(Main.columnLetters, ""+move.charAt(0))] = null;
            */
        }
    }
    public Piece[][] getBoard() {
        return board;
    }
    public int[] posToIndex(String pos) {
        int[] index = {8-((int)pos.charAt(1)-'0'), Arrays.binarySearch(Main.columnLetters, ""+pos.charAt(0))};
        return index;
    }
    public String indexToPos(int[] index) {
        return Main.columnLetters[index[1]] + (8-index[0]);
    }
    public Piece getPiece(int[] index) {
        return board[index[0]][index[1]];
    }
    public Piece getPiece(String pos) {
        int[] index = posToIndex(pos);
        return board[index[0]][index[1]];
    }
    public Board newBoardWithmove(String move) {
        Board newBoard = new Board(printFormatting, logger);
        for (String oldMove: moves) {
            newBoard.move(oldMove);
        }
        newBoard.move(move);
        return newBoard;
    }
    public String[] nextPositions(boolean whitesTurn, boolean ignoreKingMoves) {
        ArrayList<String> newBoards = new ArrayList<String>();
        for (int i = 0; i < 8; i ++) {
            for (int j = 0; j < 8; j ++) {
                if (board[i][j] != null && (board[i][j].isWhite() == whitesTurn)) {
                    if (ignoreKingMoves && board[i][j].value() == 0) continue;
                    for (String move : board[i][j].GetMoves(this)) {
                        newBoards.add(move);
                    }
                }
            }
        }
        String[] toReturn = newBoards.toArray(new String[moves.size()]);
        return toReturn;
    }
    public int whiteWon() {
        if (moves.size() > 12 && ((moves.get(moves.size()-1).equals(moves.get(moves.size()-5))) && (moves.get(moves.size()-5).equals(moves.get(moves.size()-9)))) && ((moves.get(moves.size()-2).equals(moves.get(moves.size()-6))) && (moves.get(moves.size()-6).equals(moves.get(moves.size()-10)))) && ((moves.get(moves.size()-3).equals(moves.get(moves.size()-7))) && (moves.get(moves.size()-7).equals(moves.get(moves.size()-11)))) && ((moves.get(moves.size()-4).equals(moves.get(moves.size()-8))) && (moves.get(moves.size()-8).equals(moves.get(moves.size()-12))))){
            return -2;
        }
        boolean whiteKing = false;
        boolean blackKing = false;
        for (int i = 0; i < 8; i ++) {
            for (int j = 0; j < 8; j ++) {
                if (board[i][j] == null) continue;
                if (board[i][j].value() == 10000 && board[i][j].isWhite()) whiteKing = true;
                if (board[i][j].value() == 10000 && !board[i][j].isWhite()) blackKing = true;
            }
        }
        if (whiteKing && blackKing) return -1;
        if (whiteKing) return 0;
        if (blackKing) return 1;
        return -1;
    }
    public String kingSquare(boolean white) {
        for (int i = 0; i < 8; i ++) {
            for (int j = 0; j < 8; j ++) {
                if (board[i][j] != null && board[i][j].value() == 0 && board[i][j].isWhite() == white) {
                    return indexToPos(new int[]{i, j});
                }
            }
        }
        return null;
    }
    public String genFEN() {
        String FEN = "\\";
        for (int i = 0; i < 8; i ++) {
            int count = 0;
            for (int j = 0; j < 8; j ++) {
                if (board[i][j] == null) {
                    count ++;
                } else {
                    if (count > 0) {
                        FEN += Integer.toString(count);
                        count = 0;
                    }
                    FEN += board[i][j];
                }
            }
            if (count > 0) {
                FEN += Integer.toString(count);
            }
            FEN += "\\";
        }
        return FEN;
    }
    public void getFEN(String FEN) {
        board = new Piece[][] {{null,null,null,null,null,null,null,null},
        {null,null,null,null,null,null,null,null},
        {null,null,null,null,null,null,null,null},
        {null,null,null,null,null,null,null,null},
        {null,null,null,null,null,null,null,null},
        {null,null,null,null,null,null,null,null},
        {null,null,null,null,null,null,null,null},
        {null,null,null,null,null,null,null,null}};
        String[] unravelled = FEN.split("\\\\");
        int i = 0; int j = 0;
        for (String section : unravelled) {
            if (section.equals("")) continue;
            for (char character : section.toCharArray()) {
                if (character == '1' || character == '2' || character == '3' || character == '4' || character == '5' || character == '6' || character == '7' || character == '8') {
                    j += (character-'0');
                } else {
                    switch (character) {
                        case 'p':
                            board[i][j] = new Pawn(Main.columnLetters[j] + (8-i), false);
                            break;
                        case 'b':
                            board[i][j] = new Bishop(Main.columnLetters[j] + (8-i), false);
                            break;
                        case 'n':
                            board[i][j] = new Knight(Main.columnLetters[j] + (8-i), false);
                            break;
                        case 'r':
                            board[i][j] = new Rook(Main.columnLetters[j] + (8-i), false);
                            break;
                        case 'q':
                            board[i][j] = new Queen(Main.columnLetters[j] + (8-i), false);
                            break;
                        case 'k':
                            board[i][j] = new King(Main.columnLetters[j] + (8-i), false);
                            break;
                        case 'P':
                            board[i][j] = new Pawn(Main.columnLetters[j] + (8-i), true);
                            break;
                        case 'B':
                            board[i][j] = new Bishop(Main.columnLetters[j] + (8-i), true);
                            break;
                        case 'N':
                            board[i][j] = new Knight(Main.columnLetters[j] + (8-i), true);
                            break;
                        case 'R':
                            board[i][j] = new Rook(Main.columnLetters[j] + (8-i), true);
                            break;
                        case 'Q':
                            board[i][j] = new Queen(Main.columnLetters[j] + (8-i), true);
                            break;
                        case 'K':
                            board[i][j] = new King(Main.columnLetters[j] + (8-i), true);
                            break;
                        default:
                            break;
                    }
                }
            }
            j = 0;
            i ++;
        }
    }
}