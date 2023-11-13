package com.unipi.msc.raiseupandroid.Activity;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationView;
import com.unipi.msc.raiseupandroid.Fragment.ProfileFragment;
import com.unipi.msc.raiseupandroid.R;

public class MainActivity extends AppCompatActivity {
    ImageView imageViewNav, imageViewSearch;
    ImageButton imageButtonProfileEdit, imageButtonClose;
    LinearLayout linearLayoutBoards, linearLayoutTasks,
                 linearLayoutTags, linearLayoutStatistics,
                 linearLayoutLogout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
        if (savedInstanceState == null) {
//            replaceFragment();
        }
//        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                    drawerLayout.closeDrawer(GravityCompat.START);
//                } else {
//
//                }
//            }
//        });

    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void initListeners() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
        imageButtonClose.setOnClickListener(view->drawerLayout.closeDrawer(GravityCompat.START));
        imageButtonProfileEdit.setOnClickListener(view->replaceFragment(new ProfileFragment()));
    }

    private void initViews() {
        View drawer_layout = findViewById(R.id.includeDrawerLayout);
        imageButtonClose = drawer_layout.findViewById(R.id.imageButtonClose);
        imageButtonProfileEdit = drawer_layout.findViewById(R.id.imageButtonProfileEdit);
        linearLayoutBoards = drawer_layout.findViewById(R.id.linearLayoutBoards);
        linearLayoutTasks = drawer_layout.findViewById(R.id.linearLayoutTasks);
        linearLayoutTags = drawer_layout.findViewById(R.id.linearLayoutTags);
        linearLayoutStatistics = drawer_layout.findViewById(R.id.linearLayoutStatistics);
        linearLayoutLogout = drawer_layout.findViewById(R.id.linearLayoutLogout);
        toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
    }
}