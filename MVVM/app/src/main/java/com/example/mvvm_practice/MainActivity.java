package com.example.mvvm_practice;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.mvvm_practice.databinding.ActivityMainBinding;
import com.example.mvvm_practice.databinding.NavHeaderBinding;
import com.example.mvvm_practice.model.User;
import com.example.mvvm_practice.view.ProfileFragment;
import com.example.mvvm_practice.viewmodel.UserViewModel;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements UserViewModel.UserProfileListener {

    ActivityMainBinding binding;
    UserViewModel viewModel;
    ActionBarDrawerToggle toggle;
    View headerView;
    NavHeaderBinding navHeaderBinding;
    ProfileFragment profileFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.setContext(this);
        viewModel.setContext(this);

        setSupportActionBar(binding.toolBar);
        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolBar,
                R.string.nav_open, R.string.nav_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        profileFragment = new ProfileFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, profileFragment)
                .hide(profileFragment)
                .commit();

        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.drawer_button) {
                    getSupportFragmentManager().beginTransaction()
                            .show(profileFragment)
//                            .addToBackStack(null)
                            .commit();
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
                return false;
            }
        });

        headerView = binding.navigationView.getHeaderView(0);
        navHeaderBinding = NavHeaderBinding.bind(headerView);

        viewModel.getUser().observe(this, updatedUser -> {
            if (updatedUser != null) {
                String avatar = updatedUser.getAvatar();
                Bitmap decodedAvatar = decodeAvatarFromPreferences(avatar);
                if (decodedAvatar != null) {
                    navHeaderBinding.headerImage.setImageBitmap(decodedAvatar);
                }
                navHeaderBinding.setUser(updatedUser);
                navHeaderBinding.executePendingBindings();
            }
        });

        SharedPreferences prefs = getSharedPreferences("user_profile", MODE_PRIVATE);
        String avatar = prefs.getString("avatar", "");
        String fullName = prefs.getString("fullName", "null");
        String email = prefs.getString("email", "null");
        String birthday = prefs.getString("birthday", "null");
        String gender = prefs.getString("gender", "null");

        User user = new User();
        user.setAvatar(avatar);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setBirthday(birthday);
        user.setGender(gender);

        navHeaderBinding.setUser(user);
        Bitmap decodedAvatar = decodeAvatarFromPreferences(avatar);
        if (decodedAvatar != null) {
            navHeaderBinding.headerImage.setImageBitmap(decodedAvatar);
        }
        navHeaderBinding.executePendingBindings();

    }

    private Bitmap decodeAvatarFromPreferences(String encodedImage) {
        if (encodedImage == null || encodedImage.isEmpty()) {
            return null;
        }
        byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @Override
    public void onUpdateProfile(User updatedUser) {
        if (updatedUser != null) {
            viewModel.saveUserProfile(updatedUser);
        }
    }
}