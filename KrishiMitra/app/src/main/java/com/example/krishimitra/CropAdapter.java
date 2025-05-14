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
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Crop crop);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

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

        holder.cropNumber.setText("Crop: " + crop.getCropType());
        holder.cropType.setText("Crop Type: " + crop.getCropType());
        holder.soilType.setText("Soil Type: " + crop.getSoilType());

        List<String> nutrients = crop.getFertilizerNutrients();
        if (nutrients != null && !nutrients.isEmpty()) {
            holder.fertilizerList.setText("Fertilizers:\n" + formatFertilizerList(nutrients));
        } else {
            holder.fertilizerList.setText("Fertilizers: None");
        }

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


        holder.buyNowButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, FertilizerActivity.class);
            intent.putExtra("soilType", crop.getSoilType());
            intent.putExtra("cropType", crop.getCropType());
            context.startActivity(intent);

        });

        // NEW: Call item click listener if defined
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(crop);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cropList.size();
    }

    private String formatFertilizerList(List<String> nutrients) {
        return TextUtils.join("\n", nutrients);
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
            buyNowButton = itemView.findViewById(R.id.buy_now);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
