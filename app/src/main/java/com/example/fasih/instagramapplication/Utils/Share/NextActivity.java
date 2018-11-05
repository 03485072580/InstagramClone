package com.example.fasih.instagramapplication.Utils.Share;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fasih.instagramapplication.R;
import com.example.fasih.instagramapplication.Utils.Utils.FirebaseMethods;
import com.example.fasih.instagramapplication.Utils.Utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class NextActivity extends AppCompatActivity {

    private ImageView nextImage,backArrow;
    private EditText caption;
    private ProgressBar nextImage_progress;
    private TextView share;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;
    private int photo_count=0;
    private String imgUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        setupActivityWidgets();
        setupNextImage();
        setupFirebase();
    }

    private void setupActivityWidgets() {
        nextImage_progress=findViewById(R.id.nextImage_progress);
        nextImage=findViewById(R.id.nextImage);
        caption =findViewById(R.id.caption);
        share=findViewById(R.id.share);
        backArrow=findViewById(R.id.backArrow);

        //Listeners
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sharing stuff here
                firebaseMethods.addNewPhoto(getString(R.string.new_photo), caption.getText().toString(),photo_count,imgUrl,null);
            }
        });
    }

    private void setupNextImage() {
        Intent intent = getIntent();
        if (intent != null) {
            imgUrl = intent.getStringExtra(getString(R.string.selected_gallery_image));
            UniversalImageLoader.setImage(imgUrl,nextImage,nextImage_progress,"");
        }
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();
        firebaseMethods=new FirebaseMethods(NextActivity.this);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                }
            }
        };
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                photo_count=firebaseMethods.getPhotosCount(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuth != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

}
