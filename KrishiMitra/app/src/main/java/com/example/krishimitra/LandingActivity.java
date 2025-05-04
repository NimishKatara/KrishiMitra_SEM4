package com.example.krishimitra;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LandingActivity extends AppCompatActivity {

    private RecyclerView cropRecyclerView;
    private CropAdapter cropAdapter;
    private static final ArrayList<Crop> cropList = new ArrayList<>();  // Persisted across launches

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing);

        cropRecyclerView = findViewById(R.id.cropRecyclerView);
        cropRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cropAdapter = new CropAdapter(cropList);
        cropRecyclerView.setAdapter(cropAdapter);

        // Retrieve data from CropRegistration
        Intent intent = getIntent();
        String soilType = intent.getStringExtra("soilType");
        String cropType = intent.getStringExtra("cropType");

        // Add new crop to the list if available
        if (soilType != null && cropType != null) {
            Crop newCrop = new Crop(soilType, cropType);
            cropList.add(newCrop);
            cropAdapter.notifyItemInserted(cropList.size() - 1);
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
}
