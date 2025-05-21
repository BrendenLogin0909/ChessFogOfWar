package com.fogchess.app.testcases;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.fogchess.app.R;
import com.fogchess.app.GameActivity;
import com.fogchess.app.ChessBoardView.ChessPiece;

public class TestCastling extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.castling_tests_menu);
        
        setupTestButtons();
    }

    private void setupTestButtons() {
        // Valid Castling Tests
        setupButton(R.id.whiteKingsideCastleButton, "castling_valid_kingside");
        setupButton(R.id.whiteQueensideCastleButton, "castling_valid_queenside");
        setupButton(R.id.blackKingsideCastleButton, "castling_valid_black_kingside");
        setupButton(R.id.blackQueensideCastleButton, "castling_valid_black_queenside");
        
        // Invalid Castling Tests
        setupButton(R.id.kingMovedButton, "castling_invalid_king_moved");
        setupButton(R.id.rookMovedButton, "castling_invalid_rook_moved");
        setupButton(R.id.piecesBetweenButton, "castling_invalid_pieces_between");
        setupButton(R.id.kingInCheckButton, "castling_invalid_in_check");
        setupButton(R.id.passThroughCheckButton, "castling_invalid_through_check");
        setupButton(R.id.landInCheckButton, "castling_invalid_land_in_check");
    }

    private void setupButton(int buttonId, String scenario) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> startGameActivity(scenario));
    }

    private void startGameActivity(String testScenario) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("test_scenario", testScenario);
        startActivity(intent);
    }

    // ===== VALID CASTLING SCENARIOS =====

    /**
     * Sets up a board for testing white kingside castling (O-O).
     * Initial position with only the necessary pieces for kingside castle.
     */
    public static ChessPiece[][] setupWhiteKingsideCastleScenario() {
        ChessPiece[][] board = new ChessPiece[8][8];
        
        // White pieces at bottom
        board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);  // White king at e1
        board[7][7] = new ChessPiece(ChessPiece.Type.ROOK, true);  // White rook at h1
        
        // Black pieces at top (minimum required)
        board[0][4] = new ChessPiece(ChessPiece.Type.KING, false); // Black king at e8
        
        return board;
    }

    /**
     * Sets up a board for testing white queenside castling (O-O-O).
     * Initial position with only the necessary pieces for queenside castle.
     */
    public static ChessPiece[][] setupWhiteQueensideCastleScenario() {
        ChessPiece[][] board = new ChessPiece[8][8];
        
        // White pieces at bottom
        board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);  // White king at e1
        board[7][0] = new ChessPiece(ChessPiece.Type.ROOK, true);  // White rook at a1
        
        // Black pieces at top (minimum required)
        board[0][4] = new ChessPiece(ChessPiece.Type.KING, false); // Black king at e8
        
        return board;
    }

    /**
     * Sets up a board for testing black kingside castling.
     * Initial position with only the necessary pieces for black kingside castle.
     */
    public static ChessPiece[][] setupBlackKingsideCastleScenario() {
        ChessPiece[][] board = new ChessPiece[8][8];
        
        // Black pieces at top
        board[0][4] = new ChessPiece(ChessPiece.Type.KING, false); // Black king at e8
        board[0][7] = new ChessPiece(ChessPiece.Type.ROOK, false); // Black rook at h8
        
        // White pieces at bottom (minimum required)
        board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);  // White king at e1
        
        return board;
    }

    /**
     * Sets up a board for testing black queenside castling.
     * Initial position with only the necessary pieces for black queenside castle.
     */
    public static ChessPiece[][] setupBlackQueensideCastleScenario() {
        ChessPiece[][] board = new ChessPiece[8][8];
        
        // Black pieces at top
        board[0][4] = new ChessPiece(ChessPiece.Type.KING, false); // Black king at e8
        board[0][0] = new ChessPiece(ChessPiece.Type.ROOK, false); // Black rook at a8
        
        // White pieces at bottom (minimum required)
        board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);  // White king at e1
        
        return board;
    }

    // ===== INVALID CASTLING SCENARIOS =====

    /**
     * Sets up a board where castling is invalid because the king has moved.
     */
    public static ChessPiece[][] setupKingMovedScenario() {
        ChessPiece[][] board = new ChessPiece[8][8];
        
        // White pieces at bottom
        board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);  // White king at e1
        board[7][7] = new ChessPiece(ChessPiece.Type.ROOK, true);  // White rook at h1
        
        // Black pieces at top
        board[0][4] = new ChessPiece(ChessPiece.Type.KING, false); // Black king at e8
        
        // Mark king as having moved
        board[7][4].hasMoved = true;
        
        return board;
    }

    /**
     * Sets up a board where castling is invalid because the rook has moved.
     */
    public static ChessPiece[][] setupRookMovedScenario() {
        ChessPiece[][] board = new ChessPiece[8][8];
        
        // White pieces at bottom
        board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);  // White king at e1
        board[7][7] = new ChessPiece(ChessPiece.Type.ROOK, true);  // White rook at h1
        
        // Black pieces at top
        board[0][4] = new ChessPiece(ChessPiece.Type.KING, false); // Black king at e8
        
        // Mark rook as having moved
        board[7][7].hasMoved = true;
        
        return board;
    }

    /**
     * Sets up a board where castling is invalid due to pieces between king and rook.
     * Uses queenside castling with a knight between the king and rook.
     */
    public static ChessPiece[][] setupPiecesBetweenScenario() {
        ChessPiece[][] board = new ChessPiece[8][8];
        
        // White pieces at bottom
        board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);   // White king at e1
        board[7][0] = new ChessPiece(ChessPiece.Type.ROOK, true);   // White rook at a1
        board[7][1] = new ChessPiece(ChessPiece.Type.KNIGHT, true); // White knight at b1
        
        // Black pieces at top (minimum required)
        board[0][4] = new ChessPiece(ChessPiece.Type.KING, false);  // Black king at e8
        
        return board;
    }

    /**
     * Sets up a board where castling is invalid because the king is in check.
     */
    public static ChessPiece[][] setupKingInCheckScenario() {
        ChessPiece[][] board = new ChessPiece[8][8];
        
        // White pieces at bottom
        board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);  // White king at e1
        board[7][7] = new ChessPiece(ChessPiece.Type.ROOK, true);  // White rook at h1
        
        // Black pieces
        board[0][4] = new ChessPiece(ChessPiece.Type.KING, false); // Black king at e8
        board[5][4] = new ChessPiece(ChessPiece.Type.ROOK, false); // Black rook attacking e1
        
        return board;
    }

    /**
     * Sets up a board where castling is invalid because the king would pass through check.
     */
    public static ChessPiece[][] setupPassThroughCheckScenario() {
        ChessPiece[][] board = new ChessPiece[8][8];
        
        // White pieces at bottom
        board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);  // White king at e1
        board[7][7] = new ChessPiece(ChessPiece.Type.ROOK, true);  // White rook at h1
        
        // Black pieces
        board[0][4] = new ChessPiece(ChessPiece.Type.KING, false); // Black king at e8
        board[5][5] = new ChessPiece(ChessPiece.Type.ROOK, false); // Black rook attacking f1
        
        return board;
    }

    /**
     * Sets up a board where castling is invalid because the king would land in check.
     */
    public static ChessPiece[][] setupLandInCheckScenario() {
        ChessPiece[][] board = new ChessPiece[8][8];
        
        // White pieces at bottom
        board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);  // White king at e1
        board[7][7] = new ChessPiece(ChessPiece.Type.ROOK, true);  // White rook at h1
        
        // Black pieces
        board[0][4] = new ChessPiece(ChessPiece.Type.KING, false); // Black king at e8
        board[5][6] = new ChessPiece(ChessPiece.Type.ROOK, false); // Black rook at g3, attacking g1
        
        return board;
    }
} 