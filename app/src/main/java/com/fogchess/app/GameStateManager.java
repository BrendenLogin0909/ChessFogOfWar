package com.fogchess.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.os.Handler;
import android.os.Looper;

import java.util.List;

public class GameStateManager {
    private ChessBoardView.ChessPiece[][] board;
    private boolean isWhiteTurn;
    private Context context;
    private boolean gameOver = false;
    private String gameOverMessage = "";
    private GameActivity gameActivity;
    private Handler handler = new Handler(Looper.getMainLooper());

    public GameStateManager(Context context, ChessBoardView.ChessPiece[][] board) {
        this.context = context;
        this.board = board;
        this.isWhiteTurn = true;
        if (context instanceof GameActivity) {
            this.gameActivity = (GameActivity) context;
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getGameOverMessage() {
        return gameOverMessage;
    }

    public void checkGameState() {
        // Check for checkmate or stalemate
        if (isCheckmate(isWhiteTurn)) {
            gameOver = true;
            gameOverMessage = isWhiteTurn ? 
                context.getString(R.string.black_wins) : 
                context.getString(R.string.white_wins);
            Toast.makeText(context, context.getString(R.string.checkmate), Toast.LENGTH_LONG).show();
        } else if (isStalemate(isWhiteTurn)) {
            gameOver = true;
            gameOverMessage = context.getString(R.string.draw);
            Toast.makeText(context, context.getString(R.string.stalemate), Toast.LENGTH_LONG).show();
        } else if (ChessRules.isInCheck(board, isWhiteTurn)) {
            // Just notify about check
            Toast.makeText(context, "Check!", Toast.LENGTH_SHORT).show();
        }
    }
    public void onMoveMade() {
        // Check game state first
        checkGameState();
        
        // Schedule automatic turn switch after delay
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!gameOver && gameActivity != null) {
                    // Show blackout screen which will handle turn switching properly
                    gameActivity.showBlackoutScreen();
                }
            }
        }, 1000); // 1 second delay
    }

    private boolean isCheckmate(boolean isWhiteKing) {
        // If not in check, can't be checkmate
        if (!ChessRules.isInCheck(board, isWhiteKing)) {
            return false;
        }

        // Check if any move can get out of check
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessBoardView.ChessPiece piece = board[row][col];
                if (piece != null && piece.isWhite == isWhiteKing) {
                    List<int[]> moves = ChessRules.getValidMoves(board, row, col);
                    for (int[] move : moves) {
                        // Try the move
                        ChessBoardView.ChessPiece capturedPiece = board[move[0]][move[1]];
                        board[move[0]][move[1]] = piece;
                        board[row][col] = null;

                        // Check if still in check
                        boolean stillInCheck = ChessRules.isInCheck(board, isWhiteKing);

                        // Undo the move
                        board[row][col] = piece;
                        board[move[0]][move[1]] = capturedPiece;

                        // If any move gets out of check, it's not checkmate
                        if (!stillInCheck) {
                            return false;
                        }
                    }
                }
            }
        }

        // If no move can get out of check, it's checkmate
        return true;
    }

    private boolean isStalemate(boolean isWhiteKing) {
        // If in check, it's not stalemate
        if (ChessRules.isInCheck(board, isWhiteKing)) {
            return false;
        }

        // Check if any legal move exists
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessBoardView.ChessPiece piece = board[row][col];
                if (piece != null && piece.isWhite == isWhiteKing) {
                    List<int[]> moves = ChessRules.getValidMoves(board, row, col);
                    if (!moves.isEmpty()) {
                        return false;
                    }
                }
            }
        }

        // If no legal move exists and not in check, it's stalemate
        return true;
    }

    public void setTurn(boolean isWhiteTurn) {
        this.isWhiteTurn = isWhiteTurn;
    }

    public void showAlert(String message) {
        if (context != null) {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
