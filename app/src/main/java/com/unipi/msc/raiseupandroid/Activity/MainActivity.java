package com.unipi.msc.raiseupandroid.Activity;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
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
    ImageButton imageButtonClose;
    TextView textViewUserName;
    ImageView imageViewSearch, imageViewUserImage;

    LinearLayout linearLayoutProfile, linearLayoutBoards,
                 linearLayoutTasks, linearLayoutTags,
                 linearLayoutStatistics, linearLayoutLogout;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
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
        loadUserData();
    }

    private void initObjects() {
        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        raiseUpAPI = RetrofitClient.getInstance(this).create(RaiseUpAPI.class);
        user = UserUtils.loadUser(this);
        itemViewModel.setUser(user);
    }

    private void loadUserData() {
        raiseUpAPI.getUser(UserUtils.loadBearerToken(this)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(MainActivity.this, response);
                    ActivityUtils.showToast(MainActivity.this,t,msg);
                    textViewUserName.setText(user.getFullName());
                    ImageUtils.loadProfileToImageView(MainActivity.this, RetrofitUtils.BASE_URL + user.getProfile(), imageViewUserImage);
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

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    private void initListeners() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
        imageButtonClose.setOnClickListener(view->drawerLayout.closeDrawer(GravityCompat.START));
        navButtons.forEach(nav->nav.setOnClickListener(this::onNavButtonSelection));
        onNavButtonSelection(linearLayoutBoards);
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAffinity();
            }
        });

    }

    private void onNavButtonSelection(View view) {
        clearSelectedButtons();
        view.setSelected(true);
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
    }

    private void clearSelectedButtons(){
        navButtons.stream().filter(View::isSelected).forEach(nav->nav.setSelected(false));
    }
}