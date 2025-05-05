package com.example.krishimitra;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FertilizerAdapter extends RecyclerView.Adapter<FertilizerAdapter.FertilizerViewHolder> {

    private List<Fertilizer> fertilizerList;

    public FertilizerAdapter(List<Fertilizer> fertilizerList) {
        this.fertilizerList = fertilizerList;
    }

    @NonNull
    @Override
    public FertilizerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fertilizer, parent, false);
        return new FertilizerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FertilizerViewHolder holder, int position) {
        Fertilizer fertilizer = fertilizerList.get(position);
        holder.fertilizerName.setText(fertilizer.getName());
        holder.fertilizerNutrient.setText(fertilizer.getNutrient());
        holder.fertilizerWeightPrice.setText(fertilizer.getWeight() + " - Rs " + fertilizer.getPrice());
        holder.fertilizerImage.setImageResource(fertilizer.getImageResId());
    }

    @Override
    public int getItemCount() {
        return fertilizerList.size();
    }

    static class FertilizerViewHolder extends RecyclerView.ViewHolder {
        ImageView fertilizerImage;
        TextView fertilizerName, fertilizerNutrient, fertilizerWeightPrice;

        public FertilizerViewHolder(@NonNull View itemView) {
            super(itemView);
            fertilizerImage = itemView.findViewById(R.id.fertilizerImage);
            fertilizerName = itemView.findViewById(R.id.fertilizerName);
            fertilizerNutrient = itemView.findViewById(R.id.fertilizerNutrient);
            fertilizerWeightPrice = itemView.findViewById(R.id.fertilizerWeightPrice);
        }
    }
}