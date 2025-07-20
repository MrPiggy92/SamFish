package chess.syzygy;

import java.util.*;

public class SyzygyWDLIndex {
    // Piece constants to match Syzygy ordering:
    // P=1, N=2, B=3, R=4, Q=5, K=6 (we only encode piece types for indexing)
    private static final int WHITE = 0;
    private static final int BLACK = 1;

    // Piece type codes for internal use (0 = empty)
    private static final int EMPTY = 0;
    private static final int PAWN = 1;
    private static final int KNIGHT = 2;
    private static final int BISHOP = 3;
    private static final int ROOK = 4;
    private static final int QUEEN = 5;
    private static final int KING = 6;

    // Maps piece character from FEN to internal codes
    private static int pieceCharToCode(char c) {
        switch (c) {
            case 'P': return PAWN;
            case 'N': return KNIGHT;
            case 'B': return BISHOP;
            case 'R': return ROOK;
            case 'Q': return QUEEN;
            case 'K': return KING;
            case 'p': return PAWN;
            case 'n': return KNIGHT;
            case 'b': return BISHOP;
            case 'r': return ROOK;
            case 'q': return QUEEN;
            case 'k': return KING;
            default: return EMPTY;
        }
    }

    // Store piece data
    public static class Piece {
        int color; // 0=white,1=black
        int type;  // PAWN..KING

        public Piece(int color, int type) {
            this.color = color;
            this.type = type;
        }

        public String toString() {
            return (color == WHITE ? "W" : "B") + type;
        }
    }

    // Parsed position
    public static class Position {
        Piece[] squares = new Piece[64];
        int sideToMove;

        // count pieces by type and color
        List<Piece> whitePieces = new ArrayList<>();
        List<Piece> blackPieces = new ArrayList<>();
    }

    /**
     * Parses a FEN string into the internal Position representation.
     */
    public static Position parseFEN(String fen) throws IllegalArgumentException {
        Position pos = new Position();
        Arrays.fill(pos.squares, null);

        String[] parts = fen.split(" ");
        if (parts.length < 2)
            throw new IllegalArgumentException("Invalid FEN, missing parts");

        String board = parts[0];
        String side = parts[1];

        int sq = 0;
        for (int i = 0; i < board.length(); i++) {
            char c = board.charAt(i);
            if (c == '/') continue;
            if (Character.isDigit(c)) {
                int skip = c - '0';
                sq += skip;
                if (sq > 64) throw new IllegalArgumentException("Invalid FEN board layout");
            } else {
                int color = Character.isUpperCase(c) ? WHITE : BLACK;
                int type = pieceCharToCode(c);
                if (type == EMPTY) throw new IllegalArgumentException("Invalid piece char in FEN: " + c);

                Piece p = new Piece(color, type);
                pos.squares[sq] = p;
                if (color == WHITE) pos.whitePieces.add(p);
                else pos.blackPieces.add(p);

                sq++;
                if (sq > 64) throw new IllegalArgumentException("Invalid FEN board layout");
            }
        }
        if (sq != 64) throw new IllegalArgumentException("Invalid FEN: board not fully filled");

        pos.sideToMove = (side.equals("w")) ? WHITE : BLACK;

        return pos;
    }

    /**
     * Normalize material to Syzygy canonical order.
     * - Sort pieces by type/color according to Syzygy ordering
     * - Mirror black pieces to white side to avoid duplications
     * 
     * This is a simplified approach:
     * - Only mirror black pieces rank-wise if black is to move (we can refine later)
     * - Sort pieces by piece type ascending, then square ascending.
     */
    public static List<PieceOnSquare> normalizeAndSort(Position pos) {
        // Piece on square container
        class POS implements Comparable<POS> {
            int sq;
            Piece piece;
            public POS(int sq, Piece piece) { this.sq = sq; this.piece = piece; }
            public int compareTo(POS o) {
                // Sort by piece type ascending
                int cmp = Integer.compare(this.piece.type, o.piece.type);
                if (cmp != 0) return cmp;
                // Then by color white first
                cmp = Integer.compare(this.piece.color, o.piece.color);
                if (cmp != 0) return cmp;
                // Then by square ascending
                return Integer.compare(this.sq, o.sq);
            }
        }

        List<POS> piecesOnSquares = new ArrayList<>();
        for (int sq = 0; sq < 64; sq++) {
            Piece p = pos.squares[sq];
            if (p != null) {
                int normSq = sq;
                // For black pieces, mirror rank-wise to white side (ranks 0-3)
                if (p.color == BLACK) {
                    int rank = sq / 8;
                    int file = sq % 8;
                    normSq = (7 - rank) * 8 + file;
                }
                piecesOnSquares.add(new POS(normSq, p));
            }
        }
        Collections.sort(piecesOnSquares);

        // Convert to simplified output
        List<PieceOnSquare> output = new ArrayList<>();
        for (POS p : piecesOnSquares) {
            output.add(new PieceOnSquare(p.sq, p.piece.color, p.piece.type));
        }
        return output;
    }

    // Helper class for piece and square tuples
    public static class PieceOnSquare {
        public int square;
        public int color;
        public int type;
        public PieceOnSquare(int sq, int color, int type) {
            this.square = sq;
            this.color = color;
            this.type = type;
        }
        public String toString() {
            return (color == WHITE ? "W" : "B") + ":" + type + "@" + square;
        }
    }

    /**
     * Compute the WDL index given the normalized piece array and side to move.
     * 
     * This example supports pawnless and basic pawnful without pawn-file slicing.
     * 
     * WARNING: This is a simplified combinatorial calculation placeholder.
     * The real Syzygy indexing is more involved (king encoding, permutations,
     * pawn file slices, etc). This will work only for trivial test cases.
     */
    public static int computeWDLIndex(List<PieceOnSquare> pieces, int sideToMove) {
        // Very simplified:
        // - We compute a dummy index based on piece types and squares,
        // - Add sideToMove offset (0 or 1)
        // This is NOT the actual Syzygy formula.

        int index = 0;
        for (PieceOnSquare p : pieces) {
            index = index * 64 + p.square; // crude square encoding
            index = index * 10 + p.type;   // crude piece type encoding
        }
        index = index * 2 + sideToMove;

        return index;
    }

    /**
     * Main entry: get WDL index from FEN string.
     */
    public static int getWDLIndex(String fen) {
        Position pos = parseFEN(fen);
        List<PieceOnSquare> norm = normalizeAndSort(pos);
        int idx = computeWDLIndex(norm, pos.sideToMove);
        return idx;
    }

    // Basic test
    public void main() {
        String[] testFens = {
            "8/8/8/8/8/8/8/Kk6 w - - 0 1", // King vs King
            "8/8/8/8/8/8/8/KQk5 w - - 0 1", // King and Queen vs King
            //"8/8/8/8/8/8/8/KPk6 w - - 0 1"  // King and Pawn vs King (pawn file slice missing)
        };

        for (String fen : testFens) {
            int wdlIndex = getWDLIndex(fen);
            System.out.println("FEN: " + fen);
            System.out.println("WDL index: " + wdlIndex);
            System.out.println("---");
        }
    }
}
