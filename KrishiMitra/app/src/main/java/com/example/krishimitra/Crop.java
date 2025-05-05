package com.example.krishimitra;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;
import java.util.List;

public class Crop {
    private String soilType;
    private String cropType;
    private List<String> fertilizerNutrients;

    private String documentId;  // Added field to store Firestore document ID

    @ServerTimestamp
    private Date timestamp; // For sorting

    public Crop() {
        // Firestore requires empty constructor
    }

    public Crop(String soilType, String cropType) {
        this.soilType = soilType;
        this.cropType = cropType;
    }

    public Crop(String soilType, String cropType, String documentId) {
        this.soilType = soilType;
        this.cropType = cropType;
        this.documentId = documentId;
    }

    public String getSoilType() {
        return soilType;
    }

    public String getCropType() {
        return cropType;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    public List<String> getFertilizerNutrients() {
        return fertilizerNutrients;
    }
    public void setFertilizerNutrients(List<String> fertilizerNutrients) {
        this.fertilizerNutrients = fertilizerNutrients;
    }

}
