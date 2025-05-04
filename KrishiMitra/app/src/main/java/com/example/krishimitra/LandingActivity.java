package com.example.krishimitra;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LandingActivity extends AppCompatActivity {

    private LinearLayout cropContainer;
    private static final ArrayList<Crop> cropList = new ArrayList<>();  // Persisted across launches

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing);

        cropContainer = findViewById(R.id.crop_container);

        // Retrieve data from CropRegistration
        Intent intent = getIntent();
        String soilType = intent.getStringExtra("soilType");
        String cropType = intent.getStringExtra("cropType");

        // Add new crop to the list if available
        if (soilType != null && cropType != null) {
            cropList.add(new Crop(soilType, cropType));
        }

        // Clear the container first
        cropContainer.removeAllViews();

        // Rebuild all cards
        for (Crop crop : cropList) {
            addCropCard(crop.getSoilType(), crop.getCropType());
        }

        // Add Crop button
        Button addCropButton = findViewById(R.id.addCrop);
        addCropButton.setOnClickListener(view -> {
            Intent addIntent = new Intent(LandingActivity.this, CropRegistration.class);
            startActivity(addIntent);
        });

        // Settings button
        ImageView settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(view -> {
            Intent settingsIntent = new Intent(LandingActivity.this, Settings.class);
            startActivity(settingsIntent);
        });
    }

    private void addCropCard(String soilType, String cropType) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(40, 40, 40, 40);

        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.parseColor("#333333"));
        background.setCornerRadius(30f);
        card.setBackgroundResource(R.drawable.cropcard_bg);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 32, 0, 32);
        card.setLayoutParams(cardParams);

        // Title
        TextView title = new TextView(this);
        title.setText("Crop Details");
        title.setTextSize(20);
        title.setTextColor(Color.WHITE);
        title.setPadding(0, 0, 0, 24);
        card.addView(title);

        // Crop Type
        TextView crop = new TextView(this);
        crop.setText("Crop: " + cropType);
        crop.setTextColor(Color.WHITE);
        crop.setTextSize(16);
        card.addView(crop);

        // Soil Type
        TextView soil = new TextView(this);
        soil.setText("Soil: " + soilType);
        soil.setTextColor(Color.WHITE);
        soil.setTextSize(16);
        soil.setPadding(0, 8, 0, 0);
        card.addView(soil);

        // Fertilizer (placeholder)
        TextView fert = new TextView(this);
        fert.setText("Fertilizers: Ammonium, Sulphate, Nitrate");
        fert.setTextColor(Color.WHITE);
        fert.setTextSize(16);
        fert.setPadding(0, 16, 0, 0);
        card.addView(fert);

        cropContainer.addView(card);
    }
}
