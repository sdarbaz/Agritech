package com.example.agritech;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Realtimefinder extends AppCompatActivity {

    private TextView cropNameTextView;
    private TextView temperatureTextView;
    private TextView humidityTextView;
    private TextView nitrogenTextView;
    private TextView phosphorousTextView;
    private TextView potassiumTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_data);

        cropNameTextView = findViewById(R.id.crop_name_textview);
        temperatureTextView = findViewById(R.id.temperature_textview);
        humidityTextView = findViewById(R.id.humidity_textview);
       /* nitrogenTextView = findViewById(R.id.nitrogen_textview);
        phosphorousTextView = findViewById(R.id.phosphorous_textview);
        potassiumTextView = findViewById(R.id.potassium_textview);*/


        fetchThingspeakData();
    }

    private void fetchThingspeakData() {

        String apiKey = "WRVPWVG9PD0OUAJG";
        String channelId = "2599324";

        // Create a URL to fetch the latest data from Thingspeak
        String url = "https://api.thingspeak.com/channels/" + channelId + "/feeds/last.json?api_key=" + apiKey;

        // Use OkHttp to make a GET request to the URL
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(Realtimefinder.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseBody);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    final double[] temperature = new double[1];
                    final double[] humidity = new double[1];
                    final double[] nitrogen = new double[1];
                    final double[] phosphorous = new double[1];
                    final double[] potassium = new double[1];

                    // Extract the values from the JSON response
                    try {
                        temperature[0] = jsonObject.getDouble("field1");
                        humidity[0] = jsonObject.getDouble("field2");
                        nitrogen[0] = jsonObject.getDouble("field3");
                        phosphorous[0] = jsonObject.getDouble("field4");
                        potassium[0] = jsonObject.getDouble("field3");

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }



                    // Compare the values with the CSV file
                    String cropName = findMatchingCrop(temperature[0], humidity[0], nitrogen[0], phosphorous[0], potassium[0]);

                    // Display the crop name
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cropNameTextView.setText("Crop Name: " + cropName);
                            temperatureTextView.setText("Temperature: " + temperature[0] + "°C");
                            humidityTextView.setText("Humidity: " + humidity[0] + "%");
                           /* nitrogenTextView.setText("Nitrogen(N): " + nitrogen[0] );
                            phosphorousTextView.setText("Phosphorous(P): " + phosphorous[0] );
                            potassiumTextView.setText("Potassium(K): " + potassium[0]);*/
                        }
                    });
                } else {
                    Toast.makeText(Realtimefinder.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String findMatchingCrop(double temperature, double humidity,double nitrogen, double phosphorous, double potassium) throws IOException {
        // Read the CSV file from the assets folder
        InputStream inputStream = getResources().getAssets().open("npk.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        List<String[]> csvData = new ArrayList<>();
        reader.readLine();

        try {
            while ((line = reader.readLine())!= null) {
                String[] values = line.split(",");
                csvData.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Iterate through the CSV data and find a match
        for (String[] row : csvData) {
            double csvTemperature = Double.parseDouble(row[0]);
            double csvHumidity = Double.parseDouble(row[1]);
            /*double csvNitrogen = Double.parseDouble(row[2]);
            double csvPhosphorous = Double.parseDouble(row[3]);
            double csvPotassium = Double.parseDouble(row[4]);*/
            String csvCropName = row[5];

            if (Math.abs(temperature - csvTemperature) < 0.1 &&
                    Math.abs(humidity - csvHumidity) < 0.1)/* &&
                    Math.abs(nitrogen - csvNitrogen) < 0.1 &&
                    Math.abs(phosphorous - csvPhosphorous) < 0.1 &&
                    Math.abs(potassium - csvPotassium) < 0.1)*/ {
                return csvCropName;
            }
        }

        return "No matching crop found";
    }
}