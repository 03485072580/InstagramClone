package com.example.fasih.instagramapplication.Utils.Share;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.fasih.instagramapplication.R;
import com.example.fasih.instagramapplication.Utils.Utils.BottomNavigationViewHelper;
import com.example.fasih.instagramapplication.Utils.Utils.FragmentCurrentItem;
import com.example.fasih.instagramapplication.Utils.Utils.PagerSectionAdapter;
import com.example.fasih.instagramapplication.Utils.Utils.Permissions;

public class ShareActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    private static int PERMISSIONS_GRANT_CODE=1;
    private PhotoFragment photo_fragment;
    private FragmentCurrentItem currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        setupPermissions();
    }

    private void setupPermissions() {
        if (checkPermissionsArray(Permissions.PERMISSIONS)) {
        //Permissions Granted by the Mobile Owner
            setupViewPager();
        }else{
            verifyPermissionsGrant(Permissions.PERMISSIONS);
            if(checkPermissionsArray(Permissions.PERMISSIONS)){
                setupViewPager();
            }
            else{
                finish();
            }
        }
    }

    public int getTask(){
        if(getIntent()!=null){
            return getIntent().getFlags();
        }
        return 0;
    }

    private void setupViewPager() {
        mViewPager=findViewById(R.id.container);
        PagerSectionAdapter adapter=new PagerSectionAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment());
        photo_fragment=new PhotoFragment();
        currentItem=photo_fragment;
        adapter.addFragment(photo_fragment);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout=findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        if(tabLayout.getTabCount()==2) {
            tabLayout.getTabAt(0).setText(R.string.fragment_gallery);
            tabLayout.getTabAt(1).setText(R.string.fragment_photo);
        }

        //Listener

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               currentItem.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void verifyPermissionsGrant(String[] permissions) {
        ActivityCompat.requestPermissions(ShareActivity.this,permissions,PERMISSIONS_GRANT_CODE);
    }

    private boolean checkPermissionsArray(String[] permissions) {
        if (permissions != null) {
            for (int i = 0; i < permissions.length; i++) {
                int permission_number = ActivityCompat.checkSelfPermission(ShareActivity.this, permissions[i]);
                if (permission_number != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }
}
