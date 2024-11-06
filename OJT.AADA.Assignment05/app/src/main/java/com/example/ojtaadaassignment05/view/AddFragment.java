package com.example.ojtaadaassignment05.view;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
        binding.btnSelectImage.setOnClickListener(v -> {
            chooseImage();
        });

        binding.btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidInput()) {
                    viewModel.addProduct(getProduct());
                }
                requireActivity().onBackPressed();
            }
        });

        return binding.getRoot();
    }

    private boolean isValidInput() {
        return !binding.edtProductCode.getText().toString().isEmpty() &&
                !binding.edtProductName.getText().toString().isEmpty() &&
                !binding.edtProductDescription.getText().toString().isEmpty() &&
                !binding.edtProductPrice.getText().toString().isEmpty();
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
        product.setCode(Integer.parseInt(binding.edtProductCode.getText().toString()));
        product.setName(binding.edtProductName.getText().toString());
        product.setDescription(binding.edtProductDescription.getText().toString());
        product.setPrice(Float.parseFloat(binding.edtProductPrice.getText().toString()));
        product.setImageResId(binding.ivProductImage.getId());
        return product;


    }
}