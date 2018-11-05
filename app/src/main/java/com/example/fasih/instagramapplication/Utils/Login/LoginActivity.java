package com.example.fasih.instagramapplication.Utils.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fasih.instagramapplication.R;
import com.example.fasih.instagramapplication.Utils.Home.HomeActivity;
import com.example.fasih.instagramapplication.Utils.Register.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private EditText mEmail, mPassword;
    private Button btn_login;
    private ProgressBar login_progress;
    private RelativeLayout relLayout1;
    private TextView textRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        settingUpWidgets();
        setupFirebase();
    }

    private void settingUpWidgets() {
        mEmail = findViewById(R.id.login_email);
        mPassword = findViewById(R.id.login_password);
        btn_login = findViewById(R.id.login);
        login_progress = findViewById(R.id.login_progress);
        relLayout1 = findViewById(R.id.relLayout1);
        textRegister = findViewById(R.id.navigating_to_register_activity);
        //Visibility
        login_progress.setVisibility(View.GONE);

        //Listeners
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                if (isValidate(email, password))
                    initFirebaseLogin(email, password);
                else
                    Toast.makeText(LoginActivity.this, "Required Email & Password", Toast.LENGTH_SHORT).show();
            }
        });

        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    private boolean isValidate(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            return false;
        }
        return true;
    }

    private void initFirebaseLogin(String email, String password) {
        relLayout1.setAlpha(0.5f);
        login_progress.setVisibility(View.VISIBLE);
        Task<AuthResult> authResultTask = mAuth.signInWithEmailAndPassword(email, password);
        authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                relLayout1.setAlpha(1.0f);
                login_progress.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    emailAuth();
                } else {
                    //Display Error Msg
                    Toast.makeText(LoginActivity.this, getString(R.string.firebase_error_msg), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void emailAuth() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (user.isEmailVerified()) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Email is not Verified...\n Check inbox for more information", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {

                }


            }
        };
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
