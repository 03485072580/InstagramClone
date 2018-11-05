package com.example.fasih.instagramapplication.Utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.example.fasih.instagramapplication.R;
import com.example.fasih.instagramapplication.Utils.Home.HomeActivity;
import com.example.fasih.instagramapplication.Utils.Likes.LikesActivity;
import com.example.fasih.instagramapplication.Utils.Profile.ProfileActivity;
import com.example.fasih.instagramapplication.Utils.Search.SearchActivity;
import com.example.fasih.instagramapplication.Utils.Share.ShareActivity;

/**
 * Created by Fasih on 10/08/18.
 */

public class BottomNavigationViewHelper {
    public static void setOnNavigationItemSelectedListener(final Context context, final Activity referencingactivity,
                                                           BottomNavigationView navigationView){
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:
                        context.startActivity(new Intent(context, HomeActivity.class));
                        referencingactivity.overridePendingTransition(R.anim.fade_in_nav, R.anim.fade_out_nav);
                        break;
                    case R.id.ic_search:
                        context.startActivity(new Intent(context, SearchActivity.class));
                        referencingactivity.overridePendingTransition(R.anim.fade_in_nav, R.anim.fade_out_nav);
                        break;
                    case R.id.ic_circle:
                        context.startActivity(new Intent(context, ShareActivity.class));
                        referencingactivity.overridePendingTransition(R.anim.fade_in_nav, R.anim.fade_out_nav);
                        break;
                    case R.id.ic_alert:
                        context.startActivity(new Intent(context, LikesActivity.class));
                        referencingactivity.overridePendingTransition(R.anim.fade_in_nav, R.anim.fade_out_nav);
                        break;
                    case R.id.ic_android:
                        context.startActivity(new Intent(context, ProfileActivity.class));
                        referencingactivity.overridePendingTransition(R.anim.fade_in_nav, R.anim.fade_out_nav);
                        break;

                }
                return false;

            }
        });
    }
}
