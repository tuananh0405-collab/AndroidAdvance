package com.example.mvvm_practice.view;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.example.mvvm_practice.R;
import com.example.mvvm_practice.databinding.FragmentProfileBinding;
import com.example.mvvm_practice.model.User;
import com.example.mvvm_practice.viewmodel.UserViewModel;

import java.io.IOException;
import java.util.Calendar;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    UserViewModel viewModel;
    ActivityResultLauncher<Intent> cameraLauncher, galleryLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.edtName.setText(user.getFullName());
                binding.edtEmail.setText(user.getEmail());
                binding.txtDOB.setText(user.getBirthday());
                Log.e("Gender",user.getGender());
                if ("male".equals(user.getGender())) {
                    binding.rbtnMale.setChecked(true);
                } else if ("female".equals(user.getGender())) {
                    binding.rbtnFemale.setChecked(true);
                }
            }
        });
        viewModel.getCloseFragment().observe(getViewLifecycleOwner(), shouldClose -> {
            if (Boolean.TRUE.equals(shouldClose)) {
                removeProfileFragment();
            }
        });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        viewModel.saveAvatarToPreferences(bitmap);
                        ((MutableLiveData<Bitmap>) viewModel.getAvatarBitmap()).setValue(bitmap);
                    }
                });

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Uri selectedImageUri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImageUri);
                            viewModel.saveAvatarToPreferences(bitmap);
                            ((MutableLiveData<Bitmap>) viewModel.getAvatarBitmap()).setValue(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.imgAvt.setOnClickListener(view1 -> {
            PopupMenu popupMenu = new PopupMenu(requireContext(), binding.imgAvt);
            popupMenu.getMenuInflater().inflate(R.menu.avatar_options_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_camera) {
                    openCamera();
                    return true;
                } else if (item.getItemId() == R.id.menu_gallery) {
                    openGallery();
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });

        binding.btnDOBPicker.setOnClickListener(view1 -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view2, selectedYear, selectedMonth, selectedDay) -> {
                String selectedDate = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;
                binding.txtDOB.setText(selectedDate);
            }, year, month, day);
            datePickerDialog.show();
        });

        return binding.getRoot();
    }


    public void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 1);
            return;
        }
        cameraLauncher.launch(cameraIntent);
    }


    public void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    private void removeProfileFragment() {
        getParentFragmentManager().beginTransaction().hide(this).commit();
    }

}