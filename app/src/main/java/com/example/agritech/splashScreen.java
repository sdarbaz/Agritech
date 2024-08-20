package com.example.agritech;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class splashScreen extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIMEOUT = 3000; // 3 seconds

    private ImageView splashLogo;
    private TextView splashText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        splashLogo = findViewById(R.id.splash_logo);
        splashText = findViewById(R.id.splash_text);

        // Add animation to the logo and text (optional)
        splashLogo.animate().alpha(1f).setDuration(1000);
        splashText.animate().alpha(1f).setDuration(1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity after the splash screen timeout
                startActivity(new Intent(splashScreen.this, MainActivity.class));
                finish();
            }
        }, SPLASH_SCREEN_TIMEOUT);
    }
}
