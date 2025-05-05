package com.example.krishimitra;

public class Fertilizer {
    private String name;
    private String nutrient;
    private String weight;
    private int price;
    private int imageResId;

    public Fertilizer(String name, String nutrient, String weight, int price, int imageResId) {
        this.name = name;
        this.nutrient = nutrient;
        this.weight = weight;
        this.price = price;
        this.imageResId = imageResId;
    }

    public String getName() { return name; }
    public String getNutrient() { return nutrient; }
    public String getWeight() { return weight; }
    public int getPrice() { return price; }
    public int getImageResId() { return imageResId; }
}
