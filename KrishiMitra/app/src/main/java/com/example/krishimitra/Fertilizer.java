package com.example.krishimitra;
public class Fertilizer {
    private String name;
    private String soilType;
    private String cropType;
    private double dosagePerAcre;

    public Fertilizer(String name, String soilType, String cropType, double dosagePerAcre) {
        this.name = name;
        this.soilType = soilType;
        this.cropType = cropType;
        this.dosagePerAcre = dosagePerAcre;
    }

    // getters
    public String getName() { return name; }
    public String getSoilType() { return soilType; }
    public String getCropType() { return cropType; }
    public double getDosagePerAcre() { return dosagePerAcre; }
}
