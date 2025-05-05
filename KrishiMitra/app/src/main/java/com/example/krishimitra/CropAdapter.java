package com.example.krishimitra;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CropAdapter extends RecyclerView.Adapter<CropAdapter.CropViewHolder> {

    private final List<Crop> cropList;
    private final Context context;

    public CropAdapter(List<Crop> cropList, Context context) {
        this.cropList = cropList;
        this.context = context;
    }

    @NonNull
    @Override
    public CropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.crop_card, parent, false);
        return new CropViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CropViewHolder holder, int position) {
        Crop crop = cropList.get(position);

        holder.cropNumber.setText("Crop " + (position + 1));
        holder.cropType.setText("Crop Type: " + crop.getCropType());
        holder.soilType.setText("Soil Type: " + crop.getSoilType());

        // Show fertilizer nutrients as comma-separated list
        List<String> nutrients = crop.getFertilizerNutrients();
        if (nutrients != null && !nutrients.isEmpty()) {
            holder.fertilizerList.setText("Fertilizers: " + TextUtils.join(", ", nutrients));
        } else {
            holder.fertilizerList.setText("Fertilizers: None");
        }

        // Handle Delete
        holder.deleteButton.setOnClickListener(v -> {
            String userPhone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
            String docId = crop.getDocumentId();

            if (userPhone != null && docId != null) {
                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(userPhone)
                        .collection("registeredCrops")
                        .document(docId)
                        .delete()
                        .addOnSuccessListener(unused -> {
                            cropList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, cropList.size());
                            Toast.makeText(context, "Crop deleted", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(context, "Failed to delete crop", Toast.LENGTH_SHORT).show()
                        );
            } else {
                Toast.makeText(context, "Error: Missing phone number or document ID", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle View More
        holder.viewMoreButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, cropdesc.class);
            intent.putExtra("cropType", crop.getCropType());
            intent.putExtra("soilType", crop.getSoilType());
            context.startActivity(intent);
        });

        // Handle Buy Now (open fertilizer recommendation page)
        holder.buyNowButton.setOnClickListener(v -> {
            Intent intent = new Intent(context,FertilizerActivity.class);
            intent.putStringArrayListExtra("nutrients", new ArrayList<>(nutrients));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cropList.size();
    }

    static class CropViewHolder extends RecyclerView.ViewHolder {
        TextView cropNumber, soilType, cropType, fertilizerList, viewMoreButton, buyNowButton;
        ImageView deleteButton;

        public CropViewHolder(@NonNull View itemView) {
            super(itemView);
            cropNumber = itemView.findViewById(R.id.crop_number);
            soilType = itemView.findViewById(R.id.soil_type);
            cropType = itemView.findViewById(R.id.crop_type);
            fertilizerList = itemView.findViewById(R.id.fertilizer_list);
            viewMoreButton = itemView.findViewById(R.id.view_more);
            buyNowButton = itemView.findViewById(R.id.buy_now); // You must add this TextView in crop_card.xml
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
