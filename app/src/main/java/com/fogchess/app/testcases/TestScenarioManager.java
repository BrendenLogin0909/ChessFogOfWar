package com.fogchess.app.testcases;

import com.fogchess.app.ChessBoardView;
import com.fogchess.app.ChessBoardView.ChessPiece;

/**
 * Manages test scenario board setups for testing various chess functionality.
 * This class centralizes test board initialization to keep game code clean.
 */
public class TestScenarioManager {

    // Test scenario IDs
    // Castling scenarios
    public static final String TEST_CASTLING_WHITE_KINGSIDE = "castling_white_kingside";
    public static final String TEST_CASTLING_WHITE_QUEENSIDE = "castling_white_queenside";
    public static final String TEST_CASTLING_BLACK_KINGSIDE = "castling_black_kingside";
    public static final String TEST_CASTLING_BLACK_QUEENSIDE = "castling_black_queenside";
    
    // En passant scenarios
    public static final String TEST_EN_PASSANT_WHITE = "en_passant_white";
    public static final String TEST_EN_PASSANT_BLACK = "en_passant_black";
    
    // Promotion scenarios
    public static final String TEST_PROMOTION_WHITE = "promotion_white_light";
    public static final String TEST_PROMOTION_BLACK = "promotion_black_light";
    
    // Checkmate scenarios
    public static final String TEST_CHECKMATE_SCHOLARS = "checkmate_scholars_mate";
    public static final String TEST_CHECKMATE_LEGALS = "checkmate_legals_mate";
    public static final String TEST_CHECKMATE_SMOTHERED = "checkmate_smothered_mate";
    public static final String TEST_CHECKMATE_BACK_RANK = "checkmate_back_rank";
    public static final String TEST_CHECKMATE_ANASTASIA = "checkmate_anastasia_mate";
    public static final String TEST_CHECKMATE_ARABIA = "checkmate_arabia_mate";
    public static final String TEST_CHECKMATE_TWO_ROOKS = "checkmate_two_rooks";
    public static final String TEST_CHECKMATE_TWO_QUEENS = "checkmate_two_queens";

    /**
     * Returns a chess board configuration based on the provided test scenario identifier.
     *
     * @param testScenario The identifier for the test scenario
     * @return A chess board configuration for the specified test scenario, or null if not found
     */
    public static ChessPiece[][] getTestBoard(String testScenario) {
        if (testScenario == null) {
            return null;
        }

        ChessPiece[][] board = new ChessPiece[8][8];
        clearBoard(board);

        switch (testScenario) {
            // Castling scenarios
            case TEST_CASTLING_WHITE_KINGSIDE:
                return TestCastling.setupWhiteKingsideCastleScenario();
            case TEST_CASTLING_WHITE_QUEENSIDE:
                return TestCastling.setupWhiteQueensideCastleScenario();
            case TEST_CASTLING_BLACK_KINGSIDE:
                return TestCastling.setupBlackKingsideCastleScenario();
            case TEST_CASTLING_BLACK_QUEENSIDE:
                return TestCastling.setupBlackQueensideCastleScenario();
            case "castling_invalid_through_check":
                return TestCastling.setupPassThroughCheckScenario();
            case "castling_invalid_in_check":
                return TestCastling.setupKingInCheckScenario();
            case "castling_invalid_king_moved":
                return TestCastling.setupKingMovedScenario();
            case "castling_invalid_rook_moved":
                return TestCastling.setupRookMovedScenario();
            case "castling_invalid_pieces_between":
                return TestCastling.setupPiecesBetweenScenario();
            case "castling_invalid_land_in_check":
                return TestCastling.setupLandInCheckScenario();
                
            // En Passant scenarios
            case TEST_EN_PASSANT_WHITE:
                return TestEnPassant.setupWhiteEnPassantCaptureScenario();
            case TEST_EN_PASSANT_BLACK:
                return TestEnPassant.setupBlackEnPassantCaptureScenario();
            case "en_passant_pawns_can_see":
                return TestEnPassant.setupPawnsCanSeeScenario();
            case "en_passant_other_pieces_can_see":
                return TestEnPassant.setupOtherPiecesCanSeeScenario();
            case "en_passant_combined_visibility":
                return TestEnPassant.setupCombinedVisibilityScenario();
            
            // Checkmate scenarios
            case TEST_CHECKMATE_SCHOLARS:
                return TestCheckmate.TestCheckmateScenarios.setupScholarsMateScenario();
            case TEST_CHECKMATE_LEGALS:
                return TestCheckmate.TestCheckmateScenarios.setupLegalsMateScenario();
            case TEST_CHECKMATE_SMOTHERED:
                return TestCheckmate.TestCheckmateScenarios.setupSmotheredMateScenario();
            case TEST_CHECKMATE_BACK_RANK:
                return TestCheckmate.TestCheckmateScenarios.setupBackRankMateScenario();
            case TEST_CHECKMATE_ANASTASIA:
                return TestCheckmate.TestCheckmateScenarios.setupAnastasiaMateScenario();
            case TEST_CHECKMATE_ARABIA:
                return TestCheckmate.TestCheckmateScenarios.setupArabiaMateScenario();
            case TEST_CHECKMATE_TWO_ROOKS:
                return TestCheckmate.TestCheckmateScenarios.setupTwoRooksMateScenario();
            case TEST_CHECKMATE_TWO_QUEENS:
                return TestCheckmate.TestCheckmateScenarios.setupTwoQueensMateScenario();
                
            // Pawn Promotion scenarios
            case TEST_PROMOTION_WHITE:
                TestPromotion.setupWhitePromotionLightSquare(board);
                return board;
            case "promotion_white_dark":
                TestPromotion.setupWhitePromotionDarkSquare(board);
                return board;
            case TEST_PROMOTION_BLACK:
                TestPromotion.setupBlackPromotionLightSquare(board);
                return board;
            case "promotion_black_dark":
                TestPromotion.setupBlackPromotionDarkSquare(board);
                return board;
            case "promotion_multiple":
                TestPromotion.setupMultiplePromotions(board);
                return board;
            case "promotion_with_capture":
                TestPromotion.setupPromotionWithCapture(board);
                return board;
                
            default:
                return null;
        }
    }
    
