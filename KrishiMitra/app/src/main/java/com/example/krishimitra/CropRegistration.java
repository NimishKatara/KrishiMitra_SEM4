package com.example.krishimitra;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CropRegistration extends AppCompatActivity {

    Spinner soilSpinner, cropSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crop_registration);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        soilSpinner = findViewById(R.id.soilInput);
        cropSpinner = findViewById(R.id.cropInput);

        // Data for spinners
        String[] soilOptions = {"Choose Soil", "Red", "Black", "Rock Soil", "Sandy Red","Sandy", "Black Sandy", "Clay"};
        String[] cropOptions = {"Choose Crop","Flowers","Potato","Sweetcorn","Tomato","Paddy","Banana","Beans","Anaar","Horsegram","Cabbage","Fowers"};

        // Create adapters using custom layout for white text
        ArrayAdapter<String> soilAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, soilOptions);
        soilAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        soilSpinner.setAdapter(soilAdapter);

        ArrayAdapter<String> cropAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, cropOptions);
        cropAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        cropSpinner.setAdapter(cropAdapter);

        // Submit Button logic
        Button submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(view -> {
            String soil = soilSpinner.getSelectedItem().toString();
            String crop = cropSpinner.getSelectedItem().toString();

            if (soil.equals("Choose Soil") || crop.equals("Choose Crop")) {
                Toast.makeText(CropRegistration.this, "Please select both soil and crop types", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(CropRegistration.this, LandingActivity.class);
            intent.putExtra("soilType", soil);
            intent.putExtra("cropType", crop);
            startActivity(intent);
            finish();
        });

        // Settings Button
        ImageView settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(view -> {
            Intent intent = new Intent(CropRegistration.this, Settings.class);
            startActivity(intent);
        });
    }
}
