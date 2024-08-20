package com.example.agritech;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PriceFinder extends AppCompatActivity {

    private TextView tvPriceFinderUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_finder);

        // Initialize text view
        tvPriceFinderUrl = findViewById(R.id.tv_price_finder_url);

        // Replace with your local server URL
        String url = "http://127.0.0.1:2400";
        String message = "Check out the latest prices for your crops at: ";

        tvPriceFinderUrl.setText(message + url);

        tvPriceFinderUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the URL in the default browser app
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }
}

