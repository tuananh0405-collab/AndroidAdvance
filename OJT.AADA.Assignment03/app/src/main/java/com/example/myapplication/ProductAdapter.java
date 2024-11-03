package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.ItemProductBinding;

import java.util.List;
import java.util.function.Consumer;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products;
    private final Consumer<Product> onProductClick;

    public ProductAdapter(List<Product> products, Consumer<Product> onProductClick) {
        this.products = products;
        this.onProductClick = onProductClick;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding, onProductClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductBinding binding;
        private final Consumer<Product> onProductClick;

        public ProductViewHolder(ItemProductBinding binding, Consumer<Product> onProductClick) {
            super(binding.getRoot());
            this.binding = binding;
            this.onProductClick = onProductClick;
        }

        public void bind(Product product) {
            binding.setProduct(product);
            binding.getRoot().setOnClickListener(v -> onProductClick.accept(product));
            binding.executePendingBindings();
        }
    }
}
