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
import com.example.ojtaadaassignment05.databinding.FragmentAddBinding;
import com.example.ojtaadaassignment05.model.Product;
import com.example.ojtaadaassignment05.viewmodel.ProductViewModel;

public class AddFragment extends Fragment {

    FragmentAddBinding binding;
    ProductViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater, container, false);

        Product product = new Product();
        binding.setProduct(product);
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
        viewModel.getImageResId().observe(getViewLifecycleOwner(), resId -> {
            if (resId != null) {
                binding.ivProductImage.setImageResource(resId);
            }
        });
        viewModel.getAddProductCompleted().observe(getViewLifecycleOwner(), completed -> {
            if (Boolean.TRUE.equals(completed)) {
                NavController navController = Navigation.findNavController(requireView());
                navController.navigateUp();
                viewModel.resetAddProductCompleted();
            }
        });

        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

}