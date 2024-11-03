package com.example.ojtaadaassignment04.mvp.view;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.ojtaadaassignment04.R;
import com.example.ojtaadaassignment04.mvp.model.User;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class EditProfileFragment extends Fragment {

    private Button btnCancel, btnDone;
    private ImageView imgAvt;
    private EditText edtName, edtEmail;
    private TextView txtDOB;
    private RadioGroup radioGroup;
    private RadioButton rbtnMale, rbtnFemale;
    private Button btnDOBPicker;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private User user;

    public interface OnProfileEditedListener {
        void onProfileEdited(User user);
    }

    private OnProfileEditedListener listener;

    public static EditProfileFragment newInstance(User user) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnProfileEditedListener) {
            listener = (OnProfileEditedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnProfileEditedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        imgAvt = view.findViewById(R.id.imgAvt);
        edtName = view.findViewById(R.id.edtName);
        edtEmail = view.findViewById(R.id.edtEmail);
        txtDOB = view.findViewById(R.id.txtDOB);
        radioGroup = view.findViewById(R.id.radioGroup);
        rbtnMale = view.findViewById(R.id.rbtnMale);
        rbtnFemale = view.findViewById(R.id.rbtnFemale);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnDone = view.findViewById(R.id.btnDone);
        btnDOBPicker = view.findViewById(R.id.btnDOBPicker);

        // Initialize camera and gallery launchers
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

        // Retrieve the user data from arguments
        if (getArguments() != null) {
            user = getArguments().getParcelable("user");
            if (user != null) {
                Log.d("EditProfileFragment", "User loaded: " + user.getName());
                edtName.setText(user.getName());
                edtEmail.setText(user.getEmail());
                txtDOB.setText(user.getDob());
                if (user.isMale()) {
                    rbtnMale.setChecked(true);
                } else {
                    rbtnFemale.setChecked(true);
                }
                if (user.getAvt() != null) {
                    imgAvt.setImageBitmap(user.getAvt());
                }
            }
        }

        // Set up date picker
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

        // Set up avatar click for options (camera/gallery)
        imgAvt.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), imgAvt);
            popupMenu.getMenuInflater().inflate(R.menu.avatar_options_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_camera) {
                    openCamera();
                } else if (item.getItemId() == R.id.menu_gallery) {
                    openGallery();
                }
                return false;
            });
            popupMenu.show();
        });

        // Handle "Cancel" button click
        btnCancel.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Edit canceled", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Handle "Done" button click
        btnDone.setOnClickListener(v -> {
            // Update user details
            String updatedName = edtName.getText().toString();
            String updatedEmail = edtEmail.getText().toString();
            String updatedDOB = txtDOB.getText().toString();
            boolean isMale = rbtnMale.isChecked();

            imgAvt.setDrawingCacheEnabled(true);
            Bitmap avatarBitmap = Bitmap.createBitmap(imgAvt.getDrawingCache());
            imgAvt.setDrawingCacheEnabled(false);

            String avatarBase64 = bitmapToBase64(avatarBitmap);

            // Save updated details to SharedPreferences
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", updatedName);
            editor.putString("email", updatedEmail);
            editor.putString("dob", updatedDOB);
            editor.putBoolean("isMale", isMale);
            editor.putString("avatar", avatarBase64);
            editor.apply();

            // Update the user object
            user.setName(updatedName);
            user.setEmail(updatedEmail);
            user.setDob(updatedDOB);
            user.setMale(isMale);
            user.setAvt(avatarBitmap);

            // Notify the listener of the updated user
            listener.onProfileEdited(user);

            Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 1);
            return;
        }
        cameraLauncher.launch(cameraIntent);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
