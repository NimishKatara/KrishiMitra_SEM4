package com.example.krishimitra;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;

public class LandingActivity extends AppCompatActivity {

    private RecyclerView cropRecyclerView;
    private CropAdapter cropAdapter;
    private ArrayList<Crop> cropList = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        cropRecyclerView = findViewById(R.id.cropRecyclerView);
        cropRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cropAdapter = new CropAdapter(cropList);
        cropRecyclerView.setAdapter(cropAdapter);

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

        // Handle crop passed from CropRegistration
        Intent intent = getIntent();
        String soilType = intent.getStringExtra("soilType");
        String cropType = intent.getStringExtra("cropType");

        // Always load crops on activity start
        if (user != null) {
            loadCropsFromFirestore();
        }

// If user is returning from CropRegistration with new crop
        if (soilType != null && cropType != null && user != null) {
            saveCropToFirestore(new Crop(soilType, cropType));
        }

        // Load all crops from Firestore for this user
        if (user != null) {
            loadCropsFromFirestore();
        }
    }

    private void saveCropToFirestore(Crop crop) {
        if (user == null) return;

        String phone = user.getPhoneNumber();

        DocumentReference userDocRef = db.collection("users").document(phone);

        // Ensure user document exists — even if it's empty
        userDocRef.set(new HashMap<>())  // Creates empty doc
                .addOnSuccessListener(unused -> {
                    userDocRef.collection("registeredCrops")
                            .add(new HashMap<String, Object>() {{
                                put("cropType", crop.getCropType());
                                put("soilType", crop.getSoilType());
                                put("timestamp", FieldValue.serverTimestamp());
                            }})
                            .addOnSuccessListener(documentReference -> Log.d("LandingActivity", "Crop saved"))
                            .addOnFailureListener(e -> Log.e("LandingActivity", "Failed to save crop", e));
                })
                .addOnFailureListener(e -> Log.e("LandingActivity", "Failed to create user doc", e));
    }


    private void loadCropsFromFirestore() {
        Log.d("LandingActivity", "Starting to load crops from Firestore...");

        cropList.clear();
        cropAdapter.notifyDataSetChanged(); // Clear existing list visually

        if (user == null) {
            Log.e("LandingActivity", "Firebase user is null. Cannot load crops.");
            return;
        }

        String phone = user.getPhoneNumber();
        if (phone == null) {
            Log.e("LandingActivity", "User phone number is null.");
            return;
        }

        Log.d("LandingActivity", "Fetching crops for phone number: " + phone);

        db.collection("users")
                .document(phone)
                .collection("registeredCrops")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d("LandingActivity", "Successfully fetched " + queryDocumentSnapshots.size() + " documents");

                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.d("LandingActivity", "No crops found for user.");
                    }

                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String cropType = document.getString("cropType");
                        String soilType = document.getString("soilType");

                        Log.d("LandingActivity", "Fetched crop - CropType: " + cropType + ", SoilType: " + soilType);

                        if (cropType != null && soilType != null) {
                            cropList.add(new Crop(soilType, cropType));
                        } else {
                            Log.w("LandingActivity", "Invalid crop document (missing fields): " + document.getId());
                        }
                    }

                    cropAdapter.notifyDataSetChanged();
                    Log.d("LandingActivity", "Crops displayed in RecyclerView.");
                })
                .addOnFailureListener(e -> {
                    Log.e("LandingActivity", "Failed to fetch crops from Firestore", e);
                });
    }

}
