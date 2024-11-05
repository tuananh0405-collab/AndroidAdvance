package com.example.mvp_practice.view;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.mvp_practice.R;
import com.example.mvp_practice.contract.ProfileContract;
import com.example.mvp_practice.model.User;
import com.example.mvp_practice.model.UserProfileModel;
import com.example.mvp_practice.presenter.UserProfilePresenter;

import java.util.Calendar;

public class ProfileFragment extends Fragment implements ProfileContract.View {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    ImageView imgAvt;
    Button btnCancel, btnDone, btnDOBPicker;
    EditText edtName, edtEmail;
    TextView txtDOB;
    RadioGroup radioGroup;

    ProfileContract.Presenter presenter;
    private ProfileUpdateListener profileUpdateListener;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;

    public interface ProfileUpdateListener {
        void updateUserProfileInNavigation(User user);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imgAvt = view.findViewById(R.id.imgAvt);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnDone = view.findViewById(R.id.btnDone);
        edtName = view.findViewById(R.id.edtName);
        edtEmail = view.findViewById(R.id.edtEmail);
        txtDOB = view.findViewById(R.id.txtDOB);
        btnDOBPicker = view.findViewById(R.id.btnDOBPicker);
        radioGroup = view.findViewById(R.id.radioGroup);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                imgAvt.setImageBitmap(photo);
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                imgAvt.setImageURI(uri);
            }
        });

        presenter = new UserProfilePresenter(this, new UserProfileModel(requireContext()));

        btnCancel.setOnClickListener(v -> presenter.onCancelButtonClicked());

        imgAvt.setOnClickListener(v -> presenter.onAvatarClicked());

        btnDone.setOnClickListener(v -> {
            imgAvt.setDrawingCacheEnabled(true);
            Bitmap avatarBitmap = Bitmap.createBitmap(imgAvt.getDrawingCache());
            imgAvt.setDrawingCacheEnabled(false);

            User user = new User(
                    edtName.getText().toString(),
                    edtEmail.getText().toString(),
                    txtDOB.getText().toString(),
                    radioGroup.getCheckedRadioButtonId() == R.id.rbtnMale,
                    avatarBitmap
            );

            presenter.onSaveButtonClicked(user);
        });
        btnDOBPicker.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view1, selectedYear, selectedMonth, selectedDay) -> {
                String selectedDate = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;
                txtDOB.setText(selectedDate);
            }, year, month, day);
            datePickerDialog.show();
        });
        presenter.loadUserProfile();
        return view;
    }

    @Override
    public void showUserProfile(User user) {
        edtName.setText(user.getName());
        edtEmail.setText(user.getEmail());
        txtDOB.setText(user.getDob());
        radioGroup.check(user.isMale() ? R.id.rbtnMale : R.id.rbtnFemale);
        imgAvt.setImageBitmap(user.getAvt());
    }

    @Override
    public void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(), imgAvt);
        popupMenu.getMenuInflater().inflate(R.menu.avatar_options_menu, popupMenu.getMenu());
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
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 1);
            return;
        }
        cameraLauncher.launch(cameraIntent);
    }

    @Override
    public void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    @Override
    public void showError(String error) {

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
        imgAvt.setDrawingCacheEnabled(true);
        Bitmap avatarBitmap = Bitmap.createBitmap(imgAvt.getDrawingCache());
        imgAvt.setDrawingCacheEnabled(false);
        User userProfile = new User(
                edtName.getText().toString(),
                edtEmail.getText().toString(),
                txtDOB.getText().toString(),
                radioGroup.getCheckedRadioButtonId() == R.id.rbtnMale,
                avatarBitmap);
        profileUpdateListener.updateUserProfileInNavigation(userProfile);
        backToPreviousScreen();
    }


}