package com.example.agritech;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CropFinder extends AppCompatActivity {

    private EditText nitrogenEditText;
    private EditText phosphorousEditText;
    private EditText potassiumEditText;
    private EditText temperatureEditText;
    private EditText humidityEditText;
    private Button submitButton;
    private TextView recommendedCropText;
    private ImageView cropImage;

    private List<Crop> crops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        nitrogenEditText = findViewById(R.id.nitrogen_edit_text);
        phosphorousEditText = findViewById(R.id.phosphorous_edit_text);
        potassiumEditText = findViewById(R.id.potassium_edit_text);
        temperatureEditText = findViewById(R.id.temperature_edit_text);
        humidityEditText = findViewById(R.id.humidity_edit_text);
        submitButton = findViewById(R.id.submit_button);
        recommendedCropText = findViewById(R.id.recommended_crop_text);
        cropImage = findViewById(R.id.crop_image);

        // Load the CSV file
        loadCsvFile();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input values
                int nitrogen = Integer.parseInt(nitrogenEditText.getText().toString());
                int phosphorous = Integer.parseInt(phosphorousEditText.getText().toString());
                int potassium = Integer.parseInt(potassiumEditText.getText().toString());
                int temperature = Integer.parseInt(temperatureEditText.getText().toString());
                int humidity = Integer.parseInt(humidityEditText.getText().toString());

                // Find the recommended crop
                Crop recommendedCrop = findRecommendedCrop(nitrogen, phosphorous, potassium, temperature, humidity);

                if (recommendedCrop != null) {
                    recommendedCropText.setText("Recommended Crop: " + recommendedCrop.getName());

                    String imageName = recommendedCrop.getImage();
                    Log.d("CropFinder","Image name:" + imageName);

                    // Load the crop image
                    int imageResourceId = getResources().getIdentifier(recommendedCrop.getImage(), "drawable", getPackageName());
                    Log.d("CropFinder", "Image resource ID: " + imageResourceId);
                    cropImage.setImageResource(imageResourceId);
                    if(imageResourceId == 0){
                        Log.e("CropFinder","Image resource ID not found!");
                    }
                } else {
                    Toast.makeText(CropFinder.this, "No matching crop found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadCsvFile() {
        AssetManager assetManager = getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("crop.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line;
            boolean isFirstLine = true;
            crops = new ArrayList<>();
            while ((line = reader.readLine())!= null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] values = line.split(",");
                Crop crop = new Crop(values[5], Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4]));
                crops.add(crop);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Crop findRecommendedCrop(int nitrogen, int phosphorous, int potassium, int temperature, int humidity) {
        for (Crop crop : crops) {
            if (crop.getNitrogen() == nitrogen && crop.getPhosphorous() == phosphorous && crop.getPotassium() == potassium && crop.getTemperature() == temperature &&crop.getHumidity() == humidity) {
                return crop;
            }
        }
        return null;
    }

    private class Crop {
        private String name;
        private int nitrogen;
        private int phosphorous;
        private int potassium;
        private int temperature;
        private int humidity;
        private String image;

        public Crop(String name, int nitrogen, int phosphorous, int potassium, int temperature, int humidity) {
            this.name = name;
            this.nitrogen = nitrogen;
            this.phosphorous = phosphorous;
            this.potassium = potassium;
            this.temperature = temperature;
            this.humidity = humidity;
            this.image = name.toLowerCase().replace(" ", "_") ;
        }

        public String getName() {
            return name;
        }

        public int getNitrogen() {
            return nitrogen;
        }

        public int getPhosphorous() {
            return phosphorous;
        }

        public int getPotassium() {
            return potassium;
        }

        public int getTemperature() {
            return temperature;
        }

        public int getHumidity() {
            return humidity;
        }

        public String getImage() {
            return image;
        }
    }
}