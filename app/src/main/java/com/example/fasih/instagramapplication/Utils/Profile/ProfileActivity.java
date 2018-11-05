package com.example.fasih.instagramapplication.Utils.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.fasih.instagramapplication.R;
import com.example.fasih.instagramapplication.Utils.Utils.BottomNavigationViewHelper;
import com.example.fasih.instagramapplication.Utils.Utils.GridImageAdapter;
import com.example.fasih.instagramapplication.Utils.Utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupProfileFragment();


    }

    private void setupProfileFragment() {
        Fragment profileFragment=new ProfileFragment();
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_profile_container,profileFragment);
        transaction.addToBackStack(getString(R.string.fragment_profile));
        transaction.commit();

    }


}
