package com.example.krishimitra;

public class Crop {
    private String soilType;
    private String cropType;

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
