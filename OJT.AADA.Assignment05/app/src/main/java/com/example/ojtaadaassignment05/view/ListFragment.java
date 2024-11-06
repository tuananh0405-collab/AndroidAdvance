package com.example.ojtaadaassignment05.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ojtaadaassignment05.adapter.ProductAdapter;
import com.example.ojtaadaassignment05.databinding.FragmentListBinding;
import com.example.ojtaadaassignment05.viewmodel.ProductViewModel;
import com.example.ojtaadaassignment05.viewmodel.ProductViewModelFactory;

import java.util.ArrayList;


public class ListFragment extends Fragment {

    FragmentListBinding binding;
    ProductViewModel viewModel;
    NavController navController;
    ProductAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);

        binding.rcvProduct.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductAdapter(new ArrayList<>(), viewModel);
        binding.rcvProduct.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
//        ProductViewModelFactory factory = new ProductViewModelFactory(getContext());
//        viewModel = new ViewModelProvider(this, factory).get(ProductViewModel.class);

        viewModel.getProducts().observe(getViewLifecycleOwner(), adapter::setProducts);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
    }

}