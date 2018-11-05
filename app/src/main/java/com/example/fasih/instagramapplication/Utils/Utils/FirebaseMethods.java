package com.example.fasih.instagramapplication.Utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fasih.instagramapplication.R;
import com.example.fasih.instagramapplication.Utils.Dialog.UploadImageDialog;
import com.example.fasih.instagramapplication.Utils.Home.HomeActivity;
import com.example.fasih.instagramapplication.Utils.Models.Photo;
import com.example.fasih.instagramapplication.Utils.Models.UserAccountSettings;
import com.example.fasih.instagramapplication.Utils.Models.UserSettings;
import com.example.fasih.instagramapplication.Utils.Models.Users;
import com.example.fasih.instagramapplication.Utils.Profile.AccountSettingActivity;
import com.example.fasih.instagramapplication.Utils.Share.NextActivity;
import com.example.fasih.instagramapplication.Utils.Share.ShareActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Fasih on 10/20/18.
 */

public class FirebaseMethods {
    private FirebaseAuth mAuth;
    private Context mContext;
    private String userID;
    private DatabaseReference myRef;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference mStorageReference;
    private UploadImageDialog dialogBox;
    private FragmentManager manager;

    public FirebaseMethods(Context context) {
        this.mContext = context;
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        authenticateUser();
    }

