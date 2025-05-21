package com.fogchess.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    
    private Button startGameButton;
    private Button settingsButton;
    private Button aboutButton;
    private Button testCasesButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        startGameButton = findViewById(R.id.startGameButton);
        settingsButton = findViewById(R.id.settingsButton);
        aboutButton = findViewById(R.id.aboutButton);
        testCasesButton = findViewById(R.id.testCasesButton);
        
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
        
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettings();
            }
        });
        
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAbout();
            }
        });

        testCasesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTestScenarios();
            }
        });
    }
    
    private void startGame() {
        android.content.Intent intent = new android.content.Intent(MainActivity.this, GameActivity.class);
        startActivity(intent);
    }
    
    private void showSettings() {
        // For the prototype, just show a toast message
        Toast.makeText(this, "Settings will be available in the full version", Toast.LENGTH_SHORT).show();
    }

    private void startTestScenarios() {
        android.content.Intent intent = new android.content.Intent(MainActivity.this, TestScenariosActivity.class);
        startActivity(intent);
    }
    
    private void showAbout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About Fog Chess");
        builder.setMessage("Fog of War Chess\n\n" +
                          "A chess variant where you can only see opponent pieces that are in your pieces' movement paths.\n\n" +
                          "Rules:\n" +
                          "- Standard chess movement rules apply\n" +
                          "- You can only see opponent pieces that your pieces could potentially move to\n" +
                          "- A blackout screen appears between turns when passing the device\n\n" +
                          "Version 1.0");
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
