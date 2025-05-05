package com.example.krishimitra;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Crop {
    private String soilType;
    private String cropType;

    @ServerTimestamp
    private Date timestamp; // For sorting

    public Crop() {
        // Firestore requires empty constructor
    }

    public Crop(String soilType, String cropType) {
        this.soilType = soilType;
        this.cropType = cropType;
    }

    public String getSoilType() {
        return soilType;
    }

    public String getCropType() {
        return cropType;
    }
}
