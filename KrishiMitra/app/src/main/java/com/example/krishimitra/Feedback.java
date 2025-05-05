package com.example.krishimitra;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feedback);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText paragraphInput = findViewById(R.id.paragraphInput);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        Button submitButton = findViewById(R.id.submitButton);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        submitButton.setOnClickListener(v -> {
            String feedbackText = paragraphInput.getText().toString().trim();
            float ratingValue = ratingBar.getRating();

            if (feedbackText.isEmpty()) {
                Toast.makeText(this, "Please enter feedback", Toast.LENGTH_SHORT).show();
                return;
            }

            String userPhone = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getPhoneNumber() : "Unknown";

            Map<String, Object> feedback = new HashMap<>();
            feedback.put("feedback", feedbackText);
            feedback.put("rating", ratingValue);
            feedback.put("timestamp", System.currentTimeMillis());
            feedback.put("userPhone", userPhone);

            db.collection("feedback")
                    .add(feedback)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(Feedback.this, "Feedback stored successfully", Toast.LENGTH_SHORT).show();
                        paragraphInput.setText("");
                        ratingBar.setRating(0);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Feedback", "Error saving feedback", e);
                        Toast.makeText(Feedback.this, "Error storing feedback", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
