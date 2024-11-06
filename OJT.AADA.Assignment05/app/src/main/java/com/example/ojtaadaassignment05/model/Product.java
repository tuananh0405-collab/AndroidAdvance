package com.example.ojtaadaassignment05.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.ojtaadaassignment05.R;

public class Product implements Parcelable {
    private int code;
    private String name;
    private String description;
    private float price;
    private int imageResId ;

    public Product() {
    }

    public Product(int code, String name, String description, float price, int imageResId) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResId = imageResId;
    }

    protected Product(Parcel in) {
        code = in.readInt();
        name = in.readString();
        description = in.readString();
        price = in.readFloat();
        imageResId = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeFloat(price);
        dest.writeInt(imageResId);
    }
}
