package com.example.fasih.instagramapplication.Utils.Likes;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.example.fasih.instagramapplication.R;
import com.example.fasih.instagramapplication.Utils.Utils.BottomNavigationViewHelper;

public class LikesActivity extends AppCompatActivity {
    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpBottomNavigation();
    }
    private void setUpBottomNavigation() {
        int activity_count=3;
        navigationView = findViewById(R.id.bottom_nav_viewBar);
        BottomNavigationViewHelper.setOnNavigationItemSelectedListener(this, this, navigationView);
        Menu menu =navigationView.getMenu();
        menu.getItem(activity_count).setChecked(true);
    }
}
