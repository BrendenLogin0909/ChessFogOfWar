package com.fogchess.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class GameActivity extends AppCompatActivity implements ChessBoardView.PawnPromotionListener {
    
    private TextView turnTextView;
    private ChessBoardView chessBoardView;
    // private Button nextTurnButton; //removed and replaced with blackout overlay automatically after 1 second
    private FrameLayout blackoutOverlay;
    private TextView passDeviceTextView;
    private TextView checkMessageTextView;
    
    // New elements for checkmate UI
    private LinearLayout checkmateContainer;
    private TextView congratsTextView;
    private Button newGameButton;
    
    private boolean isWhiteTurn = true;
    private Handler handler = new Handler(Looper.getMainLooper());
    private GameStateManager gameStateManager;
    
    // Dialog for pawn promotion
    private AlertDialog promotionDialog;
    
    private int[] currentEnPassantSquare = null;
    
    /**
     * Converts board coordinates to chess notation (e.g., "e4")
     * @param row The row number (0-7)
     * @param col The column number (0-7)
     * @return The chess notation for the square
     */
    private String toChessNotation(int row, int col) {
        char file = (char)('a' + col);
        int rank = 8 - row;
        return String.format("%c%d", file, rank);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        
        turnTextView = findViewById(R.id.turnTextView);
        chessBoardView = findViewById(R.id.chessBoardView);
        //nextTurnButton = findViewById(R.id.nextTurnButton);
        blackoutOverlay = findViewById(R.id.blackoutOverlay);
        passDeviceTextView = findViewById(R.id.passDeviceTextView);
        checkMessageTextView = findViewById(R.id.checkMessageTextView);
        
        // Initialize new checkmate UI elements
        checkmateContainer = findViewById(R.id.checkmateContainer);
        congratsTextView = findViewById(R.id.congratsTextView);
        newGameButton = findViewById(R.id.newGameButton);
        
        /* Commenting out Next Player button functionality
        nextTurnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gameStateManager.isGameOver()) {
                    showBlackoutScreen();
                }
            }
        });
        */
        
        blackoutOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchTurn();
            }
        });
        
        // Set up New Game button click listener
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeGame();
                hideCheckmateUI();
            }
        });
        
        // Set pawn promotion listener
        chessBoardView.setPawnPromotionListener(this);
        
        initializeGame();
    }
    
    private void initializeGame() {
        isWhiteTurn = true;
        updateTurnDisplay();
        
        // Hide checkmate UI if visible
        hideCheckmateUI();
        
        // Try to load a test scenario
        String testScenario = getIntent().getStringExtra("test_scenario");
        if (testScenario != null) {
            Log.i("TestScenario", "Test scenario loaded: " + testScenario);
            com.fogchess.app.testcases.TestScenarioManager.loadTestScenario(chessBoardView, testScenario);
        } else {
            // If no test scenario, initialize with the standard chess board
            chessBoardView.initializeBoard();
        }
        
        chessBoardView.updateVisibility(isWhiteTurn);
        
        // Initialize game state manager after board is set up
        gameStateManager = new GameStateManager(this, chessBoardView.getBoard());
        chessBoardView.setGameStateManager(gameStateManager);
    }
    
    private void loadTestScenario() {
        String testScenario = getIntent().getStringExtra("test_scenario");
        if (testScenario != null) {
            Log.i("TestScenario", "Test scenario loaded: " + testScenario);
            com.fogchess.app.testcases.TestScenarioManager.loadTestScenario(chessBoardView, testScenario);
        }
    }
    
    public void showBlackoutScreen() {
        // Check game state before showing blackout screen
        gameStateManager.setTurn(isWhiteTurn);
        gameStateManager.checkGameState();
        
        if (gameStateManager.isGameOver()) {
            showGameOverDialog(gameStateManager.getGameOverMessage());
            return;
        }
        
        // Store the en passant state before switching turns
        currentEnPassantSquare = chessBoardView.getEnPassantSquare();
        if (currentEnPassantSquare != null) {
            Log.d("TurnTransition", "Storing en passant square at " + 
                              toChessNotation(currentEnPassantSquare[0], currentEnPassantSquare[1]));
        } else {
            Log.d("TurnTransition", "No en passant square to store");
        }
        
        // Update text before showing the blackout screen
        passDeviceTextView.setText(getString(R.string.pass_device) + 
                                  (isWhiteTurn ? " (Black's turn next)" : " (White's turn next)"));
        
        // IMPORTANT: Do NOT update the board visibility yet
        // Ensure the blackout screen is completely visible first
        
        // Make sure the blackout overlay is black and completely opaque when we start
        blackoutOverlay.setAlpha(0f);
        blackoutOverlay.setVisibility(View.VISIBLE);

        // Create a fade in animation
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(blackoutOverlay, "alpha", 0f, 1f);
        fadeIn.setDuration(500);
        fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
        fadeIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // IMPORTANT: Now that blackout is complete, we can safely update the board
                // The screen is fully black/opaque, so the user can't see any changes
                
                // Switch turn
                isWhiteTurn = !isWhiteTurn;
                updateTurnDisplay();
                
                // For en passant to work, we need to restore the state BEFORE updating visibility
                if (currentEnPassantSquare != null) {
                    Log.d("EnPassant", "After turn switch: restoring square at " + 
                                     toChessNotation(currentEnPassantSquare[0], currentEnPassantSquare[1]));
                    chessBoardView.setEnPassantSquare(currentEnPassantSquare[0], currentEnPassantSquare[1]);
                }
                
                // Now update visibility with the restored en passant square
                chessBoardView.updateVisibility(isWhiteTurn);
                
                // Make sure our changes are applied before the fade out can start
                chessBoardView.invalidate();
            }
        });
        fadeIn.start();
    }
    
    private void switchTurn() {
        // Now that the board has been updated for the new player behind the blackout screen,
        // we can remove the blackout to reveal the new board state
        
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(blackoutOverlay, "alpha", 1f, 0f);
        fadeOut.setDuration(500);
        fadeOut.setInterpolator(new AccelerateDecelerateInterpolator());
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                blackoutOverlay.setVisibility(View.GONE);
            }
        });
        fadeOut.start();
    }
    
    private void updateTurnDisplay() {
        turnTextView.setText(isWhiteTurn ? R.string.white_turn : R.string.black_turn);
    }
    
    private void showGameOverDialog(String message) {
        // If it's a checkmate, show in-game UI instead of dialog
        if (message.equals(getString(R.string.white_wins)) || 
            message.equals(getString(R.string.black_wins))) {
            showCheckmateUI(!isWhiteTurn); // Winner is opposite of current turn
            return;
        }
        
        // For other game end conditions (stalemate, etc), show dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.game_over));
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.new_game), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initializeGame();
            }
        });
        builder.setNegativeButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
    
    /**
     * Show the checkmate UI with appropriate congratulations message
     * @param isWhiteWinner true if White won, false if Black won
     */
    private void showCheckmateUI(boolean isWhiteWinner) {
        // Set appropriate congratulatory message
        congratsTextView.setText(isWhiteWinner ? 
            getString(R.string.congrats_white) : 
            getString(R.string.congrats_black));
        
        // Show the checkmate UI
        checkmateContainer.setVisibility(View.VISIBLE);
    }
    
    /**
     * Hide the checkmate UI
     */
    private void hideCheckmateUI() {
        checkmateContainer.setVisibility(View.GONE);
    }
    
    public void onMoveCompleted() {
        // Schedule automatic turn switch after delay
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!gameStateManager.isGameOver()) {
                    showBlackoutScreen();
                }
            }
        }, 1000); // 1 second delay
    }
    
    // PawnPromotionListener implementation
    @Override
    public void onPawnReachedPromotion(int row, int col, boolean isWhitePawn) {
        // Create and show the promotion dialog
        showPromotionDialog(isWhitePawn);
    }
    
    @Override
    public void onPromotionCompleted() {
        // Turn will be completed by ChessBoardView.promotePawn() which will call gameStateManager.onMoveMade()
    }
    
    private void showPromotionDialog(boolean isWhitePawn) {
        // Inflate the promotion dialog layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.pawn_promotion_dialog, null);
        
        // Get the piece containers
        LinearLayout queenOption = dialogView.findViewById(R.id.queenOption);
        LinearLayout rookOption = dialogView.findViewById(R.id.rookOption);
        LinearLayout bishopOption = dialogView.findViewById(R.id.bishopOption);
        LinearLayout knightOption = dialogView.findViewById(R.id.knightOption);
        
        // Get the piece text views
        TextView queenSymbol = dialogView.findViewById(R.id.queenSymbol);
        TextView rookSymbol = dialogView.findViewById(R.id.rookSymbol);
        TextView bishopSymbol = dialogView.findViewById(R.id.bishopSymbol);
        TextView knightSymbol = dialogView.findViewById(R.id.knightSymbol);
        
        // Update piece colors based on pawn color
        int pieceColor = isWhitePawn ? R.color.white_piece : R.color.black_piece;
        queenSymbol.setTextColor(getResources().getColor(pieceColor));
        rookSymbol.setTextColor(getResources().getColor(pieceColor));
        bishopSymbol.setTextColor(getResources().getColor(pieceColor));
        knightSymbol.setTextColor(getResources().getColor(pieceColor));
        
        // Add black outline to white pieces only
        if (isWhitePawn) {
            // Add shadow outline to white pieces
            setShadowForTextView(queenSymbol);
            setShadowForTextView(rookSymbol);
            setShadowForTextView(bishopSymbol);
            setShadowForTextView(knightSymbol);
        } else {
            // Clear shadow for black pieces
            clearShadowForTextView(queenSymbol);
            clearShadowForTextView(rookSymbol);
            clearShadowForTextView(bishopSymbol);
            clearShadowForTextView(knightSymbol);
        }
        
        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(false); // Force the player to make a selection
        
        // Show the dialog
        promotionDialog = builder.create();
        promotionDialog.show();
        
        // Set click listeners for each piece option
        queenOption.setOnClickListener(v -> {
            chessBoardView.promotePawn(ChessBoardView.ChessPiece.Type.QUEEN);
            promotionDialog.dismiss();
        });
        
        rookOption.setOnClickListener(v -> {
            chessBoardView.promotePawn(ChessBoardView.ChessPiece.Type.ROOK);
            promotionDialog.dismiss();
        });
        
        bishopOption.setOnClickListener(v -> {
            chessBoardView.promotePawn(ChessBoardView.ChessPiece.Type.BISHOP);
            promotionDialog.dismiss();
        });
        
        knightOption.setOnClickListener(v -> {
            chessBoardView.promotePawn(ChessBoardView.ChessPiece.Type.KNIGHT);
            promotionDialog.dismiss();
        });
    }
    
    // Helper method to add shadow to a TextView
    private void setShadowForTextView(TextView textView) {
        textView.setShadowLayer(2, 1, 1, getResources().getColor(R.color.black_piece));
    }
    
    // Helper method to clear shadow from a TextView
    private void clearShadowForTextView(TextView textView) {
        textView.setShadowLayer(0, 0, 0, 0);
    }
}
