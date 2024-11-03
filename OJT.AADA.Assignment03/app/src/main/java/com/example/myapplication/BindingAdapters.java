package com.example.myapplication;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {
    @BindingAdapter("app:imageResId")
    public static void setImageResource(ImageView imageView, int imageResId) {
        imageView.setImageResource(imageResId);
    }
}
