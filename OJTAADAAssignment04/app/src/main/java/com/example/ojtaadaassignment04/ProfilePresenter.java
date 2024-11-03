package com.example.ojtaadaassignment04;

public class ProfilePresenter implements ProfileContract.Presenter {
    private ProfileContract.View view;
    private ProfileContract.Model model;

    public ProfilePresenter(ProfileContract.View view, ProfileContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void loadUserProfile() {
        UserProfile userProfile = model.getUserProfile();
        view.showUserProfile(userProfile);
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
    public void onSaveButtonClicked(UserProfile userProfile) {
        model.saveUserProfile(userProfile);
        view.openNavigationWithUserProfile();
    }

    @Override
    public void onCancelButtonClicked() {
        view.backToPreviousScreen();
    }
}
