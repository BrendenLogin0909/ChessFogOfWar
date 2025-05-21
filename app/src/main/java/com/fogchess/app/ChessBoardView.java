package com.fogchess.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.List;
import java.util.ArrayList;
import android.graphics.Color;


public class ChessBoardView extends View {
    
    private static final int BOARD_SIZE = 8;
    
    private Paint whitePaint;
    private Paint blackPaint;
    private Paint whitePiecePaint;
    private Paint blackPiecePaint;
    private Paint highlightPaint;
    private Paint fogPaint;
    private Paint ghostPawnPaint; // New paint for the ghost pawn indicator
    private Paint notationPaint; // New paint for chess notation
    
    private float cellSize;
    private float borderSize = 40; // Size of the border area for notation
    private ChessPiece[][] board;
    private boolean[][] visibleCells;
    
    private ChessPiece selectedPiece;
    private int selectedRow = -1;
    private int selectedCol = -1;
    
    private boolean isWhiteTurn = true;
    private boolean boardFlipped = false; // True when board is flipped (black's perspective)

    // Add a new field to track promotion state
    private boolean waitingForPromotion = false;
    private int promotionRow = -1;
    private int promotionCol = -1;

    // Add an interface for promotion callbacks
    public interface PawnPromotionListener {
        void onPawnReachedPromotion(int row, int col, boolean isWhitePawn);
        void onPromotionCompleted();
    }

    private PawnPromotionListener promotionListener;

    private int[] selectedSquare = null;
    private int[] lastMoveStart = null;
    private int[] lastMoveEnd = null;
    private boolean isGameOver = false;

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

    public ChessBoardView(Context context) {
        super(context);
        init();
    }
    
    public ChessBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public ChessBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        whitePaint = new Paint();
        whitePaint.setColor(getResources().getColor(R.color.white_square));
        
        blackPaint = new Paint();
        blackPaint.setColor(getResources().getColor(R.color.black_square));
        
        whitePiecePaint = new Paint();
        whitePiecePaint.setColor(getResources().getColor(R.color.white_piece));
        whitePiecePaint.setTextSize(80);
        whitePiecePaint.setTextAlign(Paint.Align.CENTER);
        
        blackPiecePaint = new Paint();
        blackPiecePaint.setColor(getResources().getColor(R.color.black_piece));
        blackPiecePaint.setTextSize(80);
        blackPiecePaint.setTextAlign(Paint.Align.CENTER);
        
        highlightPaint = new Paint();
        highlightPaint.setColor(getResources().getColor(R.color.highlight));
        highlightPaint.setStyle(Paint.Style.FILL);
        
        fogPaint = new Paint();
        fogPaint.setColor(getResources().getColor(R.color.fog));
        fogPaint.setStyle(Paint.Style.FILL);
        
        // Initialize ghost pawn paint with improved styling
        ghostPawnPaint = new Paint();
        ghostPawnPaint.setColor(getResources().getColor(R.color.black_piece));
        ghostPawnPaint.setTextSize(60); // Slightly smaller than regular pieces
        ghostPawnPaint.setTextAlign(Paint.Align.CENTER);
        ghostPawnPaint.setAlpha(130); // More translucent (50% opacity)
        ghostPawnPaint.setStrokeWidth(2);
        
        // Initialize notation paint
        notationPaint = new Paint();
        notationPaint.setColor(getResources().getColor(R.color.black_piece));
        notationPaint.setTextSize(30);
        notationPaint.setTextAlign(Paint.Align.CENTER);
        notationPaint.setAntiAlias(true);
        
