package com.example.ojtaadaassignment04.mvp.presenter;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.ojtaadaassignment04.mvp.model.User;
import com.example.ojtaadaassignment04.mvp.view.MainView;


public class MainPresenter {
    private MainView view;
    private Context context;
    private User user;

    public MainPresenter(MainView view, Context context) {
        this.view = view;
        this.context = context;
        this.user = new User();
    }

    public void loadUserData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "thang nguyen");
        String email = sharedPreferences.getString("email", "abc@abc.com");
        String dob = sharedPreferences.getString("dob", "2015/11/27");
        boolean isMale = sharedPreferences.getBoolean("isMale", false);
        String avatarBase64 = sharedPreferences.getString("avatar", null);

        if (avatarBase64 != null) {
            byte[] decodedString = Base64.decode(avatarBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            user.setAvt(decodedByte);
        }

        user.setName(name);
        user.setEmail(email);
        user.setDob(dob);
        user.setMale(isMale);

        view.showUserData(user);
    }
}