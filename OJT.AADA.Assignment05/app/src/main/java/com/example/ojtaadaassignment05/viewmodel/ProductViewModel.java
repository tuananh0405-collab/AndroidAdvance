package com.example.ojtaadaassignment05.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ojtaadaassignment05.R;
import com.example.ojtaadaassignment05.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {
    private MutableLiveData<List<Product>> products;
    private MutableLiveData<Product> selectedProduct = new MutableLiveData<>();

    public ProductViewModel() {
        products = new MutableLiveData<>(new ArrayList<>());
        selectedProduct = new MutableLiveData<>(new Product());
    }

    public MutableLiveData<List<Product>> getProducts() {
        return products;
    }

    public MutableLiveData<Product> getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product product) {
        selectedProduct.setValue(product);
    }

    public void clearSelectedProduct() {
        selectedProduct.setValue(null);
    }

    public void addProduct(Product product) {
        List<Product> currentProducts = products.getValue();
        currentProducts.add(0, product);
        products.setValue(currentProducts);
    }

//    public void updateProduct(Product product) {
//        List<Product> currentProducts = products.getValue();
//        int index = currentProducts.indexOf(selectedProduct.getValue());
//        if (index != -1) {
//            currentProducts.set(index, product);
//            products.setValue(currentProducts);
//        }
//    }

    public void removeProduct(Product product) {
        List<Product> currentProducts = products.getValue();
        if (currentProducts.remove(product)) {
            products.setValue(currentProducts);
        }
    }

    public void onProductLongClicked(Product product, Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Remove Product")
                .setMessage("Are you sure you want to remove this product?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    removeProduct(product);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private MutableLiveData<Integer> imageResId = new MutableLiveData<>(R.drawable.baseline_image_24);

    public LiveData<Integer> getImageResId() {
        return imageResId;
    }

    public void showImgDialog(Context context) {
        final String[] imageOptions = {"Bacon", "Chicken", "Ranch", "Beef", "Berry"};
        final int[] imageResources = {R.drawable.bacon_wrapped, R.drawable.bbq_chicken, R.drawable.bbq_ranch, R.drawable.beef_stir_fry, R.drawable.berry_blast};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Product Image")
                .setItems(imageOptions, (dialog, which) -> {
                    imageResId.setValue(imageResources[which]);
                });
        builder.show();
    }

    private MutableLiveData<Boolean> addProductCompleted = new MutableLiveData<>();

    public LiveData<Boolean> getAddProductCompleted() {
        return addProductCompleted;
    }

    public void resetAddProductCompleted() {
        addProductCompleted.setValue(false);
    }

    public void onAddProductClicked(Product product) {
        if (product != null ) {
            product.setImageResId(imageResId.getValue());
            addProduct(product);
            addProductCompleted.setValue(true);
        }
    }

    private MutableLiveData<Boolean> updateCompleted = new MutableLiveData<>(false);
    public LiveData<Boolean> getUpdateCompleted() {
        return updateCompleted;
    }
    public void updateProduct(Product product) {
        List<Product> currentProducts = products.getValue();
        product.setImageResId(imageResId.getValue());
        if (currentProducts != null) {
            int index = currentProducts.indexOf(selectedProduct.getValue());
            if (index != -1) {
                currentProducts.set(index, product);
                products.setValue(currentProducts);
                updateCompleted.setValue(true);
            }
        }
    }

    public void resetUpdateStatus() {
        updateCompleted.setValue(false);
    }
}
