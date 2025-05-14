package com.example.krishimitra;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class FertilizerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FertilizerAdapter adapter;
    ArrayList<Fertilizer> fertilizerList = new ArrayList<>();
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizer);

        recyclerView = findViewById(R.id.fertilizerRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();

        String soilType = getIntent().getStringExtra("soilType");
        String cropType = getIntent().getStringExtra("cropType");

        if (soilType != null && cropType != null) {
            loadFertilizersFromFirestore(soilType, cropType);
        } else {
            Toast.makeText(this, "Missing crop or soil info", Toast.LENGTH_SHORT).show();
        }

        adapter = new FertilizerAdapter(fertilizerList);
        recyclerView.setAdapter(adapter);
    }

    private void loadFertilizersFromFirestore(String soilType, String cropType) {
        String phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        if (phone == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users")
                .document(phone)
                .collection("fertilizers")
                .whereEqualTo("soilType", soilType)
                .whereEqualTo("cropType", cropType)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    fertilizerList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String name = doc.getString("name");
                        String sType = doc.getString("soilType");
                        String cType = doc.getString("cropType");
                        Double dosage = doc.getDouble("dosagePerAcre");

                        if (name != null && sType != null && cType != null && dosage != null) {
                            fertilizerList.add(new Fertilizer(name, sType, cType, dosage));
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load fertilizers", Toast.LENGTH_SHORT).show();
                    Log.e("FertilizerActivity", "Firestore error", e);
                });
    }

}