    private void authenticateUser() {
        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public void initUserRegistration(String email, String username, String password, final ProgressBar register_progress, final LinearLayout linLayout1) {
        register_progress.setVisibility(View.VISIBLE);
        linLayout1.setAlpha(0.5f);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        register_progress.setVisibility(View.GONE);
                        linLayout1.setAlpha(1.0f);
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            userID = user.getUid();
                            sendVerificationEmail(user);

                        } else {
                            if (task.getException() instanceof FirebaseNetworkException) {
                                Toast.makeText(mContext
                                        , "Network_Exception"
                                        , Toast.LENGTH_SHORT).show();
                            }
                            if (task.getException() instanceof FirebaseAuthException) {
                                Toast.makeText(mContext
                                        , "Auth_Exception"
                                        , Toast.LENGTH_SHORT).show();
                            }
                            if (task.getException() instanceof FirebaseAuthEmailException) {
                                Toast.makeText(mContext
                                        , "Auth_Email_Exception"
                                        , Toast.LENGTH_SHORT).show();
                            }
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(mContext
                                        , "Auth_User_Collision_Exception"
                                        , Toast.LENGTH_SHORT).show();
                            }
                            if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                Toast.makeText(mContext
                                        , "Auth_Weak_Password_Exception"
                                        , Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    private void sendVerificationEmail(FirebaseUser user) {
        Task<Void> verification = user.sendEmailVerification();
        verification.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(mContext, "!!!Error occurs while Verifying the Email!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addNewUser(String email, String username, String description, String website, String profile_photo) {
        Users users = new Users(userID, 1, email, StringManipulation.condenseUsername(username));
        myRef
                .child(mContext.getString(R.string.db_users_node))
                .child(userID)
                .setValue(users);
        UserAccountSettings settings = new UserAccountSettings(
                description
                , username
                , 0
                , 0
                , 0
                , profile_photo
                , StringManipulation.condenseUsername(username)
                , website);
        myRef
                .child(mContext.getString(R.string.db_user_account_settings))
                .child(userID)
                .setValue(settings);
    }

    public UserSettings getUserSettings(DataSnapshot dataSnapshot) {
        Users users = null;
        UserSettings userSettings = null;
        UserAccountSettings settings = null;
        if (dataSnapshot.getValue() != null) {
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                if (ds.getKey().equals(mContext.getString(R.string.db_users_node))) {
                    users = ds.child(userID).getValue(Users.class);
                }
                if (ds.getKey().equals(mContext.getString(R.string.db_user_account_settings))) {
                    settings = ds.child(userID).getValue(UserAccountSettings.class);
                }
            }
            userSettings = new UserSettings(users, settings);
        }


        return userSettings;
    }

    public void updateUsername(String username) {
        StringManipulation.condenseUsername(username);

        myRef
                .child(mContext.getString(R.string.db_users_node))
                .child(userID)
                .child(mContext.getString(R.string.db_field_username))
                .setValue(username);
        myRef
                .child(mContext.getString(R.string.db_user_account_settings))
                .child(userID)
                .child(mContext.getString(R.string.db_field_username))
                .setValue(username);
    }

    public void updateEmailAddress(String email) {
        myRef.child(mContext.getString(R.string.db_users_node)).child(userID)
                .child(mContext.getString(R.string.db_field_email)).setValue(mAuth.getCurrentUser().getEmail());
    }

    public void updateUserAccountSettings(String display_name, String website, String description, long phoneNumber) {
        if (display_name != null) {
            myRef.child(mContext.getString(R.string.db_user_account_settings)).child(userID).child(mContext.getString(R.string.db_field_display_name)).setValue(display_name);
        }
        if (website != null) {
            myRef.child(mContext.getString(R.string.db_user_account_settings)).child(userID).child(mContext.getString(R.string.db_field_website)).setValue(website);
        }
        if (description != null) {
            myRef.child(mContext.getString(R.string.db_user_account_settings)).child(userID).child(mContext.getString(R.string.db_field_description)).setValue(description);
        }
        if (phoneNumber != 0) {
            myRef.child(mContext.getString(R.string.db_users_node)).child(userID).child(mContext.getString(R.string.db_field_phone_number)).setValue(phoneNumber);
        }
    }

    public int getPhotosCount(DataSnapshot dataSnapshot) {
        int count = 0;
        for (DataSnapshot ds : dataSnapshot
                .child(mContext.getString(R.string.db_user_photos_node))
                .child(userID)
                .getChildren()) {
            count++;

        }
        return count;
    }

    public void addNewPhoto(final String photoType, final String caption, int photo_count, final String imgUrl, Bitmap bitmap) {

        if (photoType.equals(mContext.getString(R.string.new_photo))) {
            dialogBox = null;
            mStorageReference = mStorageReference.child(new StorageFilePaths().firebaseStorageDirectoryPath + "/" + userID + "/photo" + (photo_count + 1));
            if(bitmap==null)
            bitmap = ImageManager.getBitmap(UrlManager.originalUrl(imgUrl));
            byte[] bytes = ImageManager.getBytes(bitmap, 100);
            if (bytes.length > 1) {

                UploadTask task = mStorageReference.putBytes(bytes);
                task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri imageDownloadUrl = taskSnapshot.getDownloadUrl();
                        setupImageDatabase(caption, imageDownloadUrl);
                        Intent intent = new Intent(mContext, HomeActivity.class);
                        mContext.startActivity(intent);
                        if (dialogBox != null)
                            dialogBox.dismiss();
                        ((NextActivity) mContext).finish();
                    }
                });
                task.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (dialogBox != null)
                            dialogBox.dismiss();

                    }
                });
                task.addOnProgressListener((Activity) mContext, new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        manager = ((NextActivity) mContext).getSupportFragmentManager();
                        if (dialogBox == null) {
                            dialogBox = new UploadImageDialog();
                            dialogBox.show(manager
                                    , mContext.getString(R.string.dialog_upload_image));
                        }
                    }
                });
            }
        }
        if (photoType.equals(mContext.getString(R.string.profile_photo))) {
            dialogBox = null;
            mStorageReference = mStorageReference.child(new StorageFilePaths().firebaseStorageDirectoryPath + "/" + userID + "/profile_photo");
            if(bitmap==null)
                bitmap = ImageManager.getBitmap(UrlManager.originalUrl(imgUrl));
            byte[] bytes = ImageManager.getBytes(bitmap, 100);
            if (bytes.length > 1) {

                UploadTask task = mStorageReference.putBytes(bytes);
                task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri imageDownloadUrl = taskSnapshot.getDownloadUrl();
                        setupProfileImageDatabase(imageDownloadUrl);
                        if (dialogBox != null)
                            dialogBox.dismiss();
                    }
                });
                task.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (dialogBox != null)
                            dialogBox.dismiss();
                    }
                });
                task.addOnProgressListener((Activity) mContext, new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        FragmentManager manager = ((AccountSettingActivity) mContext).getSupportFragmentManager();
                        if (dialogBox == null) {
                            dialogBox = new UploadImageDialog();
                            dialogBox.show(manager, mContext.getString(R.string.dialog_upload_image));
                        }
                    }
                });
            }

        }

    }

    private void setupImageDatabase(String caption, Uri imageDownloadUrl) {
        String photo_id = myRef.push().getKey();

        Photo photo = new Photo(caption
                , getTimeStamp()
                , imageDownloadUrl.toString()
                , photo_id
                , mAuth.getCurrentUser().getUid()
                , StringManipulation.getTags(caption)
        );
        myRef.child(mContext.getString(R.string.db_user_photos_node)).child(userID).child(photo_id).setValue(photo);
        myRef.child(mContext.getString(R.string.db_photo_node)).child(photo_id).setValue(photo);
    }

    private String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'T' hh:mm:ss z", Locale.CANADA);
        return sdf.format(new Date());
    }

    private void setupProfileImageDatabase(Uri imageDownloadUrl) {
        myRef.child(mContext.getString(R.string.db_user_account_settings)).child(userID).child(mContext.getString(R.string.profile_photo)).setValue(imageDownloadUrl.toString());

    }
}
