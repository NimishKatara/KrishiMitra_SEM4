package com.example.krishimitra;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CropAdapter extends RecyclerView.Adapter<CropAdapter.CropViewHolder> {

    private final List<Crop> cropList;

    public CropAdapter(List<Crop> cropList) {
        this.cropList = cropList;
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
        holder.fertilizerList.setText("Ammonium, Sulphate, Nitrate");  // Placeholder
    }

    @Override
    public int getItemCount() {
        return cropList.size();
    }

    static class CropViewHolder extends RecyclerView.ViewHolder {
        TextView cropNumber, soilType, cropType, fertilizerList;

        public CropViewHolder(@NonNull View itemView) {
            super(itemView);
            cropNumber = itemView.findViewById(R.id.crop_number);
            soilType = itemView.findViewById(R.id.soil_type);
            cropType = itemView.findViewById(R.id.crop_type);
            fertilizerList = itemView.findViewById(R.id.fertilizer_list);
        }
    }
}
