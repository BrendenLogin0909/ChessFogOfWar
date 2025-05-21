package com.fogchess.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.fogchess.app.testcases.TestCastling;
import com.fogchess.app.testcases.TestEnPassant;
import com.fogchess.app.testcases.TestPromotion;
import com.fogchess.app.testcases.TestCheckmate;

public class TestScenariosActivity extends AppCompatActivity {
    
    private Button castlingTestsButton;
    private Button enPassantTestsButton;
    private Button promotionTestsButton;
    private Button checkmateTestsButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_cases_menu);
        
        castlingTestsButton = findViewById(R.id.btn_castling_tests);
        enPassantTestsButton = findViewById(R.id.btn_en_passant_tests);
        promotionTestsButton = findViewById(R.id.btn_promotion_tests);
        checkmateTestsButton = findViewById(R.id.btn_checkmate_tests);
        
        castlingTestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCastlingTests();
            }
        });
        
        enPassantTestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEnPassantTests();
            }
        });
        
        promotionTestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPromotionTests();
            }
        });
        
        checkmateTestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCheckmateTests();
            }
        });
    }
    
    private void startCastlingTests() {
        Intent intent = new Intent(this, TestCastling.class);
        startActivity(intent);
    }
    
    private void startEnPassantTests() {
        Intent intent = new Intent(this, TestEnPassant.class);
        startActivity(intent);
    }
    
    private void startPromotionTests() {
        Intent intent = new Intent(this, TestPromotion.class);
        startActivity(intent);
    }
    
    private void startCheckmateTests() {
        Intent intent = new Intent(this, TestCheckmate.class);
        startActivity(intent);
    }
} 