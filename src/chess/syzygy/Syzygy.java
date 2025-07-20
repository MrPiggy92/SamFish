package chess.syzygy;

import chess.Board;
import chess.pieces.*;

import java.io.File;
import java.util.ArrayList;

public class Syzygy {
    Board board;
    File tables;
    File rtbw;
    File rtbz;
    public boolean newPos(Board board) {
        SyzygyWDLIndex indexer = new SyzygyWDLIndex();
        indexer.main();
        if (board.getPieces().size() <= this.size()) {
            this.board = board;
            ArrayList<Piece> whitePieces = new ArrayList<Piece>();
            ArrayList<Piece> blackPieces = new ArrayList<Piece>();
            for (Piece piece : board.getPieces()) {
                if (piece.isWhite()) {
                    whitePieces.add(piece);
                } else {
                    blackPieces.add(piece);
                }
            }
            StringBuilder sb = new StringBuilder();
            ArrayList<Piece> whitePiecesOrdered = new ArrayList<Piece>();
            ArrayList<Piece> blackPiecesOrdered = new ArrayList<Piece>();
            int blackPoints = 0; int whitePoints = 0;
            for (Piece piece : whitePieces) whitePoints += piece.value();
            for (Piece piece : blackPieces) blackPoints += piece.value();
            while (whitePieces.size() > 0) {
                int maxValue = 0;
                Piece maxPiece = null;
                for (Piece piece : whitePieces) {
                    if (piece == null) continue;
                    if (piece.value() == 0) {
                        //sb.append(piece.toString());
                        whitePiecesOrdered.add(piece);
                        whitePieces.set(whitePieces.indexOf(piece), null);
                    } else if (piece.value() > maxValue) {
                        maxPiece = piece;
                        maxValue = piece.value();
                    }
                }
                while (whitePieces.contains(null)) whitePieces.remove(null);
                if (maxPiece != null) {
                    //sb.append(maxPiece.toString());
                    whitePiecesOrdered.add(maxPiece);
                    whitePieces.remove(maxPiece);
                }
            }
            //sb.append("v");
            while (blackPieces.size() > 0) {
                int maxValue = 0;
                Piece maxPiece = null;
                for (Piece piece : blackPieces) {
                    if (piece == null) continue;
                    if (piece.value() == 0) {
                        //sb.append(piece.toString().toUpperCase());
                        blackPiecesOrdered.add(piece);
                        blackPieces.set(blackPieces.indexOf(piece), null);
                    } else if (piece.value() > maxValue) {
                        maxPiece = piece;
                        maxValue = piece.value();
                    }
                }
                while (blackPieces.contains(null)) blackPieces.remove(null);
                if (maxPiece != null) {
                    //sb.append(maxPiece.toString().toUpperCase());
                    blackPiecesOrdered.add(maxPiece);
                    blackPieces.remove(maxPiece);
                }
            }
            if (whitePoints > blackPoints) {
                for (Piece piece : whitePiecesOrdered) {
                    sb.append(piece.toString());
                }
                sb.append("v");
                 for (Piece piece : blackPiecesOrdered) {
                    sb.append(piece.toString().toUpperCase());
                }
            } else {
                for (Piece piece : blackPiecesOrdered) {
                    sb.append(piece.toString().toUpperCase());
                }
                sb.append("v");
                 for (Piece piece : whitePiecesOrdered) {
                    sb.append(piece.toString());
                }
            }
            System.err.println(sb.toString());
            rtbw = new File(tables, sb.toString()+".rtbw");
            rtbz = new File(tables, sb.toString()+".rtbz");
            return true;
        }
        return false;
    }
    public void newPath(String path) {
        tables = new File(path);
    }
    public String bestmove() {
        System.err.println(rtbw.getAbsolutePath());
        return "";
    }
    private int size() {
        File[] files = this.tables.listFiles();
        int maxLength = 0;
        int size = 0;
        for (File file : files) {
            if (file.getName().length() > maxLength) {
                maxLength = file.getName().length();
                size = maxLength - 6;
            }
        }
        return size;
    }
}
