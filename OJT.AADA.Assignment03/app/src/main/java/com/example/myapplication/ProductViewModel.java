package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {
    private MutableLiveData<List<Product>> products = new MutableLiveData<>();
    private final MutableLiveData<Product> selectedProduct = new MutableLiveData<>();

    public ProductViewModel() {
        products = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        List<Product> currentProducts = products.getValue();
        currentProducts.add(0, product);
        products.setValue(currentProducts);
    }

    public void updateProduct(Product updatedProduct) {
        List<Product> currentProducts = products.getValue();
        int index = currentProducts.indexOf(selectedProduct.getValue());
        if (index >= 0) {
            currentProducts.set(index, updatedProduct);
            products.setValue(currentProducts);
        }
    }

    public LiveData<Product> getSelectedProduct() {
        return selectedProduct;
    }

    public void selectProduct(Product product) {
        selectedProduct.setValue(product);
    }

    public void clearSelectedProduct() {
        selectedProduct.setValue(null);
    }
}
