package com.fogchess.app;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class ChessRules {
    
    private static final int BOARD_SIZE = 8;
    private static boolean knightExpandedVisibility = true;  // Default to false

    // En passant state enum
    public enum EnPassantState {
        NONE,
        WHITE_PAWN,
        BLACK_PAWN
    }

    // En passant tracking
    private static int[] enPassantSquare = null;  // The square between a pawn's start and end position
    private static EnPassantState enPassantState = EnPassantState.NONE;
    private static int sourceColumn = -1;          // Column the pawn moved from

    /**
     * Creates a new en passant opportunity when a pawn moves two squares.
     * @param pawnRow The row where the pawn landed
     * @param pawnCol The column where the pawn landed
     * @param isWhitePawn Whether the pawn is white
     * @param sourceCol The column the pawn moved from
     */
    public static void createEnPassantOpportunity(int pawnRow, int pawnCol, boolean isWhitePawn, int sourceCol) {
        // The en passant square is the square between start and end positions
        int enPassantRow = isWhitePawn ? pawnRow + 1 : pawnRow - 1;
        enPassantSquare = new int[]{enPassantRow, pawnCol};
        enPassantState = isWhitePawn ? EnPassantState.WHITE_PAWN : EnPassantState.BLACK_PAWN;
        ChessRules.sourceColumn = sourceCol;
        
        Log.d("EnPassant", "Created opportunity at " + toChessNotation(enPassantRow, pawnCol) + 
                          " for " + (isWhitePawn ? "white" : "black") + " pawn");
    }
    
    /**
     * Clears the en passant opportunity.
     */
    public static void clearEnPassant() {
        if (enPassantSquare != null) {
            Log.d("EnPassant", "Clearing opportunity at " + 
                              toChessNotation(enPassantSquare[0], enPassantSquare[1]));
        }
        enPassantSquare = null;
        enPassantState = EnPassantState.NONE;
        sourceColumn = -1;
    }
    
    /**
     * Checks if a pawn can capture en passant.
     * @param pawnRow Row of the capturing pawn
     * @param pawnCol Column of the capturing pawn
     * @param isWhitePawn Whether the capturing pawn is white
     * @return true if the pawn can capture en passant
     */
    public static boolean canCaptureEnPassant(int pawnRow, int pawnCol, boolean isWhitePawn) {
        if (enPassantState == EnPassantState.NONE || enPassantSquare == null) {
            return false;
        }
        
        // The capturing pawn must be of the opposite color
        if ((isWhitePawn && enPassantState == EnPassantState.WHITE_PAWN) ||
            (!isWhitePawn && enPassantState == EnPassantState.BLACK_PAWN)) {
            return false;
        }
        
        // The capturing pawn must be on the correct row
        int requiredRow = isWhitePawn ? 3 : 4;
        if (pawnRow != requiredRow) {
            return false;
        }
        
        // The capturing pawn must be adjacent to the column of the en passant square
        return Math.abs(pawnCol - enPassantSquare[1]) == 1;
    }
    
    /**
     * Gets the en passant square.
     * @return Array containing [row, col] of the en passant square, or null if none exists
     */
    public static int[] getEnPassantSquare() {
        return enPassantSquare;
    }
    
    /**
     * Gets the source column of the pawn that created the opportunity.
     * @return The source column, or -1 if no opportunity exists
     */
    public static int getSourceColumn() {
        return sourceColumn;
    }
    
    /**
     * Gets the current en passant state.
     * @return The current en passant state
     */
    public static EnPassantState getEnPassantState() {
        return enPassantState;
    }
    
    /**
     * Checks if there is an active en passant opportunity.
     * @return true if an en passant opportunity exists
     */
    public static boolean isEnPassantActive() {
        return enPassantState != EnPassantState.NONE && enPassantSquare != null;
    }
    
    /**
     * Gets the color of the pawn that created the opportunity.
     * @return true if the pawn was white, false if black
     */
    public static boolean isEnPassantWhitePawn() {
        return enPassantState == EnPassantState.WHITE_PAWN;
    }

    // Method to toggle knight's expanded visibility
    public static void setKnightExpandedVisibility(boolean expanded) {
        knightExpandedVisibility = expanded;
    }

    // Method to get current knight visibility setting
    public static boolean isKnightExpandedVisibility() {
        return knightExpandedVisibility;
    }
    
    // Check if a move is valid according to standard chess rules
    public static boolean isValidMove(ChessBoardView.ChessPiece[][] board, int fromRow, int fromCol, 
                                     int toRow, int toCol, boolean isWhiteTurn) {
        return isValidMove(board, fromRow, fromCol, toRow, toCol, isWhiteTurn, false, false, null);
    }
    
    // Overloaded method with additional parameters for castling and en passant
    public static boolean isValidMove(ChessBoardView.ChessPiece[][] board, int fromRow, int fromCol, 
                                     int toRow, int toCol, boolean isWhiteTurn, boolean canCastleKingside,
                                     boolean canCastleQueenside, int[] enPassantSquare) {
        
        // Basic validation
        if (fromRow < 0 || fromRow >= BOARD_SIZE || fromCol < 0 || fromCol >= BOARD_SIZE ||
            toRow < 0 || toRow >= BOARD_SIZE || toCol < 0 || toCol >= BOARD_SIZE) {
            return false;
        }
        
        ChessBoardView.ChessPiece piece = board[fromRow][fromCol];
        
        // Check if there is a piece to move and it belongs to the current player
        if (piece == null || piece.isWhite != isWhiteTurn) {
            return false;
        }
        
        // Check if destination has a piece of the same color
        if (board[toRow][toCol] != null && board[toRow][toCol].isWhite == isWhiteTurn) {
            return false;
        }
        
        // Get valid moves for the selected piece
        List<int[]> validMoves = new ArrayList<>();
        
        // Handle special moves based on piece type
        switch (piece.type) {
            case PAWN:
                getPawnMoves(board, fromRow, fromCol, piece.isWhite, validMoves, enPassantSquare);
                break;
            case ROOK:
                getRookMoves(board, fromRow, fromCol, piece.isWhite, validMoves);
                break;
            case KNIGHT:
                getKnightMoves(board, fromRow, fromCol, piece.isWhite, validMoves);
                break;
            case BISHOP:
                getBishopMoves(board, fromRow, fromCol, piece.isWhite, validMoves);
                break;
            case QUEEN:
                getQueenMoves(board, fromRow, fromCol, piece.isWhite, validMoves);
                break;
            case KING:
                getKingMoves(board, fromRow, fromCol, piece.isWhite, validMoves);
                // Handle castling in the original method
                break;
        }
        
        // Check if the destination is in the list of valid moves
        for (int[] move : validMoves) {
            if (move[0] == toRow && move[1] == toCol) {
                // For castling moves, we need to validate them separately
                if (piece.type == ChessBoardView.ChessPiece.Type.KING && Math.abs(toCol - fromCol) == 2) {
                    // Check if castling is valid
                    String castleResult = canCastle(board, fromRow, fromCol, toCol > fromCol, isWhiteTurn);
                    if (!castleResult.equals("OK")) {
                        return false;
                    }
                }
                return true;
            }
        }
        
        return false;
    }
    
    // Get all valid moves for a piece
    public static List<int[]> getValidMoves(ChessBoardView.ChessPiece[][] board, int row, int col) {
        List<int[]> validMoves = new ArrayList<>();
        ChessBoardView.ChessPiece piece = board[row][col];
        
        if (piece == null) {
            return validMoves;
        }
        
        switch (piece.type) {
            case PAWN:
                getPawnMoves(board, row, col, piece.isWhite, validMoves, enPassantSquare);
                break;
            case ROOK:
                getRookMoves(board, row, col, piece.isWhite, validMoves);
                break;
            case KNIGHT:
                getKnightMoves(board, row, col, piece.isWhite, validMoves);
                break;
            case BISHOP:
                getBishopMoves(board, row, col, piece.isWhite, validMoves);
                break;
            case QUEEN:
                getQueenMoves(board, row, col, piece.isWhite, validMoves);
                break;
            case KING:
                getKingMoves(board, row, col, piece.isWhite, validMoves);
                break;
        }
        
        return validMoves;
    }
    
    // Get all cells that a piece can see (for fog of war visibility)
    public static List<int[]> getVisibleCells(ChessBoardView.ChessPiece[][] board, int row, int col) {
        List<int[]> visibleCells = new ArrayList<>();
        ChessBoardView.ChessPiece piece = board[row][col];
        
        if (piece == null) {
            return visibleCells;
        }
        
        switch (piece.type) {
            case PAWN:
                getPawnVisibility(board, row, col, piece.isWhite, visibleCells);
                break;
            case ROOK:
                getRookVisibility(board, row, col, piece.isWhite, visibleCells);
                break;
            case KNIGHT:
                getKnightVisibility(board, row, col, piece.isWhite, visibleCells);
                break;
            case BISHOP:
                getBishopVisibility(board, row, col, piece.isWhite, visibleCells);
                break;
            case QUEEN:
                getQueenVisibility(board, row, col, piece.isWhite, visibleCells);
                break;
            case KING:
                getKingVisibility(board, row, col, piece.isWhite, visibleCells);
                break;
        }
        
        return visibleCells;
    }
    
    // Pawn movement rules
    private static void getPawnMoves(ChessBoardView.ChessPiece[][] board, int row, int col, 
                                    boolean isWhite, List<int[]> moves, int[] enPassantSquare) {
        int direction = isWhite ? -1 : 1;
        int startRow = isWhite ? 6 : 1;
        
        // Move forward one square
        if (isInBounds(row + direction, col) && board[row + direction][col] == null) {
            moves.add(new int[]{row + direction, col});
            
            // Move forward two squares from starting position
            if (row == startRow && board[row + 2 * direction][col] == null) {
                moves.add(new int[]{row + 2 * direction, col});
            }
        }
        
        // Capture diagonally
        if (isInBounds(row + direction, col - 1) && 
            board[row + direction][col - 1] != null && 
            board[row + direction][col - 1].isWhite != isWhite) {
            moves.add(new int[]{row + direction, col - 1});
        }
        
        if (isInBounds(row + direction, col + 1) && 
            board[row + direction][col + 1] != null && 
            board[row + direction][col + 1].isWhite != isWhite) {
            moves.add(new int[]{row + direction, col + 1});
        }
        
        // En passant capture - corrected logic
        if (enPassantSquare != null) {
            // Log the en passant square for debugging
            System.out.println("Checking en passant. Square at: " + 
                              toChessNotation(enPassantSquare[0], enPassantSquare[1]));
            System.out.println("Pawn at: " + toChessNotation(row, col) + 
                              " (" + (isWhite ? "white" : "black") + ")");
            
            // For white pawns (moving up the board), they should be on row 3
            // For black pawns (moving down the board), they should be on row 4
            boolean correctRow = (isWhite && row == 3) || (!isWhite && row == 4);
            
            // The pawn must be adjacent to the column of the en passant square
            boolean adjacentColumn = Math.abs(col - enPassantSquare[1]) == 1;
            
            // The en passant square must be in the row where the capturing pawn will end up
            boolean correctEnPassantRow = (isWhite && enPassantSquare[0] == 2) || (!isWhite && enPassantSquare[0] == 5);
            
            if (correctRow && adjacentColumn && correctEnPassantRow) {
                System.out.println("Adding en passant move for " + (isWhite ? "white" : "black") + 
                                  " pawn at " + toChessNotation(row, col) + 
                                  " to capture at " + toChessNotation(enPassantSquare[0], enPassantSquare[1]));
                
                moves.add(new int[]{enPassantSquare[0], enPassantSquare[1]});
            } else {
                System.out.println("En passant conditions not met: correctRow=" + correctRow + 
                                  ", adjacentColumn=" + adjacentColumn + 
                                  ", correctEnPassantRow=" + correctEnPassantRow);
            }
        }
    }
    
    // Pawn visibility (for fog of war)
    private static void getPawnVisibility(ChessBoardView.ChessPiece[][] board, int row, int col, 
                                         boolean isWhite, List<int[]> visibleCells) {
        int direction = isWhite ? -1 : 1;
        
        // Pawns can see diagonally forward
        if (isInBounds(row + direction, col - 1)) {
            visibleCells.add(new int[]{row + direction, col - 1});
        }
        
        if (isInBounds(row + direction, col + 1)) {
            visibleCells.add(new int[]{row + direction, col + 1});
        }
        
        // Pawns can also see directly in front
        if (isInBounds(row + direction, col)) {
            visibleCells.add(new int[]{row + direction, col});
        }
    }
    
    // Rook movement rules
    private static void getRookMoves(ChessBoardView.ChessPiece[][] board, int row, int col, 
                                    boolean isWhite, List<int[]> moves) {
        // Move horizontally and vertically
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];
            
            while (isInBounds(r, c)) {
                if (board[r][c] == null) {
                    moves.add(new int[]{r, c});
                } else {
                    if (board[r][c].isWhite != isWhite) {
                        moves.add(new int[]{r, c});
                    }
                    break;
                }
                
                r += dir[0];
                c += dir[1];
            }
        }
    }
    
    // Rook visibility (for fog of war)
    private static void getRookVisibility(ChessBoardView.ChessPiece[][] board, int row, int col, 
                                         boolean isWhite, List<int[]> visibleCells) {
        // Rooks can see horizontally and vertically
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];
            
            while (isInBounds(r, c)) {
                visibleCells.add(new int[]{r, c});
                
                // Stop at pieces (but include the piece's cell in visibility)
                if (board[r][c] != null) {
                    break;
                }
                
                r += dir[0];
                c += dir[1];
            }
        }
    }
    
    // Knight movement rules
    private static void getKnightMoves(ChessBoardView.ChessPiece[][] board, int row, int col, 
                                      boolean isWhite, List<int[]> moves) {
        int[][] knightMoves = {
            {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
            {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };
        
        for (int[] move : knightMoves) {
            int r = row + move[0];
            int c = col + move[1];
            
            if (isInBounds(r, c) && (board[r][c] == null || board[r][c].isWhite != isWhite)) {
                moves.add(new int[]{r, c});
            }
        }
    }
    
    // Knight visibility (for fog of war)
    private static void getKnightVisibility(ChessBoardView.ChessPiece[][] board, int row, int col, 
                                        boolean isWhite, List<int[]> visibleCells) {
        // First add the standard knight move squares
        int[][] knightMoves = {
            {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
            {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };
        
        for (int[] move : knightMoves) {
            int r = row + move[0];
            int c = col + move[1];
            
            if (isInBounds(r, c)) {
                visibleCells.add(new int[]{r, c});
            }
        }
        
        // Add expanded visibility if enabled
        if (knightExpandedVisibility) {
            // Add one square in each direction
            int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},{-1, -1},{-1, 1},{1, -1},{1, 1}  // Up, Down, Left, Right + 4 diagonals
            };
            
            for (int[] dir : directions) {
                int r = row + dir[0];
                int c = col + dir[1];
                
                if (isInBounds(r, c)) {
                    visibleCells.add(new int[]{r, c});
                }
            }
        }
    }
    
    // Bishop movement rules
    private static void getBishopMoves(ChessBoardView.ChessPiece[][] board, int row, int col, 
                                      boolean isWhite, List<int[]> moves) {
        // Move diagonally
        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        
        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];
            
            while (isInBounds(r, c)) {
                if (board[r][c] == null) {
                    moves.add(new int[]{r, c});
                } else {
                    if (board[r][c].isWhite != isWhite) {
                        moves.add(new int[]{r, c});
                    }
                    break;
                }
                
                r += dir[0];
                c += dir[1];
            }
        }
    }
    
    // Bishop visibility (for fog of war)
    private static void getBishopVisibility(ChessBoardView.ChessPiece[][] board, int row, int col, 
                                           boolean isWhite, List<int[]> visibleCells) {
        // Bishops can see diagonally
        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        
        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];
            
            while (isInBounds(r, c)) {
                visibleCells.add(new int[]{r, c});
                
                // Stop at pieces (but include the piece's cell in visibility)
                if (board[r][c] != null) {
                    break;
                }
                
                r += dir[0];
                c += dir[1];
            }
        }
    }
    
    // Queen movement rules
    private static void getQueenMoves(ChessBoardView.ChessPiece[][] board, int row, int col, 
                                     boolean isWhite, List<int[]> moves) {
        // Queen combines rook and bishop moves
        getRookMoves(board, row, col, isWhite, moves);
        getBishopMoves(board, row, col, isWhite, moves);
    }
    
    // Queen visibility (for fog of war)
    private static void getQueenVisibility(ChessBoardView.ChessPiece[][] board, int row, int col, 
                                          boolean isWhite, List<int[]> visibleCells) {
        // Queen combines rook and bishop visibility
        getRookVisibility(board, row, col, isWhite, visibleCells);
        getBishopVisibility(board, row, col, isWhite, visibleCells);
    }
    
    // King movement rules
    private static void getKingMoves(ChessBoardView.ChessPiece[][] board, int row, int col, 
                                    boolean isWhite, List<int[]> moves) {
        // Regular king moves (one square in any direction)
        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
        };
        
        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];
            
            if (isInBounds(r, c) && (board[r][c] == null || board[r][c].isWhite != isWhite)) {
                moves.add(new int[]{r, c});
            }
        }
        
        // Check for castling
        ChessBoardView.ChessPiece king = board[row][col];
        if (king != null && !king.hasMoved) {
            // Add castling moves for both sides
            // These moves will be validated in handleCellClick before execution
            moves.add(new int[]{row, col + 2});  // Kingside
            moves.add(new int[]{row, col - 2});  // Queenside
        }
    }
    
    // Helper method to check if castling is possible
    public static String canCastle(ChessBoardView.ChessPiece[][] board, int kingRow, int kingCol, 
                                    boolean kingSide, boolean isWhite) {
        ChessBoardView.ChessPiece king = board[kingRow][kingCol];
        int rookCol = kingSide ? 7 : 0;
        ChessBoardView.ChessPiece rook = board[kingRow][rookCol];

        // Check if king exists, is the right color, and hasn't moved
        if (king == null || king.type != ChessBoardView.ChessPiece.Type.KING || 
            king.isWhite != isWhite || king.hasMoved) {
            return "Cannot castle - king has previously moved";
        }

        // Check if rook exists, is the right color, and hasn't moved
        if (rook == null || rook.type != ChessBoardView.ChessPiece.Type.ROOK || 
            rook.isWhite != isWhite || rook.hasMoved) {
            return "Cannot castle - rook has previously moved";
        }

        // Check if path is clear between king and rook
        int start = Math.min(kingCol, rookCol) + 1;
        int end = Math.max(kingCol, rookCol);
        for (int c = start; c < end; c++) {
            if (board[kingRow][c] != null) {
                return "Cannot castle - pieces are in the way";
            }
        }

        // Check if king is in check
        if (isInCheck(board, isWhite)) {
            return "Cannot castle - king is in check";
        }

        // Check if king would move through check
        int direction = kingSide ? 1 : -1;
        for (int c = kingCol + direction; c != rookCol; c += direction) {
            // Temporarily move the king to check if it would be in check
            ChessBoardView.ChessPiece savedKing = board[kingRow][kingCol];
            board[kingRow][kingCol] = null;
            board[kingRow][c] = savedKing;

            boolean throughCheck = isInCheck(board, isWhite);

            // Restore the king
            board[kingRow][c] = null;
            board[kingRow][kingCol] = savedKing;

            if (throughCheck) {
                return "Cannot castle - king would move through check";
            }
        }

        return "OK";
    }
    // King visibility (for fog of war) - combination of Queen and Knight
    private static void getKingVisibility(ChessBoardView.ChessPiece[][] board, int row, int col, 
                                        boolean isWhite, List<int[]> visibleCells) {
        // Add Queen-like visibility (along lines in all 8 directions)
        getQueenVisibility(board, row, col, isWhite, visibleCells);

        // Add Knight-like visibility (L-shapes)
        getKnightVisibility(board, row, col, isWhite, visibleCells);
    }
    
    // Helper method to check if coordinates are within the board
    private static boolean isInBounds(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }
    
    // Check if the king is in check
    public static boolean isInCheck(ChessBoardView.ChessPiece[][] board, boolean isWhiteKing) {
        // Find the king's position
        int kingRow = -1;
        int kingCol = -1;
        
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                ChessBoardView.ChessPiece piece = board[row][col];
                if (piece != null && piece.type == ChessBoardView.ChessPiece.Type.KING && piece.isWhite == isWhiteKing) {
                    kingRow = row;
                    kingCol = col;
                    break;
                }
            }
            if (kingRow != -1) break;
        }
        
        // Check if any opponent piece can capture the king
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                ChessBoardView.ChessPiece piece = board[row][col];
                if (piece != null && piece.isWhite != isWhiteKing) {
                    List<int[]> moves = getValidMoves(board, row, col);
                    for (int[] move : moves) {
                        if (move[0] == kingRow && move[1] == kingCol) {
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }

    /**
     * Converts board coordinates to chess notation (e.g., "e4")
     * @param row The row number (0-7)
     * @param col The column number (0-7)
     * @return The chess notation for the square
     */
    private static String toChessNotation(int row, int col) {
        char file = (char)('a' + col);
        int rank = 8 - row;
        return String.format("%c%d", file, rank);
    }
}
