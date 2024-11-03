package com.example.ojtaadaassignment04;

import android.content.Context;
import android.content.SharedPreferences;

public class ProfileModel implements ProfileContract.Model {

    private static final String PREFS_NAME = "user_profile_prefs";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_BIRTHDAY = "birthday";
    private static final String KEY_GENDER = "gender";

    private SharedPreferences sharedPreferences;

    public ProfileModel(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public UserProfile getUserProfile() {
        String avatar = sharedPreferences.getString(KEY_AVATAR, "");
        String fullName = sharedPreferences.getString(KEY_FULL_NAME, "");
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String birthday = sharedPreferences.getString(KEY_BIRTHDAY, "");
        String gender = sharedPreferences.getString(KEY_GENDER, "");
        return new UserProfile(avatar, fullName, email, birthday, gender);
    }

    @Override
    public void saveUserProfile(UserProfile userProfile) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AVATAR, userProfile.getAvatar());
        editor.putString(KEY_FULL_NAME, userProfile.getFullName());
        editor.putString(KEY_EMAIL, userProfile.getEmail());
        editor.putString(KEY_BIRTHDAY, userProfile.getBirthday());
        editor.putString(KEY_GENDER, userProfile.getGender());
        editor.apply();
    }
}
