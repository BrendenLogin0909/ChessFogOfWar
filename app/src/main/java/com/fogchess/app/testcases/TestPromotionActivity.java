package com.fogchess.app.testcases;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.fogchess.app.GameActivity;
import com.fogchess.app.R;

/**
 * Activity that provides a menu of test scenarios for pawn promotion
 */
public class TestPromotionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promotion_tests_menu);

        setupButtonClickListeners();
    }

    private void setupButtonClickListeners() {
        setupButton(R.id.btn_white_promotion_light, "promotion_white_light");
        setupButton(R.id.btn_white_promotion_dark, "promotion_white_dark");
        setupButton(R.id.btn_black_promotion_light, "promotion_black_light");
        setupButton(R.id.btn_black_promotion_dark, "promotion_black_dark");
        setupButton(R.id.btn_multiple_promotions, "promotion_multiple");
        setupButton(R.id.btn_promotion_with_capture, "promotion_with_capture");
    }

    private void setupButton(int buttonId, final String scenarioName) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestPromotionActivity.this, GameActivity.class);
                intent.putExtra("test_scenario", scenarioName);
                startActivity(intent);
            }
        });
    }
} 