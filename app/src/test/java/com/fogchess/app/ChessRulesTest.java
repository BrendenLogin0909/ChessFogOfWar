package com.fogchess.app;

import org.junit.Test;
import static org.junit.Assert.*;

public class ChessRulesTest {

    @Test
    public void testPawnMovement() {
        // Create a test board
        ChessBoardView.ChessPiece[][] board = new ChessBoardView.ChessPiece[8][8];
        
        // Place a white pawn
        board[6][3] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.PAWN, true);
        
        // Test initial two-square move
        assertTrue(ChessRules.isValidMove(board, 6, 3, 4, 3, true, false, false, null));
        
        // Test one-square move
        assertTrue(ChessRules.isValidMove(board, 6, 3, 5, 3, true, false, false, null));
        
        // Test diagonal capture (should fail without opponent piece)
        assertFalse(ChessRules.isValidMove(board, 6, 3, 5, 2, true, false, false, null));
        
        // Place a black piece for capture
        board[5][2] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.PAWN, false);
        
        // Test diagonal capture (should succeed with opponent piece)
        assertTrue(ChessRules.isValidMove(board, 6, 3, 5, 2, true, false, false, null));
    }
    
    @Test
    public void testEnPassant() {
        // Create a test board
        ChessBoardView.ChessPiece[][] board = new ChessBoardView.ChessPiece[8][8];
        
        // Place a white pawn
        board[3][2] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.PAWN, true);
        
        // Place a black pawn in starting position
        board[1][3] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.PAWN, false);
        
        // Simulate black pawn moving two squares
        board[3][3] = board[1][3];
        board[1][3] = null;
        
        // Set ghost pawn position for en passant
        int[] ghostPawnPosition = {3, 3};
        
        // Test en passant capture
        assertTrue(ChessRules.isValidMove(board, 3, 2, 2, 3, true, false, false, ghostPawnPosition));
    }
    
    @Test
    public void testCastling() {
        // Create a test board
        ChessBoardView.ChessPiece[][] board = new ChessBoardView.ChessPiece[8][8];
        
        // Place white king and rooks in starting positions
        board[7][4] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.KING, true);
        board[7][0] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.ROOK, true);
        board[7][7] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.ROOK, true);
        
        // Test kingside castling
        assertTrue(ChessRules.isValidMove(board, 7, 4, 7, 6, true, true, true, null));
        
        // Test queenside castling
        assertTrue(ChessRules.isValidMove(board, 7, 4, 7, 2, true, true, true, null));
        
        // Place a piece in the way for kingside castling
        board[7][5] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.BISHOP, true);
        
        // Test kingside castling (should fail with piece in the way)
        assertFalse(ChessRules.isValidMove(board, 7, 4, 7, 6, true, true, true, null));
    }
    
    @Test
    public void testKingInCheck() {
        // Create a test board
        ChessBoardView.ChessPiece[][] board = new ChessBoardView.ChessPiece[8][8];
        
        // Place white king
        board[7][4] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.KING, true);
        
        // Place black queen threatening the king
        board[5][4] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.QUEEN, false);
        
        // Test that king is in check
        assertTrue(ChessRules.isInCheck(board, true));
        
        // Place white piece to block check
        board[6][4] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.PAWN, true);
        
        // Test that king is no longer in check
        assertFalse(ChessRules.isInCheck(board, true));
    }
    
    @Test
    public void testCheckmate() {
        // Create a test board
        ChessBoardView.ChessPiece[][] board = new ChessBoardView.ChessPiece[8][8];
        
        // Place white king in corner
        board[7][7] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.KING, true);
        
        // Place black rooks for checkmate
        board[6][7] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.ROOK, false);
        board[7][6] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.ROOK, false);
        
        // Test checkmate
        assertTrue(ChessRules.isCheckmate(board, true));
    }
    
    @Test
    public void testStalemate() {
        // Create a test board
        ChessBoardView.ChessPiece[][] board = new ChessBoardView.ChessPiece[8][8];
        
        // Place white king in corner
        board[7][7] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.KING, true);
        
        // Place black queen for stalemate
        board[5][6] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.QUEEN, false);
        
        // Test stalemate
        assertTrue(ChessRules.isStalemate(board, true));
    }
}
