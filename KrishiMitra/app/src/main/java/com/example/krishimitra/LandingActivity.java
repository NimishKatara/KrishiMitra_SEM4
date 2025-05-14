package com.example.krishimitra;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import java.util.List;
import java.util.Map;

public class LandingActivity extends AppCompatActivity {

    private RecyclerView cropRecyclerView;
    private CropAdapter cropAdapter;
    private ArrayList<Crop> cropList = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseUser user;

    private final Map<String, Map<String, List<String>>> fertilizerMap = new HashMap<>();

    private final ActivityResultLauncher<Intent> cropResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String soil = result.getData().getStringExtra("soilType");
                    String crop = result.getData().getStringExtra("cropType");

                    if (soil != null && crop != null) {
                        Crop newCrop = new Crop(soil, crop);
                        saveCropToFirestore(newCrop);
                        cropList.add(0, newCrop);
                        newCrop.setFertilizerNutrients(getFertilizerRecommendation(soil, crop));
                        cropAdapter.notifyItemInserted(0);
                        cropRecyclerView.scrollToPosition(0);
                    }
                }
            });

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
            cropResultLauncher.launch(addIntent);
        });

        ImageView settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(view -> {
            Intent settingsIntent = new Intent(LandingActivity.this, Settings.class);
            startActivity(settingsIntent);
        });

        initializeFertilizerMap();

        if (user != null) {
            loadCropsFromFirestore();
        }

        cropAdapter.setOnItemClickListener(crop -> {
            Intent intent = new Intent(LandingActivity.this, FertilizerActivity.class);
            intent.putExtra("soilType", crop.getSoilType());
            intent.putExtra("cropType", crop.getCropType());
            startActivity(intent);
        });
    }

    private void initializeFertilizerMap() {
        fertilizerMap.put("Red", new HashMap<>() {{
            put("Paddy", Arrays.asList("Nitrogen - 50 kg/acre", "Zinc sulphate - 25 kg/acre"));
            put("Tomato", Arrays.asList("NPK 19:19:19 - 30 kg/acre", "Calcium Nitrate - 15 kg/acre"));
            put("Flowers", Arrays.asList("Compost - 50 kg/acre", "Micronutrient mix - 10 kg/acre"));
            put("Cabbage", Arrays.asList("Urea - 30 kg/acre", "Compost - 60 kg/acre"));
            put("Potato", Arrays.asList("NPK 12:32:16 - 40 kg/acre", "Urea - 25 kg/acre"));
        }});

        fertilizerMap.put("Black", new HashMap<>() {{
            put("Potato", Arrays.asList("Ammonia - 60 kg/acre", "NPK 12:32:16 - 50 kg/acre"));
            put("Paddy", Arrays.asList("MOP - 45 kg/acre", "Super phosphate - 35 kg/acre"));
            put("Sweetcorn", Arrays.asList("DAP - 40 kg/acre", "Potash - 25 kg/acre"));
        }});

        fertilizerMap.put("Sandy", new HashMap<>() {{
            put("Tomato", Arrays.asList("NPK 20:20:20 - 40 kg/acre", "Vermicompost - 80 kg/acre"));
            put("Banana", Arrays.asList("NPK 17:17:17 - 50 kg/acre", "Organic compost - 100 kg/acre"));
            put("Anaar", Arrays.asList("Bone meal - 40 kg/acre", "Compost - 80 kg/acre"));
        }});

        fertilizerMap.put("Clay", new HashMap<>() {{
            put("Beans", Arrays.asList("FYM - 100 kg/acre", "NPK 14:35:14 - 40 kg/acre"));
            put("Sweetcorn", Arrays.asList("Potassium - 45 kg/acre", "MOP - 20 kg/acre"));
            put("Cabbage", Arrays.asList("Compost - 60 kg/acre", "Urea - 30 kg/acre"));
            put("Flowers", Arrays.asList("Compost - 60 kg/acre", "Bone meal - 15 kg/acre"));
        }});

        fertilizerMap.put("Rock Soil", new HashMap<>() {{
            put("Horsegram", Arrays.asList("Minimal fertilizer needed", "Add organic manure"));
            put("Flowers", Arrays.asList("Compost - 40 kg/acre", "Bone meal - 20 kg/acre"));
        }});

        fertilizerMap.put("Sandy Red", new HashMap<>() {{
            put("Cabbage", Arrays.asList("Compost - 60 kg/acre", "Urea - 30 kg/acre"));
            put("Tomato", Arrays.asList("NPK 19:19:19 - 30 kg/acre", "Micronutrients - 10 kg/acre"));
        }});

        fertilizerMap.put("Black Sandy", new HashMap<>() {{
            put("Anaar", Arrays.asList("Bone meal - 40 kg/acre", "NPK 18:18:18 - 25 kg/acre"));
            put("Banana", Arrays.asList("Compost - 80 kg/acre", "NPK 20:10:10 - 35 kg/acre"));
        }});
    }

    private void saveCropToFirestore(Crop crop) {
        if (user == null) return;

        String phone = user.getPhoneNumber();
        if (phone == null) return;

        DocumentReference userDocRef = db.collection("users").document(phone);

        userDocRef.set(new HashMap<>());

        userDocRef.collection("registeredCrops")
                .add(new HashMap<>() {{
                    put("cropType", crop.getCropType());
                    put("soilType", crop.getSoilType());
                    put("timestamp", FieldValue.serverTimestamp());
                }})
                .addOnSuccessListener(documentReference -> Log.d("LandingActivity", "Crop saved"))
                .addOnFailureListener(e -> Log.e("LandingActivity", "Failed to save crop", e));

        List<String> fertilizerList = getFertilizerRecommendation(crop.getSoilType(), crop.getCropType());
        if (fertilizerList != null) {
            for (String fert : fertilizerList) {
                String[] parts = fert.split(" - ");
                if (parts.length == 2) {
                    String fertName = parts[0];
                    String fertDosage = parts[1];

                    userDocRef.collection("fertilizers")
                            .add(new HashMap<>() {{
                                put("cropType", crop.getCropType());
                                put("soilType", crop.getSoilType());
                                put("name", fertName);
                                put("dosagePerAcre", fertDosage);
                            }});
                }
            }
        }
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
                            crop.setDocumentId(docId);
                            crop.setFertilizerNutrients(getFertilizerRecommendation(soilType, cropType));
                            cropList.add(crop);
                        }
                    }
                    cropAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("LandingActivity", "Failed to load crops", e));
    }

    private List<String> getFertilizerRecommendation(String soilType, String cropType) {
        Map<String, List<String>> cropMap = fertilizerMap.get(soilType);
        if (cropMap != null) {
            List<String> result = cropMap.get(cropType);
            if (result != null) return result;
        }
        return Arrays.asList("Urea - 50 kg/acre", "Nitrogen 10:26:26 - 40 kg/acre");
    }
}
