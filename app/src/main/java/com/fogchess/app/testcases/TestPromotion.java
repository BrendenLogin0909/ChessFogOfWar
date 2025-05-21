package com.fogchess.app.testcases;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.fogchess.app.R;
import com.fogchess.app.GameActivity;
import com.fogchess.app.ChessBoardView.ChessPiece;

/**
 * Test activity for pawn promotion.
 * This class provides test scenarios to demonstrate pawn promotion for both black and white pawns
 * on different colored squares and with different promotion choices.
 */
public class TestPromotion extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promotion_tests_menu);
        
        setupTestButtons();
    }

    private void setupTestButtons() {
        // White Pawn Promotion Scenarios
        setupButton(R.id.btn_white_promotion_light, "promotion_white_light");
        setupButton(R.id.btn_white_promotion_dark, "promotion_white_dark");
        
        // Black Pawn Promotion Scenarios
        setupButton(R.id.btn_black_promotion_light, "promotion_black_light");
        setupButton(R.id.btn_black_promotion_dark, "promotion_black_dark");
        
        // Edge Case Scenarios
        setupButton(R.id.btn_multiple_promotions, "promotion_multiple");
        setupButton(R.id.btn_promotion_with_capture, "promotion_with_capture");
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

    /**
     * Creates a board with a white pawn ready to promote on a light square (column 0, 2, 4, or 6)
     * @return The board with the pieces arranged for the test
     */
    public static void setupWhitePromotionLightSquare(ChessPiece[][] board) {
        // White pawn about to promote on a light square (e7)
        board[1][4] = new ChessPiece(ChessPiece.Type.PAWN, true);
        board[0][7] = new ChessPiece(ChessPiece.Type.KING, false);//black king at h8
    }
    
    /**
     * Creates a board with a white pawn ready to promote on a dark square (column 1, 3, 5, or 7)
     * @return The board with the pieces arranged for the test
     */
    public static void setupWhitePromotionDarkSquare(ChessPiece[][] board) {
        // White pawn about to promote on a dark square (d7)
        board[1][3] = new ChessPiece(ChessPiece.Type.PAWN, true);
        board[0][7] = new ChessPiece(ChessPiece.Type.KING, false);//black king at h8
    }
    
    /**
     * Creates a board with a black pawn ready to promote on a light square (column 0, 2, 4, or 6)
     * @return The board with the pieces arranged for the test
     */
    public static void setupBlackPromotionLightSquare(ChessPiece[][] board) {
        // Black pawn about to promote on a light square (e2)
        board[6][4] = new ChessPiece(ChessPiece.Type.PAWN, false);
        board[7][7] = new ChessPiece(ChessPiece.Type.KING, true);//white king at h1
    }
    
    /**
     * Creates a board with a black pawn ready to promote on a dark square (column 1, 3, 5, or 7)
     * @return The board with the pieces arranged for the test
     */
    public static void setupBlackPromotionDarkSquare(ChessPiece[][] board) {
        // Black pawn about to promote on a dark square (d2)
        board[6][3] = new ChessPiece(ChessPiece.Type.PAWN, false);
        board[7][7] = new ChessPiece(ChessPiece.Type.KING, true);//white king at h1
        }
    
    /**
     * Creates a board with multiple pawns in position to be promoted
     * @return The board with the pieces arranged for the test
     */
    public static void setupMultiplePromotions(ChessPiece[][] board) {
        // Multiple pawns ready for promotion
        board[1][3] = new ChessPiece(ChessPiece.Type.PAWN, true);
        board[1][4] = new ChessPiece(ChessPiece.Type.PAWN, true);
        board[6][3] = new ChessPiece(ChessPiece.Type.PAWN, false);
        board[6][4] = new ChessPiece(ChessPiece.Type.PAWN, false);
        board[0][7] = new ChessPiece(ChessPiece.Type.KING, false);//black king at h8
        board[7][7] = new ChessPiece(ChessPiece.Type.KING, true);//white king at h1
    }
    
    /**
     * Creates a board where a pawn can promote by capturing a piece
     * @return The board with the pieces arranged for the test
     */
    public static void setupPromotionWithCapture(ChessPiece[][] board) {
        // White pawn can promote by capturing
        board[1][3] = new ChessPiece(ChessPiece.Type.PAWN, true);
        board[0][4] = new ChessPiece(ChessPiece.Type.ROOK, false);
        board[0][7] = new ChessPiece(ChessPiece.Type.KING, false);//black king at h8
        board[7][7] = new ChessPiece(ChessPiece.Type.KING, true);//white king at h1
    }
} 