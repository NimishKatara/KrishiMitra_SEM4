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
import java.util.Arrays;
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

        cropAdapter = new CropAdapter(cropList, this);
        cropRecyclerView.setAdapter(cropAdapter);

        Button addCropButton = findViewById(R.id.addCrop);
        addCropButton.setOnClickListener(view -> {
            Intent addIntent = new Intent(LandingActivity.this, CropRegistration.class);
            startActivity(addIntent);
        });

        ImageView settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(view -> {
            Intent settingsIntent = new Intent(LandingActivity.this, Settings.class);
            startActivity(settingsIntent);
        });

        // Get crop data from intent
        Intent intent = getIntent();
        String soilType = intent.getStringExtra("soilType");
        String cropType = intent.getStringExtra("cropType");

        // Save if data exists
        if (soilType != null && cropType != null && user != null) {
            saveCropToFirestore(new Crop(soilType, cropType));
        }

        // Always load crops on start
        if (user != null) {
            loadCropsFromFirestore();
        }
    }

    private void saveCropToFirestore(Crop crop) {
        if (user == null) return;

        String phone = user.getPhoneNumber();
        DocumentReference userDocRef = db.collection("users").document(phone);

        userDocRef.set(new HashMap<>()) // Create empty user doc if not exists
                .addOnSuccessListener(unused -> {
                    userDocRef.collection("registeredCrops")
                            .add(new HashMap<String, Object>() {{
                                put("cropType", crop.getCropType());
                                put("soilType", crop.getSoilType());
                                put("timestamp", FieldValue.serverTimestamp());
                            }})
                            .addOnSuccessListener(documentReference ->
                                    Log.d("LandingActivity", "Crop saved"))
                            .addOnFailureListener(e ->
                                    Log.e("LandingActivity", "Failed to save crop", e));
                })
                .addOnFailureListener(e ->
                        Log.e("LandingActivity", "Failed to create user doc", e));
    }

    private void loadCropsFromFirestore() {
        cropList.clear();
        cropAdapter.notifyDataSetChanged();

        if (user == null) return;
        String phone = user.getPhoneNumber();
        if (phone == null) return;

        db.collection("users")
                .document(phone)
                .collection("registeredCrops")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String cropType = document.getString("cropType");
                        String soilType = document.getString("soilType");
                        String docId = document.getId();

                        if (cropType != null && soilType != null) {
                            Crop crop = new Crop(soilType, cropType);
                            crop.setDocumentId(docId); // ⚠️ Add the document ID
                            crop.setFertilizerNutrients(Arrays.asList("Ammonium", "Sulphate", "Nitrate")); // ✅ Hardcoded nutrients
                            cropList.add(crop);
                        }

                    }

                    cropAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("LandingActivity", "Failed to load crops", e));
    }
}
