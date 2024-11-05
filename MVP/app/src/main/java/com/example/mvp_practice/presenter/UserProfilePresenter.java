package com.example.mvp_practice.presenter;

import com.example.mvp_practice.contract.ProfileContract;
import com.example.mvp_practice.model.User;
import com.example.mvp_practice.model.UserProfileModel;

public class UserProfilePresenter implements ProfileContract.Presenter {
    private ProfileContract.View view;
    private ProfileContract.Model model;

    public UserProfilePresenter(ProfileContract.View view, ProfileContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void loadUserProfile() {
        User user = model.getUser();
        view.showUserProfile(user);
    }

    @Override
    public void onAvatarClicked() {
        view.showPopupMenu();
    }

    @Override
    public void onCameraSelected() {
        view.openCamera();
    }

    @Override
    public void onGallerySelected() {
        view.openGallery();
    }

    @Override
    public void onSaveButtonClicked(User user) {
        model.saveUser(user);
        view.openNavigationWithUserProfile();
    }

    @Override
    public void onCancelButtonClicked() {
        view.backToPreviousScreen();
    }
}
