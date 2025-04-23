package com.example.krishimitra;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.feedbackOption).setOnClickListener(v -> {
            Intent intent = new Intent(Settings.this, Feedback.class);
            startActivity(intent);
        });
        findViewById(R.id.logoutOption).setOnClickListener(v -> {
            Intent intent = new Intent(Settings.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
