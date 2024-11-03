package com.example.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.databinding.FragmentProductFormBinding;

public class ProductFormFragment extends Fragment {
    private ProductViewModel viewModel;
    private FragmentProductFormBinding binding;
    private int selectedImageResource = R.drawable.baseline_image_24;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductFormBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
        viewModel.getSelectedProduct().observe(getViewLifecycleOwner(), this::populateForm);

        binding.buttonShowImages.setOnClickListener(v -> showImageSelectionDialog());

        binding.buttonConfirm.setOnClickListener(v -> {
            if (isValidInput()) {
                int code = Integer.parseInt(binding.editTextCode.getText().toString());
                String name = binding.editTextName.getText().toString();
                String description = binding.editTextDescription.getText().toString();
                float price = Float.parseFloat(binding.editTextPrice.getText().toString());

                Product product = new Product(code, name, description, price, selectedImageResource);

                if (viewModel.getSelectedProduct().getValue() != null) {
                    viewModel.updateProduct(product);
                    clearForm();
                    viewModel.clearSelectedProduct();
                } else {
                    viewModel.addProduct(product);
                    clearForm();
                }
                ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
                viewPager.setCurrentItem(0, true);
            }
        });

        return binding.getRoot();
    }

    private void showImageSelectionDialog() {
        final String[] imageOptions = {"Bacon", "Chicken", "Ranch", "Beef", "Berry"};
        final int[] imageResources = {
                R.drawable.bacon_wrapped,
                R.drawable.bbq_chicken,
                R.drawable.bbq_ranch,
                R.drawable.beef_stir_fry,
                R.drawable.berry_blast
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Product Image")
                .setItems(imageOptions, (dialog, which) -> {
                    selectedImageResource = imageResources[which];
                    binding.addProductImage.setImageResource(selectedImageResource);
                });
        builder.show();
    }

    private void populateForm(Product product) {
        if (product != null) {
            binding.setProduct(product);
            selectedImageResource = product.getImageResId();
            binding.addProductImage.setImageResource(selectedImageResource);
        } else {
            clearForm();
        }
    }

    private void clearForm() {
        binding.editTextCode.setText("");
        binding.editTextName.setText("");
        binding.editTextDescription.setText("");
        binding.editTextPrice.setText("");
        selectedImageResource = R.drawable.baseline_image_24;
        binding.addProductImage.setImageResource(selectedImageResource);
    }

    private boolean isValidInput() {
        return !binding.editTextCode.getText().toString().isEmpty() &&
                !binding.editTextName.getText().toString().isEmpty() &&
                !binding.editTextDescription.getText().toString().isEmpty() &&
                !binding.editTextPrice.getText().toString().isEmpty();
    }
}
