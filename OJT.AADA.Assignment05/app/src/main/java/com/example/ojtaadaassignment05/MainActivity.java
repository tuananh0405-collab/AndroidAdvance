package com.example.ojtaadaassignment05;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.ojtaadaassignment05.databinding.ActivityMainBinding;
import com.example.ojtaadaassignment05.viewmodel.ProductViewModel;
import com.example.ojtaadaassignment05.viewmodel.ProductViewModelFactory;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    NavController navController;
    ProductViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        ProductViewModelFactory factory = new ProductViewModelFactory(this);
//        viewModel = new ViewModelProvider(this, factory).get(ProductViewModel.class);

        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolBar);
        // <- for navigating
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // icon Home
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout);
        NavigationUI.setupWithNavController(binding.navigationView, navController);

        binding.navigationView.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            binding.drawerLayout.closeDrawers();
            int id = item.getItemId();
            if (id == R.id.drawer_list_product) {
                navController.navigate(R.id.listFragment);
            } else if (id == R.id.drawer_add_product) {
                navController.navigate(R.id.addFragment);
            }
            return true;
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), binding.drawerLayout);
    }
}