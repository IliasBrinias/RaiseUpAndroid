package com.unipi.msc.raiseupandroid.Activity;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;
import com.unipi.msc.raiseupandroid.Fragment.BoardFragment;
import com.unipi.msc.raiseupandroid.Fragment.ProfileFragment;
import com.unipi.msc.raiseupandroid.Fragment.StatisticsFragment;
import com.unipi.msc.raiseupandroid.Fragment.TagFragment;
import com.unipi.msc.raiseupandroid.Fragment.TaskFragment;
import com.unipi.msc.raiseupandroid.Model.User;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Retrofit.RaiseUpAPI;
import com.unipi.msc.raiseupandroid.Retrofit.RetrofitClient;
import com.unipi.msc.raiseupandroid.Tools.ActivityUtils;
import com.unipi.msc.raiseupandroid.Tools.ImageUtils;
import com.unipi.msc.raiseupandroid.Tools.ItemViewModel;
import com.unipi.msc.raiseupandroid.Tools.RetrofitUtils;
import com.unipi.msc.raiseupandroid.Tools.UserUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ItemViewModel itemViewModel;
    private ImageButton imageButtonClose;
    private TextView textViewUserName;
    private ImageView imageViewSearch, imageViewUserImage;
    private LinearLayout linearLayoutProfile, linearLayoutBoards,linearLayoutTasks, linearLayoutTags,linearLayoutStatistics, linearLayoutLogout;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private EditText editTextSearch;
    private final List<LinearLayout> navButtons = new ArrayList<>();
    private User user;
    private RaiseUpAPI raiseUpAPI;
    private Toast t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initObjects();
        initListeners();
    }
    @Override
    protected void onResume() {
        super.onResume();
        resetSearch();
        loadUserData();
    }
    private void initViews() {
        View drawer_layout = findViewById(R.id.includeDrawerLayout);
        textViewUserName = drawer_layout.findViewById(R.id.textViewUserName);
        imageButtonClose = drawer_layout.findViewById(R.id.imageButtonClose);
        linearLayoutProfile = drawer_layout.findViewById(R.id.linearLayoutProfile);
        linearLayoutBoards = drawer_layout.findViewById(R.id.linearLayoutBoards);
        linearLayoutTasks = drawer_layout.findViewById(R.id.linearLayoutTasks);
        linearLayoutTags = drawer_layout.findViewById(R.id.linearLayoutTags);
        linearLayoutStatistics = drawer_layout.findViewById(R.id.linearLayoutStatistics);
        linearLayoutLogout = drawer_layout.findViewById(R.id.linearLayoutLogout);
        imageViewUserImage = drawer_layout.findViewById(R.id.imageViewUserImage);
        navButtons.add(linearLayoutProfile);navButtons.add(linearLayoutBoards);
        navButtons.add(linearLayoutTasks);navButtons.add(linearLayoutTags);
        navButtons.add(linearLayoutStatistics);navButtons.add(linearLayoutLogout);
        toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        imageViewSearch = findViewById(R.id.imageViewSearch);
        editTextSearch = findViewById(R.id.editTextSearch);
    }
    private void initObjects() {
        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        raiseUpAPI = RetrofitClient.getInstance(this).create(RaiseUpAPI.class);
        user = UserUtils.loadUser(this);
        itemViewModel.setUser(user);
    }
    private void initListeners() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
        imageButtonClose.setOnClickListener(view->drawerLayout.closeDrawer(GravityCompat.START));
        navButtons.forEach(nav->nav.setOnClickListener(this::onNavButtonSelection));
        onNavButtonSelection(linearLayoutBoards);
        imageViewSearch.setOnClickListener(view -> {
            imageViewSearch.setSelected(!imageViewSearch.isSelected());
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imageViewSearch.isSelected()){
                editTextSearch.getText().clear();
                editTextSearch.setVisibility(View.VISIBLE);
                editTextSearch.requestFocus();
                imm.showSoftInput(editTextSearch, InputMethodManager.SHOW_IMPLICIT);
            }else {
                editTextSearch.setVisibility(View.INVISIBLE);
                imm.hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);
                itemViewModel.setKeyword("");
            }
        });
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                itemViewModel.setKeyword(editable.toString());
            }
        });
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAffinity();
            }
        });
    }
    private void resetSearch() {
        if (imageViewSearch.isSelected()) imageViewSearch.performClick();
    }
    private void loadUserData() {
        raiseUpAPI.getUser(UserUtils.loadBearerToken(this)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(MainActivity.this, response);
                    ActivityUtils.showToast(MainActivity.this,t,msg);
                }else {
                    user = User.buildFromJSON(response.body().get("data").getAsJsonObject());
                    UserUtils.saveUser(MainActivity.this,user);
                    textViewUserName.setText(user.getFullName());
                    ImageUtils.loadProfileToImageView(MainActivity.this, user.getProfile(), imageViewUserImage);
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(MainActivity.this,t);
            }
        });
    }
    private void onNavButtonSelection(View view) {
        clearSelectedButtons();
        view.setSelected(true);
        if (imageViewSearch.isSelected()) imageViewSearch.performClick();
        if (imageViewSearch.getVisibility() == View.GONE) imageViewSearch.setVisibility(View.VISIBLE);
        if (view.getId() == linearLayoutProfile.getId()) {
            replaceFragment(new ProfileFragment());
            imageViewSearch.setVisibility(View.GONE);
        } else if (view.getId() == linearLayoutBoards.getId()) {
            replaceFragment(new BoardFragment());
        } else if (view.getId() == linearLayoutTasks.getId()) {
            replaceFragment(new TaskFragment());
        } else if (view.getId() == linearLayoutTags.getId()) {
            replaceFragment(new TagFragment());
        } else if (view.getId() == linearLayoutStatistics.getId()) {
            replaceFragment(new StatisticsFragment());
            imageViewSearch.setVisibility(View.GONE);
        } else if (view.getId() == linearLayoutLogout.getId()) {
            UserUtils.logout(this);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    private void clearSelectedButtons(){
        navButtons.stream().filter(View::isSelected).forEach(nav->nav.setSelected(false));
    }
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}