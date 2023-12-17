package com.unipi.msc.raiseupandroid.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.unipi.msc.raiseupandroid.Model.User;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Retrofit.RaiseUpAPI;
import com.unipi.msc.raiseupandroid.Retrofit.Request.EditUserRequest;
import com.unipi.msc.raiseupandroid.Retrofit.RetrofitClient;
import com.unipi.msc.raiseupandroid.Tools.ActivityUtils;
import com.unipi.msc.raiseupandroid.Tools.CustomBottomSheet;
import com.unipi.msc.raiseupandroid.Tools.FileUtils;
import com.unipi.msc.raiseupandroid.Tools.ImageUtils;
import com.unipi.msc.raiseupandroid.Tools.ItemViewModel;
import com.unipi.msc.raiseupandroid.Tools.RetrofitUtils;
import com.unipi.msc.raiseupandroid.Tools.UserUtils;

import java.io.File;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    ConstraintLayout constraintLayoutProfile;
    ImageView imageViewProfile;
    TextView textViewEmail, textViewUsername, textViewFirstName,
            textViewLastName, textViewPassword;
    View.OnClickListener textListener;
    private RaiseUpAPI raiseUpAPI;
    private Toast t;
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
                            imageViewProfile.setImageBitmap(bitmap);
                            File file = FileUtils.saveBitmapToFile(requireActivity(), bitmap);
                            EditUserRequest request = new EditUserRequest();
                            request.setMultipartFile(MultipartBody.Part.createFormData("multipartFile",
                                    file.getName(),
                                    RequestBody.create(MediaType.parse("image/*"),file)));
                            updateUserInfo(request);
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
                .into(imageViewProfile);
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
        initObjects();
        initListeners();
        loadData();
        return view;
    }

    private void initObjects() {
        raiseUpAPI = RetrofitClient.getInstance(requireActivity()).create(RaiseUpAPI.class);
    }

    private void loadData() {
        new ViewModelProvider(requireActivity()).get(ItemViewModel.class).getUser().observe(getViewLifecycleOwner(), user -> {
            ImageUtils.loadProfileToImageView(requireActivity(),user.getProfile(), imageViewProfile);
            textViewEmail.setText(user.getEmail());
            textViewUsername.setText(user.getUsername());
            textViewFirstName.setText(user.getFirstName());
            textViewLastName.setText(user.getLastName());
        });
    }

    private void initListeners() {
        textListener = view -> {
            TextView textView = (TextView) view;
            CustomBottomSheet.showEdit(requireActivity(), getLabel(view.getId()), textView.getText().toString(), text->{
                EditUserRequest request = new EditUserRequest();
                if (textView.getId() == textViewFirstName.getId()){
                    request.setFirstName(RequestBody.create(MediaType.parse("text/plain"),text));
                    textView.setText(text);
                }else if (textView.getId() == textViewLastName.getId()){
                    request.setLastName(RequestBody.create(MediaType.parse("text/plain"),text));
                    textView.setText(text);
                }else if (textView.getId() == textViewPassword.getId()){
                    request.setPassword(RequestBody.create(MediaType.parse("text/plain"),text));
                }
                updateUserInfo(request);
            });
        };
        textViewFirstName.setOnClickListener(textListener);
        textViewLastName.setOnClickListener(textListener);
        textViewPassword.setOnClickListener(textListener);
        constraintLayoutProfile.setOnClickListener(this::openImage);
    }

    private void updateUserInfo(EditUserRequest request) {
        raiseUpAPI.editUser(UserUtils.loadBearerToken(requireActivity()),
                request.getMultipartFile(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(requireActivity(),response);
                    ActivityUtils.showToast(requireActivity(),t,msg);
                }else {
                    JsonObject data = response.body().get("data").getAsJsonObject();
                    UserUtils.saveUser(requireActivity(), User.buildFromJSON(data));
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(requireActivity(),t);
            }
        });
    }

    private void openImage(View view) {
        openImageDialog();
    }

    private void openImageDialog() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        launchSomeActivity.launch(i);
    }

    private String getLabel(int viewId){
        if (textViewFirstName.getId() == viewId){
            return getString(R.string.first_name);
        }else if (textViewLastName.getId() == viewId){
            return getString(R.string.last_name);
        }else if (textViewPassword.getId() == viewId){
            return getString(R.string.password);
        }
        return "";
    }

    private void initViews(View view) {
        constraintLayoutProfile = view.findViewById(R.id.constraintLayoutProfile);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewUsername = view.findViewById(R.id.textViewUsername);
        textViewFirstName = view.findViewById(R.id.textViewFirstName);
        textViewLastName = view.findViewById(R.id.textViewLastName);
        textViewPassword = view.findViewById(R.id.textViewPassword);
    }
}