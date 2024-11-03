package com.example.ojtaadaassignment04.mvp.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.ojtaadaassignment04.R;
import com.example.ojtaadaassignment04.mvp.model.User;
import com.example.ojtaadaassignment04.mvp.presenter.MainPresenter;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements MainView, EditProfileFragment.OnProfileEditedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private MainPresenter presenter;
    private User user;

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

        presenter = new MainPresenter(this, this);
        presenter.loadUserData();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.drawer_button) {
                // Hiển thị EditProfileFragment khi mục được chọn
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                EditProfileFragment fragment = EditProfileFragment.newInstance(user);
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            drawerLayout.closeDrawer(navigationView);
            return true;
        });
    }

    @Override
    public void showUserData(User user) {
        this.user = user;

        // Hiển thị thông tin trong NavigationView
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.drawer_email);
        menuItem.setTitle(user.getEmail());
        menuItem = menu.findItem(R.id.drawer_dob);
        menuItem.setTitle(user.getDob());
        if (user.isMale()) {
            menuItem = menu.findItem(R.id.drawer_female);
            menuItem.setTitle("Male");
        } else {
            menuItem = menu.findItem(R.id.drawer_female);
            menuItem.setTitle("Female");
        }
        View headerView = navigationView.getHeaderView(0);
        TextView tvName = headerView.findViewById(R.id.tvName);
        tvName.setText(user.getName());
        ImageView headerImage = headerView.findViewById(R.id.headerImage);
        if (user.getAvt() != null) {
            headerImage.setImageBitmap(user.getAvt());
        }
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Triển khai OnProfileEditedListener
    @Override
    public void onProfileEdited(User updatedUser) {
        this.user = updatedUser; // Cập nhật thông tin người dùng

        // Cập nhật thông tin hiển thị trong NavigationView
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.drawer_email);
        menuItem.setTitle(user.getEmail());
        menuItem = menu.findItem(R.id.drawer_dob);
        menuItem.setTitle(user.getDob());
        if (user.isMale()) {
            menuItem = menu.findItem(R.id.drawer_female);
            menuItem.setTitle("Male");
        } else {
            menuItem = menu.findItem(R.id.drawer_female);
            menuItem.setTitle("Female");
        }
        View headerView = navigationView.getHeaderView(0);
        TextView tvName = headerView.findViewById(R.id.tvName);
        tvName.setText(user.getName());
        ImageView headerImage = headerView.findViewById(R.id.headerImage);
        if (user.getAvt() != null) {
            headerImage.setImageBitmap(user.getAvt());
        }

        Toast.makeText(this, "Profile updated in MainActivity", Toast.LENGTH_SHORT).show();
    }
}
