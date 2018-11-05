package com.example.fasih.instagramapplication.Utils.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Fasih on 10/12/18.
 */

public class SectionsPagerStateAdapter extends FragmentStatePagerAdapter {
    private final ArrayList arrayList= new ArrayList();
    private final HashMap<Fragment,Integer> mFragments=new HashMap();
    private final HashMap<String,Integer> mFragmentNumber=new HashMap();
    private final HashMap<Integer,String> mFragmentNames=new HashMap();
    public SectionsPagerStateAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) arrayList.get(position);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }
    public void addFragment(Fragment fragment,String fragmentName){
        arrayList.add(fragment);
        mFragments.put(fragment,arrayList.size()-1);
        mFragmentNumber.put(fragmentName,arrayList.size()-1);
        mFragmentNames.put(arrayList.size()-1,fragmentName);

    }
    public Integer getFragmentNumber(String fragmentName){
     if(mFragmentNumber.containsKey(fragmentName)){
         return (mFragmentNumber.get(fragmentName));
     }else{
         return null;
     }

    }
    public String getFragmentName(Integer fragmentNumber){
        if(mFragmentNames.containsKey(fragmentNumber)){
            return (mFragmentNames.get(fragmentNumber));
        }else{
            return null;
        }

    }

    public Integer getFragmentNumber(Fragment fragment){
        if(mFragmentNumber.containsKey(fragment)){
            return (mFragmentNumber.get(fragment));
        }else{
            return null;
        }

    }


}
