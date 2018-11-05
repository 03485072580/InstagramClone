package com.example.fasih.instagramapplication.Utils.Search;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import com.example.fasih.instagramapplication.R;
import com.example.fasih.instagramapplication.Utils.Utils.BottomNavigationViewHelper;

public class SearchActivity extends AppCompatActivity {
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpBottomNavigation();
    }

    private void setUpBottomNavigation() {
        int activity_count = 1;
        navigationView = findViewById(R.id.bottom_nav_viewBar);
        BottomNavigationViewHelper.setOnNavigationItemSelectedListener(this, navigationView);
        navigationView.getMenu().getItem(activity_count).setChecked(true);
    }
}