    /**
     * For scenarios that require special state like en passant,
     * this method configures the board view with the necessary state.
     * 
     * @param chessBoardView The view to configure
     * @param testScenario The scenario being tested
     */
    public static void configureSpecialScenarios(ChessBoardView chessBoardView, String testScenario) {
        if (testScenario == null) {
            return;
        }
        
        switch (testScenario) {
            case TEST_EN_PASSANT_WHITE:
                // For white en passant test:
                // - Black pawn has just moved from row 1 to row 3 (moved 2 squares)
                // - The en passant square is at row 2, col 3 (between start and end positions)
                // This simulates as if the black pawn just moved two squares forward
                chessBoardView.setEnPassantSquare(2, 3);
                break;
                
            case TEST_EN_PASSANT_BLACK:
                // For black en passant test:
                // - White pawn has just moved from row 6 to row 4 (moved 2 squares)
                // - The en passant square is at row 5, col 3 (between start and end positions)
                // This simulates as if the white pawn just moved two squares forward
                chessBoardView.setEnPassantSquare(5, 3);
                break;
                
            case "en_passant_pawns_can_see":
                // Set up the en passant square for the pawns visibility test
                chessBoardView.setEnPassantSquare(2, 3);
                break;
                
            case "en_passant_other_pieces_can_see":
                // Set up the en passant square for the non-pawn pieces test
                chessBoardView.setEnPassantSquare(2, 3);
                break;
                
            case "en_passant_combined_visibility":
                // Set up the en passant square for the combined test
                chessBoardView.setEnPassantSquare(2, 3);
                break;
        }
    }

    public static void initializeBoard(ChessBoardView.ChessPiece[][] board, String scenario) {
        clearBoard(board);
        
        switch (scenario) {
            // ... existing code ...
            case TEST_PROMOTION_WHITE:
                TestPromotion.setupWhitePromotionLightSquare(board);
                break;
            case TEST_PROMOTION_BLACK:
                TestPromotion.setupBlackPromotionLightSquare(board);
                break;
            case "promotion_white_dark":
                TestPromotion.setupWhitePromotionDarkSquare(board);
                break;
            case "promotion_black_dark":
                TestPromotion.setupBlackPromotionDarkSquare(board);
                break;
            case "promotion_multiple":
                TestPromotion.setupMultiplePromotions(board);
                break;
            case "promotion_with_capture":
                TestPromotion.setupPromotionWithCapture(board);
                break;
            // ... existing code ...
        }
    }

    /**
     * Loads a test scenario into the provided ChessBoardView.
     * This method sets up both the board configuration and any special state needed.
     *
     * @param chessBoardView The view to load the scenario into
     * @param testScenario The identifier for the test scenario
     */
    public static void loadTestScenario(ChessBoardView chessBoardView, String testScenario) {
        // Get the board configuration for this scenario
        ChessPiece[][] board = getTestBoard(testScenario);
        if (board != null) {
            // Set up the board
            chessBoardView.setBoard(board);
            
            // Configure any special state (like en passant)
            configureSpecialScenarios(chessBoardView, testScenario);
        }
    }

    /**
     * Clears a chess board by setting all squares to null.
     * @param board The board to clear
     */
    private static void clearBoard(ChessPiece[][] board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }
    }
} 