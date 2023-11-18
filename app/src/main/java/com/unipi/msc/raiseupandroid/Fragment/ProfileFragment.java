package com.unipi.msc.raiseupandroid.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Tools.CustomAlertDialog;
import com.unipi.msc.raiseupandroid.Tools.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;

public class ProfileFragment extends Fragment {
    ConstraintLayout constraintLayoutProfile;
    ImageView imageView;
    TextView textViewEmail, textViewUsername, textViewFirstName,
            textViewLastName, textViewPassword;
    View.OnClickListener textListener;
    ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    openImageDialog();
                }
            });

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        final InputStream imageStream;
                        try {
                            imageStream = requireActivity().getContentResolver().openInputStream(selectedImageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                            imageView.setImageBitmap(bitmap);
                            File file = FileUtils.saveBitmapToFile(requireActivity(), bitmap);
                            file.delete();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });

    private void loadFileToImageView(Uri uri) {
        final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), uri.getPath());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_profile);

        Glide.with(requireActivity())
                .load(file)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
        initListeners();
        return view;
    }

    private void initListeners() {
        textListener = view -> {
            TextView textView = (TextView) view;
            CustomAlertDialog.showEdit(requireActivity(), getLabel(view.getId()), textView.getText().toString(), textView::setText);
        };
        textViewFirstName.setOnClickListener(textListener);
        textViewLastName.setOnClickListener(textListener);
        textViewPassword.setOnClickListener(textListener);
        constraintLayoutProfile.setOnClickListener(this::openImage);
    }

    private void openImage(View view) {
        openImageDialog();
//        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            // You can use the API that requires the permission.
//        } else {
//            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
//        }
    }

    private void openImageDialog() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        launchSomeActivity.launch(i);
    }

    private int getLabel(int viewId){
        if (textViewFirstName.getId() == viewId){
            return R.string.first_name;
        }else if (textViewLastName.getId() == viewId){
            return R.string.last_name;
        }else if (textViewPassword.getId() == viewId){
            return R.string.password;
        }
        return 0;
    }

    private void initViews(View view) {
        constraintLayoutProfile = view.findViewById(R.id.constraintLayoutProfile);
        imageView = view.findViewById(R.id.imageView);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewUsername = view.findViewById(R.id.textViewUsername);
        textViewFirstName = view.findViewById(R.id.textViewFirstName);
        textViewLastName = view.findViewById(R.id.textViewLastName);
        textViewPassword = view.findViewById(R.id.textViewPassword);
    }
}