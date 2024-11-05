package com.example.mvp_practice.contract;

import com.example.mvp_practice.model.User;

public interface ProfileContract {
    interface Model {
        User getUser();

        void saveUser(User user);
    }

    interface View {
        void showUserProfile(User user);

        void showPopupMenu();

        void openCamera();

        void openGallery();

        void showError(String error);

        void backToPreviousScreen();

        void openNavigationWithUserProfile();
    }

    interface Presenter {
        void loadUserProfile();

        void onAvatarClicked();

        void onCameraSelected();

        void onGallerySelected();

        void onSaveButtonClicked(User user);

        void onCancelButtonClicked();
    }
}
