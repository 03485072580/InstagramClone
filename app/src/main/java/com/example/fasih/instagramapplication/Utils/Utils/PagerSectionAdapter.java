package com.example.fasih.instagramapplication.Utils.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Fasih on 10/09/18.
 */

public class PagerSectionAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList= Collections.emptyList();
    public PagerSectionAdapter(FragmentManager fm) {
        super(fm);
        fragmentList=new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        if(fragmentList.isEmpty())
        {
            return 0;
        }
        return fragmentList.size();
    }
    public void addFragment(Fragment fragment){
        fragmentList.add(fragment);
    }
}
