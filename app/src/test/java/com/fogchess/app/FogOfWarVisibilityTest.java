package com.fogchess.app;

import org.junit.Test;
import static org.junit.Assert.*;

public class FogOfWarVisibilityTest {

    @Test
    public void testPawnVisibility() {
        // Create a test board
        ChessBoardView.ChessPiece[][] board = new ChessBoardView.ChessPiece[8][8];
        boolean[][] visibleCells = new boolean[8][8];
        
        // Place a white pawn
        board[6][3] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.PAWN, true);
        
        // Calculate visibility for white
        ChessRules.updateVisibility(board, visibleCells, true);
        
        // Pawn should see diagonally forward
        assertTrue(visibleCells[5][2]);
        assertTrue(visibleCells[5][4]);
        
        // Pawn should not see directly forward or other cells
        assertFalse(visibleCells[5][3]);
        assertFalse(visibleCells[4][3]);
    }
    
    @Test
    public void testKnightVisibility() {
        // Create a test board
        ChessBoardView.ChessPiece[][] board = new ChessBoardView.ChessPiece[8][8];
        boolean[][] visibleCells = new boolean[8][8];
        
        // Place a white knight
        board[7][1] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.KNIGHT, true);
        
        // Calculate visibility for white
        ChessRules.updateVisibility(board, visibleCells, true);
        
        // Knight should see in L-shapes
        assertTrue(visibleCells[5][0]);
        assertTrue(visibleCells[5][2]);
        assertTrue(visibleCells[6][3]);
        
        // Knight should not see other cells
        assertFalse(visibleCells[6][1]);
        assertFalse(visibleCells[5][1]);
    }
    
    @Test
    public void testBishopVisibility() {
        // Create a test board
        ChessBoardView.ChessPiece[][] board = new ChessBoardView.ChessPiece[8][8];
        boolean[][] visibleCells = new boolean[8][8];
        
        // Place a white bishop
        board[7][2] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.BISHOP, true);
        
        // Calculate visibility for white
        ChessRules.updateVisibility(board, visibleCells, true);
        
        // Bishop should see diagonally
        assertTrue(visibleCells[6][1]);
        assertTrue(visibleCells[5][0]);
        assertTrue(visibleCells[6][3]);
        assertTrue(visibleCells[5][4]);
        assertTrue(visibleCells[4][5]);
        
        // Bishop should not see other cells
        assertFalse(visibleCells[6][2]);
        assertFalse(visibleCells[5][2]);
    }
    
    @Test
    public void testRookVisibility() {
        // Create a test board
        ChessBoardView.ChessPiece[][] board = new ChessBoardView.ChessPiece[8][8];
        boolean[][] visibleCells = new boolean[8][8];
        
        // Place a white rook
        board[7][0] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.ROOK, true);
        
        // Calculate visibility for white
        ChessRules.updateVisibility(board, visibleCells, true);
        
        // Rook should see horizontally and vertically
        assertTrue(visibleCells[7][1]);
        assertTrue(visibleCells[7][2]);
        assertTrue(visibleCells[6][0]);
        assertTrue(visibleCells[5][0]);
        assertTrue(visibleCells[4][0]);
        
        // Rook should not see other cells
        assertFalse(visibleCells[6][1]);
        assertFalse(visibleCells[5][2]);
    }
    
    @Test
    public void testQueenVisibility() {
        // Create a test board
        ChessBoardView.ChessPiece[][] board = new ChessBoardView.ChessPiece[8][8];
        boolean[][] visibleCells = new boolean[8][8];
        
        // Place a white queen
        board[7][3] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.QUEEN, true);
        
        // Calculate visibility for white
        ChessRules.updateVisibility(board, visibleCells, true);
        
        // Queen should see horizontally, vertically, and diagonally
        assertTrue(visibleCells[7][0]);
        assertTrue(visibleCells[7][1]);
        assertTrue(visibleCells[7][2]);
        assertTrue(visibleCells[7][4]);
        assertTrue(visibleCells[6][2]);
        assertTrue(visibleCells[5][1]);
        assertTrue(visibleCells[6][3]);
        assertTrue(visibleCells[5][3]);
        assertTrue(visibleCells[6][4]);
        assertTrue(visibleCells[5][5]);
        
        // Queen should not see other cells
        assertFalse(visibleCells[5][2]);
        assertFalse(visibleCells[6][5]);
    }
    
    @Test
    public void testEnhancedKingVisibility() {
        // Create a test board
        ChessBoardView.ChessPiece[][] board = new ChessBoardView.ChessPiece[8][8];
        boolean[][] visibleCells = new boolean[8][8];
        
        // Place a white king
        board[7][4] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.KING, true);
        
        // Calculate visibility for white with enhanced king visibility
        ChessRules.updateVisibilityWithEnhancedKing(board, visibleCells, true);
        
        // King should see in all directions (like queen)
        assertTrue(visibleCells[7][0]);
        assertTrue(visibleCells[7][1]);
        assertTrue(visibleCells[7][2]);
        assertTrue(visibleCells[7][3]);
        assertTrue(visibleCells[7][5]);
        assertTrue(visibleCells[7][6]);
        assertTrue(visibleCells[7][7]);
        assertTrue(visibleCells[6][3]);
        assertTrue(visibleCells[6][4]);
        assertTrue(visibleCells[6][5]);
        assertTrue(visibleCells[5][2]);
        assertTrue(visibleCells[5][4]);
        assertTrue(visibleCells[5][6]);
        
        // King should also see knight moves
        assertTrue(visibleCells[5][3]);
        assertTrue(visibleCells[5][5]);
        assertTrue(visibleCells[6][2]);
        assertTrue(visibleCells[6][6]);
    }
    
    @Test
    public void testVisibilityWithBoardFlipping() {
        // Create a test board
        ChessBoardView.ChessPiece[][] board = new ChessBoardView.ChessPiece[8][8];
        boolean[][] visibleCells = new boolean[8][8];
        
        // Place a white pawn
        board[6][3] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.PAWN, true);
        
        // Calculate visibility for white
        ChessRules.updateVisibility(board, visibleCells, true);
        
        // Store white's visibility
        boolean[][] whiteVisibility = new boolean[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(visibleCells, 0, whiteVisibility, 0, 8);
        }
        
        // Clear visibility array
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                visibleCells[i][j] = false;
            }
        }
        
        // Place a black pawn
        board[1][3] = new ChessBoardView.ChessPiece(ChessBoardView.ChessPiece.Type.PAWN, false);
        
        // Calculate visibility for black
        ChessRules.updateVisibility(board, visibleCells, false);
        
        // Black pawn should see diagonally forward (opposite direction from white)
        assertTrue(visibleCells[2][2]);
        assertTrue(visibleCells[2][4]);
        
        // Verify that flipping the board doesn't affect visibility rules
        // Black's forward is opposite of white's forward
        assertTrue(whiteVisibility[5][2]); // White pawn sees diagonally forward-left
        assertTrue(visibleCells[2][2]);    // Black pawn sees diagonally forward-left (from black's perspective)
    }
}
