package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.databinding.FragmentProductListBinding;

import java.util.ArrayList;

public class ProductListFragment extends Fragment {

    private ProductViewModel viewModel;
    private FragmentProductListBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(inflater, container, false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ProductAdapter adapter = new ProductAdapter(new ArrayList<>(), this::onProductClicked);
        binding.recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
        viewModel.getProducts().observe(getViewLifecycleOwner(), adapter::setProducts);

        return binding.getRoot();
    }

    private void onProductClicked(Product product) {
        viewModel.selectProduct(product);
        ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
        viewPager.setCurrentItem(1, true);
    }
}