        board = new ChessPiece[BOARD_SIZE][BOARD_SIZE];
        visibleCells = new boolean[BOARD_SIZE][BOARD_SIZE];
    }
    
    public void initializeBoard() {
        // Initialize the chess board with pieces in starting positions
        // Back row pieces
        board[0][0] = new ChessPiece(ChessPiece.Type.ROOK, false);
        board[0][1] = new ChessPiece(ChessPiece.Type.KNIGHT, false);
        board[0][2] = new ChessPiece(ChessPiece.Type.BISHOP, false);
        board[0][3] = new ChessPiece(ChessPiece.Type.QUEEN, false);
        board[0][4] = new ChessPiece(ChessPiece.Type.KING, false);
        board[0][5] = new ChessPiece(ChessPiece.Type.BISHOP, false);
        board[0][6] = new ChessPiece(ChessPiece.Type.KNIGHT, false);
        board[0][7] = new ChessPiece(ChessPiece.Type.ROOK, false);
        
        // Black pawns
        for (int col = 0; col < BOARD_SIZE; col++) {
            board[1][col] = new ChessPiece(ChessPiece.Type.PAWN, false);
        }
        
        // Empty middle rows
        for (int row = 2; row < 6; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = null;
            }
        }
        
        // White pawns
        for (int col = 0; col < BOARD_SIZE; col++) {
            board[6][col] = new ChessPiece(ChessPiece.Type.PAWN, true);
        }
        
        // White back row
        board[7][0] = new ChessPiece(ChessPiece.Type.ROOK, true);
        board[7][1] = new ChessPiece(ChessPiece.Type.KNIGHT, true);
        board[7][2] = new ChessPiece(ChessPiece.Type.BISHOP, true);
        board[7][3] = new ChessPiece(ChessPiece.Type.QUEEN, true);
        board[7][4] = new ChessPiece(ChessPiece.Type.KING, true);
        board[7][5] = new ChessPiece(ChessPiece.Type.BISHOP, true);
        board[7][6] = new ChessPiece(ChessPiece.Type.KNIGHT, true);
        board[7][7] = new ChessPiece(ChessPiece.Type.ROOK, true);
        
        // Initialize visibility based on current turn
        updateVisibility(isWhiteTurn);
        
        invalidate();
    }
    
    public void updateVisibility(boolean isWhiteTurn) {
        this.isWhiteTurn = isWhiteTurn;
        this.boardFlipped = !isWhiteTurn; // Flip board when it's black's turn

        // Reset visibility
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                visibleCells[row][col] = false;
            }
        }
        
        // Calculate visibility based on current player's pieces
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                ChessPiece piece = board[row][col];
                if (piece != null && piece.isWhite == isWhiteTurn) {
                    // Always make the cell with player's own piece visible
                    visibleCells[row][col] = true;
                    
                    // Get all cells that this piece can see
                    List<int[]> visibleFromPiece = ChessRules.getVisibleCells(board, row, col);
                    for (int[] cell : visibleFromPiece) {
                        visibleCells[cell[0]][cell[1]] = true;
                    }
                }
            }
        }
        
        // Just for debugging
        if (ChessRules.getEnPassantSquare() != null) {
            System.out.println("[EnPassant] After visibility update: square at " + 
                              toChessNotation(ChessRules.getEnPassantSquare()[0], ChessRules.getEnPassantSquare()[1]));
        }
        
        invalidate();
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Adjust cell size to account for borders
        cellSize = Math.min(w - 2 * borderSize, h - 2 * borderSize) / (float) BOARD_SIZE;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        // Calculate the starting position of the board to center it
        float startX = (getWidth() - (BOARD_SIZE * cellSize)) / 2;
        float startY = (getHeight() - (BOARD_SIZE * cellSize)) / 2;
        
        // Draw the border background
        Paint borderPaint = new Paint();
        borderPaint.setColor(getResources().getColor(R.color.white_square));
        canvas.drawRect(0, 0, getWidth(), getHeight(), borderPaint);
        
        // Draw the chess board
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                // Convert logical board position to screen position
                int[] screenPos = boardToScreenCoordinates(row, col);
                float left = startX + screenPos[1] * cellSize;
                float top = startY + screenPos[0] * cellSize;
                float right = left + cellSize;
                float bottom = top + cellSize;
                
                // Draw square
                Paint squarePaint = (row + col) % 2 == 0 ? whitePaint : blackPaint;
                canvas.drawRect(left, top, right, bottom, squarePaint);
                
                // Draw pieces and other board elements
                ChessPiece piece = board[row][col];
                if (piece != null) {
                    Paint piecePaint = piece.isWhite ? whitePiecePaint : blackPiecePaint;
                    if (piece.isWhite == isWhiteTurn || visibleCells[row][col]) {
                        String symbol = piece.getSymbol();
                        float x = left + cellSize / 2;
                        float y = top + cellSize / 2 + 25;
                        canvas.drawText(symbol, x, y, piecePaint);
                    }
                }
                
                // Draw selected square highlight
                if (row == selectedRow && col == selectedCol) {
                    canvas.drawRect(left, top, right, bottom, highlightPaint);
                }
                
                // Draw en passant indicator if applicable
                if (shouldShowEnPassantIndicator(row, col)) {
                    float x = left + cellSize / 2;
                    float y = top + cellSize / 2 + 25;
                    // The ghost pawn should be the color of the pawn that moved two squares
                    boolean ghostPawnIsWhite = ChessRules.isEnPassantWhitePawn();
                    ghostPawnPaint.setColor(getResources().getColor(
                        ghostPawnIsWhite ? R.color.white_piece : R.color.black_piece));
                    ghostPawnPaint.setAlpha(180);
                    canvas.drawText("♟", x, y, ghostPawnPaint);
                    
                    // Draw directional arrows to show movement
                    Paint arrowPaint = new Paint(ghostPawnPaint);
                    arrowPaint.setTextSize(40); // Smaller for the arrow
                    
                    // Direction depends on pawn color - white pawns move up, black pawns move down
                    String directionArrow = ghostPawnIsWhite ? "↑" : "↓";
                    
                    // Draw arrows on both sides of the pawn
                    canvas.drawText(directionArrow, x - cellSize/4, y - 15, arrowPaint);
                    canvas.drawText(directionArrow, x + cellSize/4, y - 15, arrowPaint);
                }
                
                // Draw fog if applicable
                if (!visibleCells[row][col] && (piece == null || piece.isWhite != isWhiteTurn)) {
                    canvas.drawRect(left, top, right, bottom, fogPaint);
                }
            }
        }
        
        // Draw notation outside the board
        notationPaint.setTextAlign(Paint.Align.CENTER);
        
        // Draw column letters (a-h)
        for (int col = 0; col < BOARD_SIZE; col++) {
            String letter = boardFlipped ? String.valueOf((char)('H' - col)) : String.valueOf((char)('A' + col));
            // Draw letters at bottom
            float x = startX + col * cellSize + cellSize / 2;
            float y = startY + BOARD_SIZE * cellSize + borderSize * 0.7f;
            canvas.drawText(letter, x, y, notationPaint);
            // Draw letters at top
            canvas.drawText(letter, x, startY - borderSize * 0.3f, notationPaint);
        }
        
        // Draw row numbers (1-8)
        notationPaint.setTextAlign(Paint.Align.RIGHT);
        for (int row = 0; row < BOARD_SIZE; row++) {
            String number = boardFlipped ? String.valueOf(row + 1) : String.valueOf(8 - row);
            // Draw numbers on left
            float x = startX - borderSize * 0.3f;
            float y = startY + row * cellSize + cellSize * 0.6f;
            canvas.drawText(number, x, y, notationPaint);
            // Draw numbers on right
            x = startX + BOARD_SIZE * cellSize + borderSize * 0.7f;
            canvas.drawText(number, x, y, notationPaint);
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            
            int screenCol = (int) (x / cellSize);
            int screenRow = (int) (y / cellSize);
            
            // Convert screen coordinates to logical board coordinates
            int[] boardCoords = screenToBoardCoordinates(screenRow, screenCol);
            int row = boardCoords[0];
            int col = boardCoords[1];
            
            // Ensure coordinates are within bounds
            if (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE) {
                handleCellClick(row, col);
                return true;
            }
        }
        return super.onTouchEvent(event);
    }
    
    private GameStateManager gameStateManager;
    
    public void setGameStateManager(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }
    
    public ChessPiece[][] getBoard() {
        return board;
    }
    
    public void setFlipped(boolean flipped) {
        this.boardFlipped = flipped;
        invalidate();
    }
    
    public boolean isFlipped() {
        return boardFlipped;
    }
    
    // Convert from logical board coordinates to screen coordinates
    private int[] boardToScreenCoordinates(int row, int col) {
        if (boardFlipped) {
            // When flipped, transform coordinates
            return new int[]{7 - row, 7 - col};
        } else {
            // When not flipped, use coordinates as-is
            return new int[]{row, col};
        }
    }
    
    // Convert from screen coordinates to logical board coordinates
    private int[] screenToBoardCoordinates(int row, int col) {
        if (boardFlipped) {
            // When flipped, transform back
            return new int[]{7 - row, 7 - col};
        } else {
            // When not flipped, just use the coordinates as is
            return new int[]{row, col};
        }
    }

    private boolean wouldPutKingInCheck(int fromRow, int fromCol, int toRow, int toCol) {
        // Store the current state
        ChessPiece movingPiece = board[fromRow][fromCol];
        ChessPiece capturedPiece = board[toRow][toCol];
        
        // Simulate the move
        board[toRow][toCol] = movingPiece;
        board[fromRow][fromCol] = null;
        
        // Check if the king is in check after the move
        boolean inCheck = ChessRules.isInCheck(board, isWhiteTurn);
        
        // Restore the board state
        board[fromRow][fromCol] = movingPiece;
        board[toRow][toCol] = capturedPiece;
        
        return inCheck;
    }
    
    /**
     * Clears any en passant opportunities after a player's full turn.
     * This should be called after a player completes a move that isn't
     * an en passant capture.
     */
    public void clearEnPassantAfterTurn() {
        if (ChessRules.getEnPassantSquare() != null) {
            System.out.println("[EnPassant] Clearing opportunity at " + 
                              toChessNotation(ChessRules.getEnPassantSquare()[0], ChessRules.getEnPassantSquare()[1]));
            ChessRules.clearEnPassant();
            invalidate();
        }
    }
    
    // Update the handleCellClick method to check for pawn promotion
    private void handleCellClick(int row, int col) {
        if (waitingForPromotion) {
            // When waiting for promotion, ignore board clicks
            return;
        }
                
        ChessPiece clickedPiece = board[row][col];
        
        if (selectedPiece != null) {
            // If already selected a piece, try to move it
            if (selectedRow == row && selectedCol == col) {
                // Clicking on the same piece deselects it
                selectedPiece = null;
                selectedRow = -1;
                selectedCol = -1;
            } else {
                // Check if this is a castling move
                if (selectedPiece.type == ChessPiece.Type.KING && Math.abs(col - selectedCol) == 2) {
                    // Check if king is in its original position
                    int originalKingRow = isWhiteTurn ? 7 : 0;
                    int originalKingCol = 4;
                    if (selectedRow != originalKingRow || selectedCol != originalKingCol) {
                        if (gameStateManager != null) {
                        //    gameStateManager.showAlert("Cannot castle - king must be in its original position");
                        }
                        selectedPiece = null;
                        selectedRow = -1;
                        selectedCol = -1;
                        invalidate();
                        return;
                    }

                    String castleResult = ChessRules.canCastle(board, selectedRow, selectedCol, col > selectedCol, isWhiteTurn);
                    if (!castleResult.equals("OK")) {
                        if (gameStateManager != null) {
                            gameStateManager.showAlert(castleResult);
                        }
                        selectedPiece = null;
                        selectedRow = -1;
                        selectedCol = -1;
                        invalidate();
                        return;
                    }
                }
                
                // Check if this is a valid move
                boolean isValidMove = ChessRules.isValidMove(
                    board, selectedRow, selectedCol, row, col, 
                    isWhiteTurn, boardFlipped, 
                    false, ChessRules.getEnPassantSquare());
                
                if (isValidMove) {
                    // Check if the move would put or leave own king in check
                    if (wouldPutKingInCheck(selectedRow, selectedCol, row, col)) {
                        // Show alert that the move would put king in check
                        if (gameStateManager != null) {
                            gameStateManager.showAlert("Can't put your King into Check");
                        }
                        // Deselect the piece
                        selectedPiece = null;
                        selectedRow = -1;
                        selectedCol = -1;
                        invalidate();
                        return;
                    }
                    
                    // Check if this is an en passant capture
                    boolean isEnPassantCapture = false;
                    if (selectedPiece.type == ChessPiece.Type.PAWN && 
                        ChessRules.isEnPassantActive() && 
                        row == ChessRules.getEnPassantSquare()[0] && 
                        col == ChessRules.getEnPassantSquare()[1] && 
                        col != selectedCol) { // Diagonal move to en passant square
                        isEnPassantCapture = true;
                    }
                    
                    // Capture or move
                    board[row][col] = selectedPiece;
                    board[selectedRow][selectedCol] = null;

                    // Mark the piece as moved
                    selectedPiece.hasMoved = true;

                    // Handle castling (special move)
                    if (selectedPiece.type == ChessPiece.Type.KING && Math.abs(col - selectedCol) == 2) {
                        // Castling - move the rook too
                        int rookFromCol = (col > selectedCol) ? 7 : 0; // 7 for kingside, 0 for queenside
                        int rookToCol = (col > selectedCol) ? col - 1 : col + 1; // Place rook next to king
                        
                        // Move the rook
                        ChessPiece rook = board[row][rookFromCol];
                        board[row][rookToCol] = rook;
                        board[row][rookFromCol] = null;
                        
                        // Mark rook as moved
                        if (rook != null) {
                            rook.hasMoved = true;
                        }
                    }
                    
                    // Handle en passant capture
                    if (isEnPassantCapture) {
                        // Remove the captured pawn
                        int capturedPawnRow = selectedPiece.isWhite ? row + 1 : row - 1;
                        board[capturedPawnRow][col] = null;
                        
                        // Clear en passant state
                        ChessRules.clearEnPassant();
                    } else {
                        // Update en passant state for pawn moving two squares
                        updateEnPassantAfterMove(selectedRow, selectedCol, row, col);
                    }

                    // Check for pawn promotion
                    if (board[row][col].type == ChessPiece.Type.PAWN) {
                        boolean reachedPromotion = (isWhiteTurn && row == 0) || (!isWhiteTurn && row == 7);
                        if (reachedPromotion) {
                            // Set promotion state
                            waitingForPromotion = true;
                            promotionRow = row;
                            promotionCol = col;
                            
                            // Notify listener to show promotion dialog
                            if (promotionListener != null) {
                                promotionListener.onPawnReachedPromotion(row, col, isWhiteTurn);
                            }
                            
                            // Important: DO NOT complete the turn yet
                            // It will be completed after promotion selection
                            
                            // Reset selection
                            selectedPiece = null;
                            selectedRow = -1;
                            selectedCol = -1;
                            
                            invalidate();
                            return;
                        }
                    }

                    // Reset selection
                    selectedPiece = null;
                    selectedRow = -1;
                    selectedCol = -1;
                    
                    // Update visibility for current player after move
                    // This ensures the player can see their new position until blackout screen
                    updateVisibility(isWhiteTurn);
                    
                    // Trigger game state management
                    if (gameStateManager != null) {
                        gameStateManager.onMoveMade();
                    }
                } else {
                    // Invalid move, check if clicking on another own piece to select it instead
                    if (clickedPiece != null && clickedPiece.isWhite == isWhiteTurn) {
                        selectedPiece = clickedPiece;
                        selectedRow = row;
                        selectedCol = col;
                    } else {
                        // Deselect if clicking on invalid move location
                        selectedPiece = null;
                        selectedRow = -1;
                        selectedCol = -1;
                    }
                }
            }
        } else if (clickedPiece != null && clickedPiece.isWhite == isWhiteTurn) {
            // Select the piece if it belongs to the current player
            selectedPiece = clickedPiece;
            selectedRow = row;
            selectedCol = col;
        }
        invalidate();
    }
    // Inner class for chess pieces
    public static class ChessPiece {
        public enum Type {
            PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING
        }
        
        Type type;
        public boolean isWhite;
        public boolean hasMoved = false; // Add this field to track if the piece has moved
        
        public ChessPiece(Type type, boolean isWhite) {
            this.type = type;
            this.isWhite = isWhite;
        }
        
        public String getSymbol() {
            switch (type) {
                case PAWN: return "♟";
                case ROOK: return "♜";
                case KNIGHT: return "♞";
                case BISHOP: return "♝";
                case QUEEN: return "♛";
                case KING: return "♚";
                default: return "";
            }
        }
    }

    public void setBoard(ChessPiece[][] customBoard) {
        this.board = customBoard;
        updateVisibility(isWhiteTurn);
        invalidate();
    }

    /**
     * Determines if the en passant indicator should be shown at the given position.
     * Only show it if the current player can perform an en passant capture at this position.
     */
    private boolean shouldShowEnPassantIndicator(int row, int col) {
        if (!ChessRules.isEnPassantActive()) {
            return false;
        }
        
        int[] enPassantSquare = ChessRules.getEnPassantSquare();
        if (enPassantSquare == null) {
            return false;
        }

        // Only show the ghost pawn to the player who can capture it
        // i.e., if white pawn moved, only show to black's turn and vice versa
        if ((ChessRules.isEnPassantWhitePawn() && isWhiteTurn) ||
            (!ChessRules.isEnPassantWhitePawn() && !isWhiteTurn)) {
            return false;
        }
        
        // Check if this is the en passant square
        if (row == enPassantSquare[0] && col == enPassantSquare[1]) {
            // Check if there's a pawn of the opposite color that can capture
            int pawnRow = ChessRules.isEnPassantWhitePawn() ? row - 1 : row + 1;
            for (int pawnCol = col - 1; pawnCol <= col + 1; pawnCol += 2) {
                if (pawnCol >= 0 && pawnCol < 8) {
                    ChessPiece piece = board[pawnRow][pawnCol];
                    if (piece != null && 
                        piece.type == ChessPiece.Type.PAWN && 
                        piece.isWhite != ChessRules.isEnPassantWhitePawn()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Sets the en passant square explicitly.
     * Used when restoring state between turns.
     */
    public void setEnPassantSquare(int row, int col) {
        // When restoring from GameActivity, we want to show the ghost pawn
        // to the opponent, so we create a new opportunity
        boolean isWhitePawn = row == 5; // row 5 means it was a white pawn that moved
        ChessRules.createEnPassantOpportunity(
            isWhitePawn ? row - 1 : row + 1, // The row where the pawn actually is
            col,
            isWhitePawn,
            col // We don't have the original source column, so use the same column
        );
        
        System.out.println("En passant square explicitly set to " + toChessNotation(row, col));
        invalidate();
    }
    
    /**
     * Updates the en passant tracking after a move has been made.
     * This should be called after every move.
     */
    private void updateEnPassantAfterMove(int startRow, int startCol, int endRow, int endCol) {
        // Clear any existing en passant opportunity
        ChessRules.clearEnPassant();
        
        // Check if a pawn moved two squares
        if (Math.abs(endRow - startRow) == 2 && 
            board[endRow][endCol] != null && 
            board[endRow][endCol].type == ChessPiece.Type.PAWN) {
            // Create new en passant opportunity
            ChessRules.createEnPassantOpportunity(
                endRow, 
                endCol, 
                board[endRow][endCol].isWhite, 
                startCol
            );
        }
    }
    
    /**
     * Gets the current en passant square.
     * @return An array with [row, col] of the en passant square, or null if none exists
     */
    public int[] getEnPassantSquare() {
        return ChessRules.getEnPassantSquare();
    }

    public void setPawnPromotionListener(PawnPromotionListener listener) {
        this.promotionListener = listener;
    }

    /**
     * Promotes a pawn to the selected piece type.
     * 
     * @param pieceType The type of piece to promote to
     */
    public void promotePawn(ChessPiece.Type pieceType) {
        if (!waitingForPromotion || promotionRow < 0 || promotionCol < 0) {
            return;
        }
        
        // Check if there's a pawn to promote
        ChessPiece pawn = board[promotionRow][promotionCol];
        if (pawn == null || pawn.type != ChessPiece.Type.PAWN) {
            System.out.println("Error: No pawn to promote at " + toChessNotation(promotionRow, promotionCol));
            waitingForPromotion = false;
            return;
        }
        
        // Create a new piece of the selected type
        boolean isWhite = pawn.isWhite;
        ChessPiece newPiece = new ChessPiece(pieceType, isWhite);
        newPiece.hasMoved = true; // Mark as moved to prevent castling with a promoted rook
        
        // Replace the pawn with the new piece
        board[promotionRow][promotionCol] = newPiece;
        
        // Reset promotion state
        waitingForPromotion = false;
        promotionRow = -1;
        promotionCol = -1;
        
        // Update visibility based on the new piece's rules
        updateVisibility(isWhiteTurn);
        
        // Redraw the board
        invalidate();
        
        // Notify listener that promotion is completed
        if (promotionListener != null) {
            promotionListener.onPromotionCompleted();
        }
        
        // Complete the turn (delayed until after promotion)
        if (gameStateManager != null) {
            gameStateManager.onMoveMade();
        }
    }

    private boolean isPawnTwoSquareMove(int fromRow, int fromCol, int toRow, int toCol) {
        ChessPiece piece = board[fromRow][fromCol];
        if (piece == null || piece.type != ChessPiece.Type.PAWN) {
            return false;
        }
        
        // Check if the move is two squares forward
        boolean isTwoSquares = Math.abs(fromRow - toRow) == 2;
        boolean isSameColumn = fromCol == toCol;
        boolean isForward = piece.isWhite ? (fromRow > toRow) : (fromRow < toRow);
        
        return isTwoSquares && isSameColumn && isForward;
    }
}
