package com.example.ojtaadaassignment04;


import static com.example.ojtaadaassignment04.EditProfileFragment.decodeBase64ToImage;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements EditProfileFragment.ProfileUpdateListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private ImageView avatarImageView;
    private TextView fullNameTextView;
    private TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        avatarImageView = headerView.findViewById(R.id.nav_avatar);
        fullNameTextView = headerView.findViewById(R.id.nav_full_name);
        emailTextView = headerView.findViewById(R.id.nav_email);


        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_edit_profile) {
                showEditProfileFragment();
                drawerLayout.closeDrawers();
                return true;
            } else {
                // Handle other menu items here
                return false;
            }
        });

        UserProfile initialUserProfile = new ProfileModel(this).getUserProfile();
        Log.d("TAG", "onCreate: " + initialUserProfile.getFullName() + "/" + initialUserProfile.getEmail());
        updateUserProfileInNavigation(initialUserProfile);

    }

    private void showEditProfileFragment() {
        EditProfileFragment fragment = new EditProfileFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void updateUserProfileInNavigation(UserProfile userProfile) {
        avatarImageView.setImageBitmap(decodeBase64ToImage(userProfile.getAvatar()));
        fullNameTextView.setText(userProfile.getFullName());
        emailTextView.setText(userProfile.getEmail());
    }
}