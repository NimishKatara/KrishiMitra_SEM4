package com.example.krishimitra;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FertilizerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FertilizerAdapter adapter;
    ArrayList<Fertilizer> fertilizerList = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizer);

        recyclerView = findViewById(R.id.fertilizerRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<String> nutrients = getIntent().getStringArrayListExtra("nutrients");
        if (nutrients != null) {
            for (String nutrient : nutrients) {
                // Add dummy fertilizers for demo. Replace with real lookup if needed
                fertilizerList.add(new Fertilizer("Urea", nutrient, "50kg", 300, R.drawable.fertilizer_icon));
            }
        }

        adapter = new FertilizerAdapter(fertilizerList);
        recyclerView.setAdapter(adapter);
    }
}
