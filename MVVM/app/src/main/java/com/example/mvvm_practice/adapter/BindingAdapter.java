package com.example.mvvm_practice.adapter;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.databinding.InverseMethod;

public class BindingAdapter {
    @InverseMethod("stringToBoolean")
    public static String booleanToString(boolean isChecked) {
        return isChecked ? "male" : "female";
    }

    public static boolean stringToBoolean(String gender) {
        return "male".equals(gender);
    }

    @androidx.databinding.BindingAdapter("imageBitmap")
    public static void setImageBitmap(ImageView imageView, Bitmap bitmap) {
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
