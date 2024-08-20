package com.example.agritech;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button cropFinderButton;
    private Button priceFinderButton;
    private Button realtimecropFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeText = findViewById(R.id.welcome_text);
        cropFinderButton = findViewById(R.id.crop_finder_button);
        priceFinderButton = findViewById(R.id.price_finder_button);
        realtimecropFinder = findViewById(R.id.real_time_finder);

        // Add click listeners to the buttons
        cropFinderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Crop Finder activity
                startActivity(new Intent(MainActivity.this, CropFinder.class));
            }
        });

        priceFinderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Price Finder activity
                startActivity(new Intent(MainActivity.this, PriceFinder.class));
            }
        });

        realtimecropFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Realtimefinder.class));
            }
        });
    }
}