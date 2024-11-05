package com.example.mvvm_practice.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvm_practice.MainActivity;
import com.example.mvvm_practice.model.User;

import java.io.ByteArrayOutputStream;

public class UserViewModel extends ViewModel {

    private MutableLiveData<User> user = new MutableLiveData<>(new User());
    private MutableLiveData<Boolean> btnDone = new MutableLiveData<>();
    private MutableLiveData<Bitmap> avtBitmap = new MutableLiveData<>();
    private UserProfileListener listener;
    private Context context;
    private MutableLiveData<Boolean> closeFragment = new MutableLiveData<>();

    public LiveData<Boolean> getCloseFragment() {
        return closeFragment;
    }


    public interface UserProfileListener {
        void onUpdateProfile(User updatedUser);
    }

    public UserViewModel() {
    }

    public void setContext(Context context) {
        this.context = context;
        loadUserFromPreferences();
    }

    private void loadUserFromPreferences() {
        SharedPreferences prefs = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        User userData = new User();
        userData.setFullName(prefs.getString("fullName", ""));
        userData.setEmail(prefs.getString("email", ""));
        userData.setBirthday(prefs.getString("birthday", ""));
        userData.setGender(prefs.getString("gender", "Male"));
        user.setValue(userData);
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<Boolean> getBtnDone() {
        return btnDone;
    }

    public LiveData<Bitmap> getAvatarBitmap() {
        return avtBitmap;
    }

    public void setListener(UserProfileListener listener) {
        this.listener = listener;
    }

    public void saveUserProfile(User user) {
        this.user.setValue(user);
        saveUserToPreferences(user);
        btnDone.setValue(true);
    }

    public void onDoneClick() {
        saveUserProfile(user.getValue());
        if (listener != null) {
            listener.onUpdateProfile(user.getValue());
        }
        closeFragment.setValue(true);
    }

    private void saveUserToPreferences(User user) {
        SharedPreferences prefs = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("fullName", user.getFullName());
        editor.putString("email", user.getEmail());
        editor.putString("birthday", user.getBirthday());
        editor.putString("gender", user.getGender());
        editor.apply();
    }

    public void saveAvatarToPreferences(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        SharedPreferences prefs = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("avatar", encodedImage);
        user.getValue().setAvatar(encodedImage);
        editor.apply();
    }

    public void onCancelClick() {
        btnDone.setValue(false);
        closeFragment.setValue(true);
    }


}
