package com.example.mvp_practice.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.mvp_practice.contract.ProfileContract;
import com.example.mvp_practice.utils.BitmapUtils;

public class UserProfileModel implements ProfileContract.Model {

    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_AVATAR = "avt";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_BIRTHDAY = "dob";
    private static final String KEY_GENDER = "isMale";

    private SharedPreferences sharedPreferences;

    public UserProfileModel(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public User getUser() {
        String avt = sharedPreferences.getString(KEY_AVATAR, "");
        String name = sharedPreferences.getString(KEY_NAME, "");
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String dob = sharedPreferences.getString(KEY_BIRTHDAY, "");
        String isMale = sharedPreferences.getString(KEY_GENDER, "");

        Bitmap decodedByte = null;
        if (avt != null) {
            byte[] decodedString = Base64.decode(avt, Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }

        return new User( name, email, dob, isMale.equals("Male"), decodedByte);
    }

    @Override
    public void saveUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_AVATAR, BitmapUtils.bitmapToBase64(user.getAvt()));
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_BIRTHDAY, user.getDob());
        editor.putString(KEY_GENDER, user.isMale() ? "Male" : "Female");

        editor.apply();
    }
}
