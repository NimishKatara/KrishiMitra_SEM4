package com.example.krishimitra;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CropRegistration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crop_registration);

        // Adjust for insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Submit button click -> Go to LandingActivity
        Button submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(view -> {
            Intent intent = new Intent(CropRegistration.this, LandingActivity.class);
            startActivity(intent);
            finish();
        });

        // Settings image button click -> Go to SettingsActivity
        ImageView settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(view -> {
            Intent intent = new Intent(CropRegistration.this, Settings.class);
            startActivity(intent);
        });
    }
}
