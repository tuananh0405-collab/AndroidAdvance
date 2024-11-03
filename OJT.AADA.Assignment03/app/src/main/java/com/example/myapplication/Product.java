package com.example.myapplication;

public class Product {
    private int code;
    private String name;
    private String description;
    private float price;
    private int imageResId;

    public Product(int code, String name, String description, float price, int imageResId) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResId = imageResId;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
