package com.fogchess.app.testcases;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import com.fogchess.app.R;
import com.fogchess.app.GameActivity;
import com.fogchess.app.ChessBoardView.ChessPiece;

/**
 * Test activity for en passant captures and visualization.
 * This class provides test scenarios to demonstrate the en passant rule
 * and the visual indicators for pawns that have moved two squares.
 */
public class TestEnPassant extends AppCompatActivity {
    
    private static final String TAG = "TestEnPassant";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.en_passant_tests_menu);
        
        setupTestButtons();
    }

    private void setupTestButtons() {
        // Valid En Passant Scenarios
        setupButton(R.id.whiteEnPassantCaptureButton, "en_passant_white");
        setupButton(R.id.blackEnPassantCaptureButton, "en_passant_black");
        
        // Visual Indicator Tests
        setupButton(R.id.pawnsCanSeeButton, "en_passant_pawns_can_see");
        setupButton(R.id.otherPiecesCanSeeButton, "en_passant_other_pieces_can_see");
        setupButton(R.id.combinedVisibilityButton, "en_passant_combined_visibility");
    }

    private void setupButton(int buttonId, String scenario) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            Log.d(TAG, "Starting test scenario: " + scenario);
            startGameActivity(scenario);
        });
    }

    private void startGameActivity(String testScenario) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("test_scenario", testScenario);
        startActivity(intent);
    }

    /**
     * Sets up a board to test a white pawn capturing a black pawn via en passant.
     * Initial position has a black pawn at its starting position (row 1, col 3),
     * which will move two squares forward on the first move. A white pawn is positioned
     * adjacent (row 3, col 2) to capture via en passant on the next move.
     * This setup demonstrates the en passant transition during actual gameplay.
     */
    public static ChessPiece[][] setupWhiteEnPassantCaptureScenario() {
        ChessPiece[][] board = new ChessPiece[8][8];
        
        // White pawn at row 3, col 2 (adjacent to where black pawn will move)
        board[3][2] = new ChessPiece(ChessPiece.Type.PAWN, true);
        
        // Black pawn at starting position (will move two squares)
        board[1][3] = new ChessPiece(ChessPiece.Type.PAWN, false);
        
        // Both kings for valid board
        board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);  // White king at e1
        board[0][4] = new ChessPiece(ChessPiece.Type.KING, false); // Black king at e8
        
        return board;
    }

    /**
     * Sets up a board to test a black pawn capturing a white pawn via en passant.
     * Initial position has a white pawn at its starting position (row 6, col 3),
     * which will move two squares forward on the first move. A black pawn is positioned
     * adjacent (row 4, col 4) to capture via en passant on the next move.
     * This setup demonstrates the en passant transition during actual gameplay.
     */
    public static ChessPiece[][] setupBlackEnPassantCaptureScenario() {
        ChessPiece[][] board = new ChessPiece[8][8];
        
        // Black pawn at row 4, col 4 (adjacent to where white pawn will move)
        board[4][4] = new ChessPiece(ChessPiece.Type.PAWN, false);
        
        // White pawn at starting position (will move two squares)
        board[6][3] = new ChessPiece(ChessPiece.Type.PAWN, true);
        
        // Both kings for valid board
        board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);  // White king at e1
        board[0][4] = new ChessPiece(ChessPiece.Type.KING, false); // Black king at e8
        
        return board;
    }

    /**
     * Sets up a board where white pawns can see the en passant square after black moves.
     * A black pawn starts at its initial position and will move two squares on the first move.
     * White pawns are positioned on either side to demonstrate the visual indicator
     * that appears when a pawn can make an en passant capture.
     */
    public static ChessPiece[][] setupPawnsCanSeeScenario() {
        ChessPiece[][] board = new ChessPiece[8][8];
        
        // White pawns on 5th rank adjacent to where black pawn will move
        board[3][2] = new ChessPiece(ChessPiece.Type.PAWN, true);
        board[3][4] = new ChessPiece(ChessPiece.Type.PAWN, true);
        
        // Black pawn at starting position (will move two squares)
        board[1][3] = new ChessPiece(ChessPiece.Type.PAWN, false);
        
        // Both kings for valid board
        board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);
        board[0][4] = new ChessPiece(ChessPiece.Type.KING, false);
        
        return board;
    }

    /**
     * Sets up a board where other pieces can see the en passant square but no pawns can capture.
     * A black pawn starts at its initial position and will move two squares on the first move.
     * A white bishop is positioned diagonally to see the resulting square, but cannot
     * make an en passant capture (only pawns can). Tests that the visual indicator
     * is NOT shown for non-pawn pieces.
     */
    public static ChessPiece[][] setupOtherPiecesCanSeeScenario() {
        ChessPiece[][] board = new ChessPiece[8][8];
        
        // White bishop that can see the en passant square but can't capture
        board[5][0] = new ChessPiece(ChessPiece.Type.BISHOP, true);
        
        // Black pawn at starting position (will move two squares)
        board[1][3] = new ChessPiece(ChessPiece.Type.PAWN, false);
        
        // Both kings for valid board
        board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);
        board[0][4] = new ChessPiece(ChessPiece.Type.KING, false);
        
        return board;
    }

    /**
     * Sets up a board with both pawns that can capture and other pieces that can see.
     * A black pawn starts at its initial position and will move two squares on the first move.
     * A white pawn is positioned to make an en passant capture, while a bishop and knight
     * can see the square but cannot capture. Tests that the visual indicator appears only
     * for the pawn that can make the en passant capture.
     */
    public static ChessPiece[][] setupCombinedVisibilityScenario() {
        ChessPiece[][] board = new ChessPiece[8][8];
        
        // White pawn that can make the en passant capture
        board[3][2] = new ChessPiece(ChessPiece.Type.PAWN, true);
        
        // White bishop that can see the en passant square but can't capture
        board[5][6] = new ChessPiece(ChessPiece.Type.BISHOP, true);
        
        // White knight that can see the en passant square but can't capture
        board[5][5] = new ChessPiece(ChessPiece.Type.KNIGHT, true);
        
        // Black pawn at starting position (will move two squares)
        board[1][3] = new ChessPiece(ChessPiece.Type.PAWN, false);
        
        // Both kings for valid board
        board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);
        board[0][4] = new ChessPiece(ChessPiece.Type.KING, false);
        
        return board;
    }
} 