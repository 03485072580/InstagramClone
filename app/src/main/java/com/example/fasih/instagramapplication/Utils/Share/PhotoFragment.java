package com.example.fasih.instagramapplication.Utils.Share;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fasih.instagramapplication.R;
import com.example.fasih.instagramapplication.Utils.Profile.AccountSettingActivity;
import com.example.fasih.instagramapplication.Utils.Utils.FragmentCurrentItem;

import static android.app.Activity.RESULT_OK;

public class PhotoFragment extends Fragment implements FragmentCurrentItem {

    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int FRAGMENT_PHOTO_NUMBER = 1;
    private static final int FRAGMENT_GALLERY_NUMBER = 0;

    public PhotoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        return view;
    }

    private void setupCameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode==RESULT_OK ) {
            if (((ShareActivity) getActivity()).getTask() == 0) {
            } else {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    Intent cameraIntent = new Intent(getContext(), AccountSettingActivity.class);
                    cameraIntent.putExtra(getString(R.string.from_fragment_photo), getString(R.string.fragment_photo));
                    cameraIntent.putExtra(getString(R.string.camera_bitmap), bitmap);
                    startActivity(cameraIntent);
                    getActivity().finish();
                }
            }

        }
    }


    @Override
    public void setCurrentItem(int itemNo) {
        if (itemNo == FRAGMENT_PHOTO_NUMBER)
            setupCameraIntent();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((ShareActivity) getActivity()).getViewPager().setCurrentItem(FRAGMENT_GALLERY_NUMBER);
    }
}
