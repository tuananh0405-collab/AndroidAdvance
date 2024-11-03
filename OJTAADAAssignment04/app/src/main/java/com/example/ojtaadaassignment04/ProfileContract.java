package com.example.ojtaadaassignment04;

public interface ProfileContract {

    interface Model {
        UserProfile getUserProfile();

        void saveUserProfile(UserProfile userProfile);
    }

    interface View {
        void showUserProfile(UserProfile userProfile);

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

        void onSaveButtonClicked(UserProfile userProfile);

        void onCancelButtonClicked();
    }
}
