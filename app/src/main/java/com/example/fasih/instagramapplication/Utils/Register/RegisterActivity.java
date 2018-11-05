package com.example.fasih.instagramapplication.Utils.Register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fasih.instagramapplication.R;
import com.example.fasih.instagramapplication.Utils.Utils.FirebaseMethods;
import com.example.fasih.instagramapplication.Utils.Utils.StringManipulation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private EditText mEmail, mPassword, mUsername;
    private Button btn_register;
    private ProgressBar register_progress;
    private LinearLayout linLayout1;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupFirebase();
        settingUpWidgets();
    }

    String username;
    String email;

    private void settingUpWidgets() {
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mUsername = findViewById(R.id.username);
        btn_register = findViewById(R.id.register);
        register_progress = findViewById(R.id.register_progress);
        linLayout1 = findViewById(R.id.linLayout1);
        firebaseMethods = new FirebaseMethods(RegisterActivity.this);

        //Visibility
        register_progress.setVisibility(View.GONE);

        //Listener
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmail.getText().toString();
                username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                if (isValidate(email, username, password))
                    firebaseMethods.initUserRegistration(email, username, password, register_progress, linLayout1);
                else
                    Toast.makeText(RegisterActivity.this, "All fields must be Filled out", Toast.LENGTH_LONG).show();

            }
        });
    }

    private boolean isValidate(String email, String username, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password))
            return false;
        return true;
    }

    private void setupFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    setupDatabase();
                }
            }
        };
    }

    private void checkIfUserNameAlreadyExists(final String username) {
        Query query = myRef
                .child(getString(R.string.db_users_node))
                .orderByChild(getString(R.string.db_field_username))
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //Found a match
                    RegisterActivity.this.username = username + myRef.push().getKey().substring(3, 10);
                }
                firebaseMethods.addNewUser(email, RegisterActivity.this.username, "", "", "");
                mAuth.signOut();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void setupDatabase() {
        checkIfUserNameAlreadyExists(username);
        finish();
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
