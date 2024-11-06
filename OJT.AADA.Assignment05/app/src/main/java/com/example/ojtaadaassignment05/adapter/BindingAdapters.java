package com.example.ojtaadaassignment05.adapter;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

public class BindingAdapters {

    @BindingAdapter("imageResId")
    public static void setImageResource(ImageView imageView, int imageResId) {
        imageView.setImageResource(imageResId);
    }

    @BindingAdapter("android:text")
    public static void setFloatInText(EditText view, float value) {
        if (!view.getText().toString().equals(String.valueOf(value))) {
            view.setText(String.valueOf(value));
        }
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static float getFloatFromText(EditText view) {
        try {
            return Float.parseFloat(view.getText().toString());
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    @BindingAdapter("android:text")
    public static void setIntInText(EditText view, int value) {
        if (!view.getText().toString().equals(String.valueOf(value))) {
            view.setText(String.valueOf(value));
        }
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static int getIntFromText(EditText view) {
        try {
            return Integer.parseInt(view.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    @BindingAdapter("android:text")
    public static void setFloatInText(TextView view, float value) {
        if (!view.getText().toString().equals(String.valueOf(value))) {
            view.setText(String.valueOf(value));
        }
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static int getFloatFromText(TextView view) {
        try {
            return Integer.parseInt(view.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
