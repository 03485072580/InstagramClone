package com.example.fasih.instagramapplication.Utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.example.fasih.instagramapplication.Utils.Home.HomeActivity;
import com.example.fasih.instagramapplication.Utils.Likes.LikesActivity;
import com.example.fasih.instagramapplication.Utils.Profile.ProfileActivity;
import com.example.fasih.instagramapplication.R;
import com.example.fasih.instagramapplication.Utils.Search.SearchActivity;
import com.example.fasih.instagramapplication.Utils.Share.ShareActivity;

/**
 * Created by Fasih on 10/08/18.
 */

public class BottomNavigationViewHelper {
    public static void setOnNavigationItemSelectedListener(final Context context,
                                                           BottomNavigationView navigationView){
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:
                        context.startActivity(new Intent(context, HomeActivity.class));
                        break;
                    case R.id.ic_search:
                        context.startActivity(new Intent(context, SearchActivity.class));
                        break;
                    case R.id.ic_circle:
                        context.startActivity(new Intent(context, ShareActivity.class));
                        break;
                    case R.id.ic_alert:
                        context.startActivity(new Intent(context, LikesActivity.class));
                        break;
                    case R.id.ic_android:
                        context.startActivity(new Intent(context, ProfileActivity.class));
                        break;

                }
                return false;

            }
        });
    }
}
