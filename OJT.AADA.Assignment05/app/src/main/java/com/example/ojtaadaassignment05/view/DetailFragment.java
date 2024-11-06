package com.example.ojtaadaassignment05.view;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ojtaadaassignment05.R;
import com.example.ojtaadaassignment05.databinding.FragmentDetailBinding;
import com.example.ojtaadaassignment05.model.Product;
import com.example.ojtaadaassignment05.viewmodel.ProductViewModel;


public class DetailFragment extends Fragment {

    FragmentDetailBinding binding;
    ProductViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
        viewModel.getSelectedProduct().observe(getViewLifecycleOwner(), product -> {
            if (product != null) {
                binding.setProduct(product);
            }
        });
        viewModel.getImageResId().observe(getViewLifecycleOwner(), resId -> {
            if (resId != null) {
                binding.ivProductImage.setImageResource(resId);
            }
        });

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.getUpdateCompleted().observe(getViewLifecycleOwner(), completed -> {
            if (completed != null && completed) {
                NavController navController = Navigation.findNavController(requireView());
                navController.navigateUp();
                viewModel.resetUpdateStatus();
            }
        });
        return binding.getRoot();
    }

}