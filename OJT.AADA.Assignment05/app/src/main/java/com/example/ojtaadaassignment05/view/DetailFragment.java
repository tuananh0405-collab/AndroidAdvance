package com.example.ojtaadaassignment05.view;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ojtaadaassignment05.R;
import com.example.ojtaadaassignment05.databinding.FragmentDetailBinding;
import com.example.ojtaadaassignment05.model.Product;
import com.example.ojtaadaassignment05.viewmodel.ProductViewModel;
import com.example.ojtaadaassignment05.viewmodel.ProductViewModelFactory;


public class DetailFragment extends Fragment {

    FragmentDetailBinding binding;
    ProductViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
//        ProductViewModelFactory factory = new ProductViewModelFactory(getContext());
//        viewModel = new ViewModelProvider(this, factory).get(ProductViewModel.class);

        viewModel.getSelectedProduct().observe(getViewLifecycleOwner(), product -> {
            if (product != null) {
                binding.setProduct(product);
            }
        });

        binding.ivProductImage.setOnClickListener(view -> {
            chooseImage();
        });

        binding.btnUpdate.setOnClickListener(v -> {
            if (isValidInput()) {
                if (viewModel.getSelectedProduct().getValue() != null) {
                    viewModel.updateProduct(getProduct());
                    clearForm();
                    viewModel.clearSelectedProduct();
                }
            }
            requireActivity().onBackPressed();
        });
        return binding.getRoot();
    }

    private void clearForm() {
        binding.setProduct(new Product());
    }

    private boolean isValidInput() {
        return !binding.tvProductName.getText().toString().isEmpty() &&
                !binding.tvProductDescription.getText().toString().isEmpty() &&
                !binding.tvProductPrice.getText().toString().isEmpty();
    }
    private void chooseImage() {
        final String[] imageOptions = {"Bacon", "Chicken", "Ranch", "Beef", "Berry"};
        final int[] imageResources = {R.drawable.bacon_wrapped, R.drawable.bbq_chicken, R.drawable.bbq_ranch, R.drawable.beef_stir_fry, R.drawable.berry_blast};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Product Image")
                .setItems(imageOptions, (dialog, which) -> {
                    binding.ivProductImage.setImageResource(imageResources[which]);
                    binding.ivProductImage.setId(imageResources[which]);
                });
        builder.show();
    }
    private Product getProduct() {
        Product product = new Product();
        product.setCode(Integer.parseInt(binding.tvProductCode.getText().toString()));
        product.setName(binding.tvProductName.getText().toString());
        product.setDescription(binding.tvProductDescription.getText().toString());
        product.setPrice(Float.parseFloat(binding.tvProductPrice.getText().toString()));
        product.setImageResId(binding.ivProductImage.getId());
        return product;


    }
}