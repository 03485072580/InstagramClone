package com.example.fasih.instagramapplication.Utils.Profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.fasih.instagramapplication.R;
import com.example.fasih.instagramapplication.Utils.Models.Photo;

public class ProfileActivity extends AppCompatActivity implements ProfileFragment.OnGridImageSelectedListener {

    /**
     * all the interfaces place
     */

    @Override
    public void onGridImageSelected(Photo photo, int Activity_Number) {
        ViewPostFragment postFragment = new ViewPostFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_profile_container, postFragment);
        transaction.addToBackStack(getString(R.string.view_post_fragment));
        transaction.commit();
    }


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
        transaction.commit();

    }
}
