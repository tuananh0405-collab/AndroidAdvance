package com.example.ojtaadaassignment04;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditProfileFragment extends Fragment implements ProfileContract.View {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    private ImageView avatarImageView;
    private EditText fullNameEditText;
    private EditText emailEditText;
    private EditText birthdayEditText;
    private RadioGroup genderRadioGroup;

    private ProfileContract.Presenter presenter;
    private ProfileUpdateListener profileUpdateListener;

    public interface ProfileUpdateListener {
        void updateUserProfileInNavigation(UserProfile userProfile);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        avatarImageView = view.findViewById(R.id.avatarImageView);
        fullNameEditText = view.findViewById(R.id.fullNameEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        birthdayEditText = view.findViewById(R.id.birthdayEditText);
        genderRadioGroup = view.findViewById(R.id.genderRadioGroup);
        Button saveButton = view.findViewById(R.id.saveButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        presenter = new ProfilePresenter(this, new ProfileModel(getActivity()));

        avatarImageView.setOnClickListener(v -> presenter.onAvatarClicked());

        saveButton.setOnClickListener(v -> {
            UserProfile userProfile = new UserProfile(
                    encodeImageToBase64(((BitmapDrawable) avatarImageView.getDrawable()).getBitmap()),
                    fullNameEditText.getText().toString(),
                    emailEditText.getText().toString(),
                    birthdayEditText.getText().toString(),
                    ((RadioButton) view.findViewById(genderRadioGroup.getCheckedRadioButtonId())).getText().toString()
            );
            presenter.onSaveButtonClicked(userProfile);
        });

        cancelButton.setOnClickListener(v -> presenter.onCancelButtonClicked());

        presenter.loadUserProfile();
        return view;
    }

    @Override
    public void showUserProfile(UserProfile userProfile) {
        if (!userProfile.getAvatar().isEmpty()) {
            avatarImageView.setImageBitmap(decodeBase64ToImage(userProfile.getAvatar()));
        }
        fullNameEditText.setText(userProfile.getFullName());
        emailEditText.setText(userProfile.getEmail());
        birthdayEditText.setText(userProfile.getBirthday());
        if (userProfile.getGender().equals("Male")) {
            genderRadioGroup.check(R.id.radioMale);
        } else {
            genderRadioGroup.check(R.id.radioFemale);
        }

    }

    @Override
    public void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(), avatarImageView);
        popupMenu.getMenuInflater().inflate(R.menu.avatar_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_camera) {
                presenter.onCameraSelected();
                return true;
            } else if (item.getItemId() == R.id.menu_gallery) {
                presenter.onGallerySelected();
                return true;
            } else {
                return false;
            }

        });
        popupMenu.show();
    }

    @Override
    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void openGallery() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_GALLERY);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void backToPreviousScreen() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void openNavigationWithUserProfile() {
        UserProfile userProfile = new UserProfile(
                encodeImageToBase64(((BitmapDrawable) avatarImageView.getDrawable()).getBitmap()),
                fullNameEditText.getText().toString(),
                emailEditText.getText().toString(),
                birthdayEditText.getText().toString(),
                ((RadioButton) getView().findViewById(genderRadioGroup.getCheckedRadioButtonId())).getText().toString()
        );
        profileUpdateListener.updateUserProfileInNavigation(userProfile);
        backToPreviousScreen();
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap decodeBase64ToImage(String base64Str) {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                avatarImageView.setImageBitmap(imageBitmap);
            } else if (requestCode == REQUEST_IMAGE_GALLERY && data != null) {
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    avatarImageView.setImageBitmap(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProfileUpdateListener) {
            profileUpdateListener = (ProfileUpdateListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ProfileUpdateListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        profileUpdateListener = null;
    }
}
