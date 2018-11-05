package com.example.fasih.instagramapplication.Utils.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.example.fasih.instagramapplication.R;
import com.example.fasih.instagramapplication.Utils.Login.LoginActivity;
import com.example.fasih.instagramapplication.Utils.Utils.BottomNavigationViewHelper;
import com.example.fasih.instagramapplication.Utils.Utils.PagerSectionAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView navigationView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpBottomNavigation();
        setUpViewPager();
        setupFirebase();
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                checkCurrentUser(user);
            }
        };

    }

    private void checkCurrentUser(FirebaseUser user) {
        if (user == null) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }
        else{
            emailAuth(user);
        }
    }

    private void emailAuth(FirebaseUser user) {
       if(!user.isEmailVerified()){
           startActivity(new Intent(HomeActivity.this, LoginActivity.class));
           finish();
       }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuth != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    private void setUpViewPager() {
        ViewPager viewPager = findViewById(R.id.container);
        PagerSectionAdapter pagerSectionAdapter = new PagerSectionAdapter(getSupportFragmentManager());
        pagerSectionAdapter.addFragment(new CameraFragment());
        pagerSectionAdapter.addFragment(new HomeFragment());
        pagerSectionAdapter.addFragment(new MessageFragment());
        viewPager.setAdapter(pagerSectionAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        if(tabLayout.getTabCount()==3){
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_home);
            tabLayout.getTabAt(2).setIcon(R.drawable.ic_send);
        }
    }

    private void setUpBottomNavigation() {
        int activity_count = 0;
        navigationView = findViewById(R.id.bottom_nav_viewBar);
        BottomNavigationViewHelper.setOnNavigationItemSelectedListener(HomeActivity.this, navigationView);
        Menu menu = navigationView.getMenu();
        menu.getItem(activity_count).setChecked(true);
    }
}
