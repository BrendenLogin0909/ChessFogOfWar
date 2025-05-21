package com.fogchess.app;

import org.junit.Test;
import static org.junit.Assert.*;

public class BoardFlippingTest {

    @Test
    public void testCoordinateTransformation() {
        // Create a test board view
        ChessBoardView boardView = new ChessBoardView(null);
        
        // Test coordinate transformation when board is not flipped
        boardView.setFlipped(false);
        
        int[] coords = boardView.transformCoordinates(3, 4);
        assertEquals(3, coords[0]);
        assertEquals(4, coords[1]);
        
        // Test coordinate transformation when board is flipped
        boardView.setFlipped(true);
        
        coords = boardView.transformCoordinates(3, 4);
        assertEquals(4, coords[0]);
        assertEquals(3, coords[1]);
    }
    
    @Test
    public void testPieceMovementWithFlippedBoard() {
        // Create a test board
        ChessBoardView.ChessPiece[][] board = new ChessBoardView.ChessPiece[8][8];
        
        // Place a white pawn
        board[6][3] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.PAWN, true);
        
        // Test valid moves with board not flipped
        assertTrue(ChessRules.isValidMove(board, 6, 3, 5, 3, true, false, false, null));
        assertTrue(ChessRules.isValidMove(board, 6, 3, 4, 3, true, false, false, null));
        
        // Create a board view and flip it
        ChessBoardView boardView = new ChessBoardView(null);
        boardView.setBoard(board);
        boardView.setFlipped(true);
        
        // Get transformed coordinates for the pawn
        int[] fromCoords = boardView.transformCoordinates(6, 3);
        int[] toCoords1 = boardView.transformCoordinates(5, 3);
        int[] toCoords2 = boardView.transformCoordinates(4, 3);
        
        // Test valid moves with transformed coordinates
        assertTrue(ChessRules.isValidMove(board, fromCoords[0], fromCoords[1], 
                                         toCoords1[0], toCoords1[1], true, false, false, null));
        assertTrue(ChessRules.isValidMove(board, fromCoords[0], fromCoords[1], 
                                         toCoords2[0], toCoords2[1], true, false, false, null));
    }
    
    @Test
    public void testCastlingWithFlippedBoard() {
        // Create a test board
        ChessBoardView.ChessPiece[][] board = new ChessBoardView.ChessPiece[8][8];
        
        // Place white king and rooks in starting positions
        board[7][4] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.KING, true);
        board[7][0] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.ROOK, true);
        board[7][7] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.ROOK, true);
        
        // Test castling with board not flipped
        assertTrue(ChessRules.isValidMove(board, 7, 4, 7, 6, true, true, true, null)); // Kingside
        assertTrue(ChessRules.isValidMove(board, 7, 4, 7, 2, true, true, true, null)); // Queenside
        
        // Create a board view and flip it
        ChessBoardView boardView = new ChessBoardView(null);
        boardView.setBoard(board);
        boardView.setFlipped(true);
        
        // Get transformed coordinates for castling
        int[] kingCoords = boardView.transformCoordinates(7, 4);
        int[] kingsideCoords = boardView.transformCoordinates(7, 6);
        int[] queensideCoords = boardView.transformCoordinates(7, 2);
        
        // Test castling with transformed coordinates
        assertTrue(ChessRules.isValidMove(board, kingCoords[0], kingCoords[1], 
                                         kingsideCoords[0], kingsideCoords[1], true, true, true, null));
        assertTrue(ChessRules.isValidMove(board, kingCoords[0], kingCoords[1], 
                                         queensideCoords[0], queensideCoords[1], true, true, true, null));
    }
    
    @Test
    public void testEnPassantWithFlippedBoard() {
        // Create a test board
        ChessBoardView.ChessPiece[][] board = new ChessBoardView.ChessPiece[8][8];
        
        // Place a white pawn
        board[3][2] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.PAWN, true);
        
        // Place a black pawn that just moved two squares
        board[3][3] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.PAWN, false);
        
        // Set ghost pawn position for en passant
        int[] ghostPawnPosition = {3, 3};
        
        // Test en passant with board not flipped
        assertTrue(ChessRules.isValidMove(board, 3, 2, 2, 3, true, false, false, ghostPawnPosition));
        
        // Create a board view and flip it
        ChessBoardView boardView = new ChessBoardView(null);
        boardView.setBoard(board);
        boardView.setFlipped(true);
        
        // Get transformed coordinates for en passant
        int[] pawnCoords = boardView.transformCoordinates(3, 2);
        int[] captureCoords = boardView.transformCoordinates(2, 3);
        int[] transformedGhostPawn = {
            boardView.transformCoordinates(ghostPawnPosition[0], ghostPawnPosition[1])[0],
            boardView.transformCoordinates(ghostPawnPosition[0], ghostPawnPosition[1])[1]
        };
        
        // Test en passant with transformed coordinates
        assertTrue(ChessRules.isValidMove(board, pawnCoords[0], pawnCoords[1], 
                                         captureCoords[0], captureCoords[1], true, false, false, transformedGhostPawn));
    }
}
