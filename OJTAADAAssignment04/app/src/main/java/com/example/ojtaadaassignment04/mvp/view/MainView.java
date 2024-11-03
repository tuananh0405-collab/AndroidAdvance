package com.example.ojtaadaassignment04.mvp.view;

import com.example.ojtaadaassignment04.mvp.model.User;

public interface MainView {
    void showUserData(User user);
    void showErrorMessage(String message);
}