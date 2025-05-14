package com.example.krishimitra;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        double dosagePerSqM = fertilizer.getDosagePerAcre() / 4046.86;

        holder.fertilizerName.setText(fertilizer.getName());
        holder.fertilizerNutrient.setText("For " + fertilizer.getCropType() + " on " + fertilizer.getSoilType());
        holder.fertilizerWeightPrice.setText(String.format("Dosage: %.4f g/m²", dosagePerSqM));
    }

    @Override
    public int getItemCount() {
        return fertilizerList.size();
    }

    static class FertilizerViewHolder extends RecyclerView.ViewHolder {
        TextView fertilizerName, fertilizerNutrient, fertilizerWeightPrice;

        public FertilizerViewHolder(@NonNull View itemView) {
            super(itemView);
            fertilizerName = itemView.findViewById(R.id.fertilizerName);
            fertilizerNutrient = itemView.findViewById(R.id.fertilizerNutrient);
            fertilizerWeightPrice = itemView.findViewById(R.id.fertilizerWeightPrice);
        }
    }
}
