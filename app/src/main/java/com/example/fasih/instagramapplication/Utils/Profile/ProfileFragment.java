package com.example.fasih.instagramapplication.Utils.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fasih.instagramapplication.R;
import com.example.fasih.instagramapplication.Utils.Models.UserAccountSettings;
import com.example.fasih.instagramapplication.Utils.Models.UserSettings;
import com.example.fasih.instagramapplication.Utils.Utils.BottomNavigationViewHelper;
import com.example.fasih.instagramapplication.Utils.Utils.FirebaseMethods;
import com.example.fasih.instagramapplication.Utils.Utils.GridImageAdapter;
import com.example.fasih.instagramapplication.Utils.Utils.UniversalImageLoader;
import com.example.fasih.instagramapplication.Utils.Utils.UrlManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Fasih on 10/21/18.
 */

public class ProfileFragment extends Fragment {
    private BottomNavigationView navigationView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private ImageView profileMenu,profile_photo;
    private ProgressBar profile_gridview_progressbar;
    private GridImageAdapter adapter;
    private ProgressBar photoProgress;
    private Toolbar toolbar;
    private TextView tvPosts, tvFollowers, tvFollowing
            , textEditProfile, display_name, description
            , website;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile,container,false);
        setupFragmentWidgets(view);
        setUpBottomNavigation(view);
        setupFirebase();
        setUpMenu(view);
        initImageLoader();
        setUpImageGrid(view);

        return view;
    }

    private void setupFragmentWidgets(View view) {
        profile_gridview_progressbar = view.findViewById(R.id.profile_gridview_progressbar);
        adapter = new GridImageAdapter(getActivity());
        profile_photo = view.findViewById(R.id.profile_photo);
        photoProgress = view.findViewById(R.id.profile_photo_progressbar);
        toolbar = view.findViewById(R.id.profileToolbar);
        profileMenu = view.findViewById(R.id.profileMenu);
        navigationView = view.findViewById(R.id.bottom_nav_viewBar);

        textEditProfile=view.findViewById(R.id.textEditProfile);
        tvPosts=view.findViewById(R.id.tvPosts);
        tvFollowers=view.findViewById(R.id.tvFollowers);
        tvFollowing=view.findViewById(R.id.tvFollowing);
        display_name=view.findViewById(R.id.display_name);
        description=view.findViewById(R.id.description);
        website=view.findViewById(R.id.website);

        //Listener
        textEditProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),AccountSettingActivity.class);
                intent.putExtra(getActivity().getString(R.string.calling_activity),getActivity().getString(R.string.profile_activity));
                getActivity().startActivity(intent);
            }
        });
    }

    private void setUpImageGrid( View view) {
        ArrayList arrayListOfUrls = prepareList();
        GridView gridView = view.findViewById(R.id.gridView);
        adapter.setArrayList(arrayListOfUrls);
        adapter.setGridProgress(profile_gridview_progressbar);
        gridView.setAdapter(adapter);
    }

    private void initImageLoader() {
        ImageLoader.getInstance().init(new UniversalImageLoader(getActivity()).getConfig());
    }

    private void setUpProfilePhoto(String url) {
        UniversalImageLoader.setImage(UrlManager.originalUrl(url), profile_photo, photoProgress, "https://");
    }

    private void setUpMenu( View view) {
        ((ProfileActivity)getActivity()).setSupportActionBar(toolbar);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AccountSettingActivity.class));
            }
        });
    }

    private void setUpBottomNavigation( View view) {
        int activity_count = 4;
        BottomNavigationViewHelper.setOnNavigationItemSelectedListener(getActivity(), navigationView);
        Menu menu = navigationView.getMenu();
        menu.getItem(activity_count).setChecked(true);
    }

    private ArrayList prepareList() {
        ArrayList arrayListOfUrls = new ArrayList();
        arrayListOfUrls.add("https://cdn.vox-cdn.com/thumbor/eVoUeqwkKQ7MFjDCgrPrqJP5ztc=/0x0:2040x1360/1200x800/filters:focal(860x1034:1186x1360)/cdn.vox-cdn.com/uploads/chorus_image/image/59377089/wjoel_180413_1777_android_001.1523625143.jpg");
        arrayListOfUrls.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQRbl9bFP5z1_V0MsBNSrropjhWQxV25YVDXZ0-zFW4gdvZ8fLFqw");
        arrayListOfUrls.add("https://cdn.wccftech.com/wp-content/uploads/2017/08/download-android-8.jpg");
        arrayListOfUrls.add("https://st1.bgr.in/wp-content/uploads/2018/03/Android-P-poll-1.png");
        arrayListOfUrls.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSs4Ps6bBLgT9RzOE5CO4hyN748JEwlMu67D880KtYyS6-SXSKaLw");
        arrayListOfUrls.add("https://images.idgesg.net/images/article/2017/08/android_robot_logo_by_ornecolorada_cc0_via_pixabay1904852_wide-100732483-large.jpg");
        arrayListOfUrls.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR2aZwiwdc6FqwDi6Hb99E1H4xv7sRpVGgY1J0u91w4aGMfikfW");
        arrayListOfUrls.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTqPgOH0njEY3NvX_8SkzJdA73JPgl-H97HXJJCR1yIOqpjX_zdfg");
        return arrayListOfUrls;
    }

    private FirebaseMethods firebaseMethods;
    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();
        firebaseMethods=new FirebaseMethods(ProfileFragment.this.getActivity());
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {}
            }
        };
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Retrieving Data fron the database
               UserSettings settings= firebaseMethods.getUserSettings(dataSnapshot);
               if(settings!=null){
                   initAccountFields(settings.getSettings());
                   setUpProfilePhoto(settings.getSettings().getProfile_photo());
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initAccountFields(UserAccountSettings settings) {

        tvPosts.setText(String.valueOf(settings.getPosts()));
        tvFollowers.setText(String.valueOf(settings.getFollowers()));
        tvFollowing.setText(String.valueOf(settings.getFollowing()));
        display_name.setText(String.valueOf(settings.getDisplay_name()));
        description.setText(String.valueOf(settings.getDescription()));
        website.setText(String.valueOf(settings.getWebsite()));



    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuth != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }
}
