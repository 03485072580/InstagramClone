package com.example.fasih.instagramapplication.Utils.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fasih.instagramapplication.R;
import com.example.fasih.instagramapplication.Utils.Dialog.ConfirmDialogPassword;
import com.example.fasih.instagramapplication.Utils.Login.LoginActivity;
import com.example.fasih.instagramapplication.Utils.Models.MessageEvent;
import com.example.fasih.instagramapplication.Utils.Models.UserSettings;
import com.example.fasih.instagramapplication.Utils.Share.ShareActivity;
import com.example.fasih.instagramapplication.Utils.Utils.FirebaseMethods;
import com.example.fasih.instagramapplication.Utils.Utils.UniversalImageLoader;
import com.example.fasih.instagramapplication.Utils.Utils.UrlManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Fasih on 10/13/18.
 */

public class EditProfileFragment extends Fragment {
    private ImageView profile_photo;
    private Context mContext;
    private ProgressBar progressBar;
    private ImageView backArrow, checkmark;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private EditText username, display_name, website, description, email, phoneNumber;
    private String email1;
    private TextView update_profile_photo;
    private FirebaseMethods firebaseMethods;
    private UserSettings settings;

    @Subscribe
    public void onEventReceive(MessageEvent messageEvent) {
        AuthCredential credential = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(), messageEvent.getPassword());
        try {
            Task<Void> task = mAuth.getCurrentUser().reauthenticate(credential);
            task.addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        mAuth.fetchProvidersForEmail(email1)
                                .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                        if (task.getResult().getProviders().size() != 0) {
                                            Toast.makeText(mContext, "Email Already in Use", Toast.LENGTH_SHORT)
                                                    .show();
                                        } else {
                                            mAuth.getCurrentUser().updateEmail(email1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        firebaseMethods.updateEmailAddress(mAuth.getCurrentUser().getEmail());
                                                        checkEmailValidity(email);
                                                   }
                                                }
                                            });

                                        }
                                    }
                                });
                    }else{

                    }
                }
            });
        } catch (Exception exc) {exc.printStackTrace();}

    }

    private void checkEmailValidity(EditText email) {
        try {
            mAuth.getCurrentUser().sendEmailVerification();
            Intent intent=new Intent(mContext,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getActivity().startActivity(intent);
            Toast.makeText(mContext, "Verification Message is Sent\nCheck Inbox For More Information", Toast.LENGTH_SHORT).show();
        }catch(NullPointerException exc){exc.printStackTrace();}
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);
        setUpFragmentWidgets(view);
        initUniversalImageLoader();
        setUpBackNavigation();
        setupFirebase();
        return view;
    }

    private void setUpBackNavigation() {
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((AccountSettingActivity)getActivity()).getTask()==0){
                    getActivity().overridePendingTransition(R.anim.fade_in_nav, R.anim.fade_out_nav);
                    getActivity().finish();
                }
                else{
                    //finish 1=AccountActivity
                    getActivity().overridePendingTransition(R.anim.fade_in_nav, R.anim.fade_out_nav);
                    getActivity().finish();
                }
            }
        });
    }

    private void setProfileImage(String url) {
        UniversalImageLoader.setImage(UrlManager.originalUrl(url), profile_photo, progressBar, "https://");
    }

    private void initUniversalImageLoader() {
        ImageLoader.getInstance().init(new UniversalImageLoader(mContext).getConfig());
    }

    private void setUpFragmentWidgets(View view) {
        mContext = getActivity();
        profile_photo = view.findViewById(R.id.profile_photo);
        progressBar = view.findViewById(R.id.editprofile_progress);
        backArrow = view.findViewById(R.id.backArrow);
        username = view.findViewById(R.id.username);
        display_name = view.findViewById(R.id.display_name);
        website = view.findViewById(R.id.website);
        description = view.findViewById(R.id.description);
        email = view.findViewById(R.id.email);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        checkmark = view.findViewById(R.id.checkmark);
        update_profile_photo=view.findViewById(R.id.update_profile_photo);

        //Listener
        checkmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileSettings();
            }
        });
        update_profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ShareActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        firebaseMethods = new FirebaseMethods(EditProfileFragment.this.getActivity());
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                } else {
                }
            }
        };
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Retrieving Data fron the database

                settings = firebaseMethods.getUserSettings(dataSnapshot);
                setProfileImage(settings.getSettings().getProfile_photo());
                populateFields(settings);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void populateFields(UserSettings settings) {
        if(settings!=null) {
            username.setText(settings.getSettings().getUsername());
            display_name.setText(settings.getSettings().getDisplay_name());
            website.setText(settings.getSettings().getWebsite());
            description.setText(settings.getSettings().getDescription());
            email.setText(settings.getUsers().getEmail());
            phoneNumber.setText(String.valueOf(settings.getUsers().getPhone_number()));
        }
    }

    private void saveProfileSettings() {
        final String username = EditProfileFragment.this.username.getText().toString();
        String display_name = EditProfileFragment.this.display_name.getText().toString();
        String website = EditProfileFragment.this.website.getText().toString();
        String description = EditProfileFragment.this.description.getText().toString();
        email1 = EditProfileFragment.this.email.getText().toString();
        long phoneNumber = Long.parseLong(EditProfileFragment.this.phoneNumber.getText().toString());
        if (!settings.getUsers().getUsername().equals(username))
            checkIfUsernameAlreadyExists(username);
        if (!settings.getUsers().getEmail().equals(email1)) {
            ConfirmDialogPassword confirmPassword = new ConfirmDialogPassword();
            confirmPassword.show(getFragmentManager(), getString(R.string.dialog_confirm_password));
        }
        if(!String.valueOf(settings.getSettings().getDisplay_name()).equals(display_name)){
            firebaseMethods.updateUserAccountSettings(display_name,null,null,0);
        }
        if(!String.valueOf(settings.getSettings().getWebsite()).equals(website)){
            firebaseMethods.updateUserAccountSettings(null,website,null,0);
        }
        if(!String.valueOf(settings.getSettings().getDescription()).equals(description)){
            firebaseMethods.updateUserAccountSettings(null,null,description,0);
        }
        if(!String.valueOf(settings.getUsers().getPhone_number()).equals(String.valueOf(phoneNumber))){
            firebaseMethods.updateUserAccountSettings(null,null,null,phoneNumber);
        }
    }

    private void checkIfUsernameAlreadyExists(final String username) {

        //Querying Firebase Database
        Query query = myRef
                .child(getString(R.string.db_users_node))
                .orderByChild(getString(R.string.db_field_username))
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(mContext, "UserName Already Exists\n Try Another one", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseMethods.updateUsername(username);
                    getActivity().finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuth != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
        if (EventBus.getDefault() != null) {
            EventBus.getDefault().unregister(this);
        }
    }
}
