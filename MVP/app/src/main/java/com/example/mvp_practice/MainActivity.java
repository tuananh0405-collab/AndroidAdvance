package com.example.mvp_practice;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mvp_practice.model.User;
import com.example.mvp_practice.model.UserProfileModel;
import com.example.mvp_practice.view.ProfileFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements ProfileFragment.ProfileUpdateListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;

    ImageView headerImage;
    TextView tvName, tvEmail, tvDOB, tvGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.navigation_view);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        headerImage = headerView.findViewById(R.id.headerImage);
        tvName = headerView.findViewById(R.id.tvName);
        tvEmail = headerView.findViewById(R.id.tvEmail);
        tvDOB = headerView.findViewById(R.id.tvDOB);
        tvGender = headerView.findViewById(R.id.tvGender);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.drawer_button) {
                showProfileFragment();
                drawerLayout.closeDrawers();
            }
            return false;
        });

        // initiate user and get from shared preference
        User user = new UserProfileModel(this).getUser();
        //updateInNav();
        updateUserProfileInNavigation(user);
    }

    private void showProfileFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ProfileFragment())
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void updateUserProfileInNavigation(User user) {
        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());
        tvDOB.setText(user.getDob());
        tvGender.setText(user.isMale() ? "Male" : "Female");
        headerImage.setImageBitmap(user.getAvt());
    }
}