package com.example.fasih.instagramapplication.Utils.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fasih.instagramapplication.R;
import com.example.fasih.instagramapplication.Utils.Models.Photo;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Fasih on 10/21/18.
 */

public class ProfileFragment extends Fragment {
    private static String userID;
    private OnGridImageSelectedListener onGridImageSelectedListener;
    private BottomNavigationView navigationView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private ImageView profileMenu, profile_photo;
    private ProgressBar profile_gridview_progressbar;
    private GridImageAdapter adapter;
    private ProgressBar photoProgress;
    private Toolbar toolbar;
    private GridView gridView;
    private TextView tvPosts, tvFollowers, tvFollowing, textEditProfile, display_name, description, website;
    private FirebaseMethods firebaseMethods;
    private ArrayList<Photo> photoList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setupFragmentWidgets(view);
        setUpBottomNavigation(view);
        setupFirebase();
        setUpMenu(view);
        initImageLoader();

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
        gridView = view.findViewById(R.id.gridView);

        textEditProfile = view.findViewById(R.id.textEditProfile);
        tvPosts = view.findViewById(R.id.tvPosts);
        tvFollowers = view.findViewById(R.id.tvFollowers);
        tvFollowing = view.findViewById(R.id.tvFollowing);
        display_name = view.findViewById(R.id.display_name);
        description = view.findViewById(R.id.description);
        website = view.findViewById(R.id.website);

        //Listener
        textEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountSettingActivity.class);
                intent.putExtra(getActivity().getString(R.string.calling_activity), getActivity().getString(R.string.profile_activity));
                getActivity().overridePendingTransition(R.anim.fade_in_nav, R.anim.fade_out_nav);
                getActivity().startActivity(intent);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onGridImageSelectedListener.onGridImageSelected(photoList.get(position), 0);
            }
        });
    }

    private void setUpImageGrid() {
        final ArrayList<String> arrayListUrls = new ArrayList<>();
        Query query = myRef.child(getString(R.string.db_user_photos_node)).child(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot photos_node : dataSnapshot.getChildren()) {
                    Photo photo = photos_node.getValue(Photo.class);
                    arrayListUrls.add(photo.getImage_path());
                    photoList.add(photo);

                }
                adapter.setArrayList(arrayListUrls, getString(R.string.ProfileFragment));
                adapter.setGridProgress(profile_gridview_progressbar);
                gridView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initImageLoader() {
        ImageLoader.getInstance().init(new UniversalImageLoader(getActivity()).getConfig());
    }

    private void setUpProfilePhoto(String url) {
        UniversalImageLoader.setImage(UrlManager.originalUrl(url), profile_photo, photoProgress, "https://");
    }

    private void setUpMenu(View view) {
        ((ProfileActivity) getActivity()).setSupportActionBar(toolbar);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AccountSettingActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in_nav, R.anim.fade_out_nav);
            }
        });
    }

    private void setUpBottomNavigation(View view) {
        int activity_count = 4;
        BottomNavigationViewHelper.setOnNavigationItemSelectedListener(getActivity(), getActivity(), navigationView);
        Menu menu = navigationView.getMenu();
        menu.getItem(activity_count).setChecked(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onGridImageSelectedListener = (OnGridImageSelectedListener) context;
        } catch (ClassCastException exc) {
            exc.printStackTrace();
        }
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        firebaseMethods = new FirebaseMethods(ProfileFragment.this.getActivity());
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    userID = user.getUid();
                    setUpImageGrid();
                }
            }
        };
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Retrieving Data fron the database
                UserSettings settings = firebaseMethods.getUserSettings(dataSnapshot);
                if (settings != null) {
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

    public interface OnGridImageSelectedListener {
        void onGridImageSelected(Photo photo, int Activity_Number);
    }
}
