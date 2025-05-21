package com.fogchess.app.testcases;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.fogchess.app.R;
import com.fogchess.app.GameActivity;
import com.fogchess.app.ChessBoardView;
import com.fogchess.app.ChessBoardView.ChessPiece;

/**
 * Test activity for checkmate scenarios.
 * This class provides test scenarios to demonstrate different checkmate positions
 * and the checkmate user interface.
 */
public class TestCheckmate extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkmate_tests_menu);
        
        setupTestButtons();
    }

    private void setupTestButtons() {
        // Basic Checkmate Tests
        setupButton(R.id.btn_scholars_mate, "checkmate_scholars_mate");
        setupButton(R.id.btn_legals_mate, "checkmate_legals_mate");
        setupButton(R.id.btn_smothered_mate, "checkmate_smothered_mate");
        setupButton(R.id.btn_back_rank_mate, "checkmate_back_rank");
        
        // Advanced Checkmate Tests
        setupButton(R.id.btn_anastasia_mate, "checkmate_anastasia_mate");
        setupButton(R.id.btn_arabia_mate, "checkmate_arabia_mate");
        setupButton(R.id.btn_two_rooks_mate, "checkmate_two_rooks");
        setupButton(R.id.btn_two_queens_mate, "checkmate_two_queens");
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
     * Helper method to clear a chess board
     */
    private static void clearBoard(ChessPiece[][] board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }
    }

    /**
     * Test scenarios for various checkmate positions
     */
    public static class TestCheckmateScenarios {

        /**
         * Scholar's Mate (Four-Move Checkmate)
         * A quick checkmate that can be achieved in just four moves:
         * 1. e4 e5
         * 2. Qh5 Nc6
         * 3. Bc4 Nf6??
         * 4. Qxf7# 
         */
        public static ChessPiece[][] setupScholarsMateScenario() {
            ChessPiece[][] board = new ChessPiece[8][8];
            clearBoard(board);
            
            // Set up white pieces
            board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);  // White king on e1
            board[7][3] = new ChessPiece(ChessPiece.Type.QUEEN, true); // White queen on d1
            board[7][2] = new ChessPiece(ChessPiece.Type.BISHOP, true); // White bishop on c1
            board[6][4] = new ChessPiece(ChessPiece.Type.PAWN, true);  // White pawn on e2
            
            // Set up black pieces
            board[0][4] = new ChessPiece(ChessPiece.Type.KING, false); // Black king on e8
            board[1][4] = new ChessPiece(ChessPiece.Type.PAWN, false); // Black pawn on e7
            board[2][5] = new ChessPiece(ChessPiece.Type.KNIGHT, false); // Black knight on f6
            
            return board;
        }
        
        /**
         * Legal's Mate
         * A classic checkmate pattern:
         * 1. e4 e5
         * 2. Bc4 d6
         * 3. Qh5 Nf6??
         * 4. Qxf7#
         */
        public static ChessPiece[][] setupLegalsMateScenario() {
            ChessPiece[][] board = new ChessPiece[8][8];
            clearBoard(board);
            
            // Set up white pieces
            board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);  // White king on e1
            board[7][3] = new ChessPiece(ChessPiece.Type.QUEEN, true); // White queen on d1
            board[7][5] = new ChessPiece(ChessPiece.Type.BISHOP, true); // White bishop on f1
            board[6][4] = new ChessPiece(ChessPiece.Type.PAWN, true);  // White pawn on e2
            
            // Set up black pieces
            board[0][4] = new ChessPiece(ChessPiece.Type.KING, false); // Black king on e8
            board[1][4] = new ChessPiece(ChessPiece.Type.PAWN, false); // Black pawn on e7
            board[1][5] = new ChessPiece(ChessPiece.Type.PAWN, false); // Black pawn on f7
            
            return board;
        }
        
        /**
         * Smothered Mate
         * A checkmate delivered by a knight where the enemy king is "smothered" by its own pieces:
         * Final position shows a knight delivering mate to a king that cannot move because it's
         * surrounded by its own pieces.
         */
        public static ChessPiece[][] setupSmotheredMateScenario() {
            ChessPiece[][] board = new ChessPiece[8][8];
            clearBoard(board);
            
            // Set up white pieces
            board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);  // White king on e1
            board[2][6] = new ChessPiece(ChessPiece.Type.KNIGHT, true); // White knight on g6
            
            // Set up black pieces
            board[0][7] = new ChessPiece(ChessPiece.Type.KING, false); // Black king on h8
            board[1][6] = new ChessPiece(ChessPiece.Type.PAWN, false); // Black pawn on g7
            board[1][7] = new ChessPiece(ChessPiece.Type.PAWN, false); // Black pawn on h7
            board[0][6] = new ChessPiece(ChessPiece.Type.ROOK, false); // Black rook on g8
            
            return board;
        }
        
        /**
         * Back Rank Mate
         * A common checkmate pattern where the king is trapped on the back rank by its own pawns
         * and is checkmated by an enemy rook or queen.
         */
        public static ChessPiece[][] setupBackRankMateScenario() {
            ChessPiece[][] board = new ChessPiece[8][8];
            clearBoard(board);
            
            // Set up white pieces
            board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);  // White king on e1
            board[1][0] = new ChessPiece(ChessPiece.Type.ROOK, true);  // White rook on a7
            
            // Set up black pieces
            board[0][4] = new ChessPiece(ChessPiece.Type.KING, false); // Black king on e8
            board[1][3] = new ChessPiece(ChessPiece.Type.PAWN, false); // Black pawn on d7
            board[1][4] = new ChessPiece(ChessPiece.Type.PAWN, false); // Black pawn on e7
            board[1][5] = new ChessPiece(ChessPiece.Type.PAWN, false); // Black pawn on f7
            
            return board;
        }
        
        /**
         * Anastasia's Mate
         * A checkmate pattern where a knight and rook coordinate to trap the enemy king
         * with the help of a pawn or bishop to limit the king's escape squares.
         */
        public static ChessPiece[][] setupAnastasiaMateScenario() {
            ChessPiece[][] board = new ChessPiece[8][8];
            clearBoard(board);
            
            // Set up white pieces
            board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);  // White king on e1
            board[2][7] = new ChessPiece(ChessPiece.Type.ROOK, true);  // White rook on h6
            board[2][5] = new ChessPiece(ChessPiece.Type.KNIGHT, true); // White knight on f6
            
            // Set up black pieces
            board[0][7] = new ChessPiece(ChessPiece.Type.KING, false); // Black king on h8
            board[1][6] = new ChessPiece(ChessPiece.Type.PAWN, false); // Black pawn on g7
            board[1][7] = new ChessPiece(ChessPiece.Type.PAWN, false); // Black pawn on h7
            
            return board;
        }
        
        /**
         * Arabian Mate
         * A checkmate pattern where a rook and knight team up to trap the enemy king on the edge of the board.
         */
        public static ChessPiece[][] setupArabiaMateScenario() {
            ChessPiece[][] board = new ChessPiece[8][8];
            clearBoard(board);
            
            // Set up white pieces
            board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);  // White king on e1
            board[2][7] = new ChessPiece(ChessPiece.Type.ROOK, true);  // White rook on h6
            board[1][5] = new ChessPiece(ChessPiece.Type.KNIGHT, true); // White knight on f7
            
            // Set up black pieces
            board[0][7] = new ChessPiece(ChessPiece.Type.KING, false); // Black king on h8
            board[1][6] = new ChessPiece(ChessPiece.Type.PAWN, false); // Black pawn on g7
            
            return board;
        }
        
        /**
         * Two Rooks Mate
         * A simple but effective checkmate pattern with two rooks working together.
         */
        public static ChessPiece[][] setupTwoRooksMateScenario() {
            ChessPiece[][] board = new ChessPiece[8][8];
            clearBoard(board);
            
            // Set up white pieces
            board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);  // White king on e1
            board[1][0] = new ChessPiece(ChessPiece.Type.ROOK, true);  // White rook on a7
            board[1][1] = new ChessPiece(ChessPiece.Type.ROOK, true);  // White rook on b7
            
            // Set up black pieces
            board[0][4] = new ChessPiece(ChessPiece.Type.KING, false); // Black king on e8
            
            return board;
        }
        
        /**
         * Two Queens Mate
         * A powerful checkmate pattern using two queens to trap the enemy king.
         */
        public static ChessPiece[][] setupTwoQueensMateScenario() {
            ChessPiece[][] board = new ChessPiece[8][8];
            clearBoard(board);
            
            // Set up white pieces
            board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);  // White king on e1
            board[2][3] = new ChessPiece(ChessPiece.Type.QUEEN, true); // White queen on d6
            board[2][5] = new ChessPiece(ChessPiece.Type.QUEEN, true); // White queen on f6
            
            // Set up black pieces
            board[0][4] = new ChessPiece(ChessPiece.Type.KING, false); // Black king on e8
            
            return board;
        }
    }
} 