package com.example.fasih.instagramapplication.Utils.Share;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fasih.instagramapplication.R;
import com.example.fasih.instagramapplication.Utils.Models.UserAccountSettings;
import com.example.fasih.instagramapplication.Utils.Profile.AccountSettingActivity;
import com.example.fasih.instagramapplication.Utils.Utils.FileDirectorySearch;
import com.example.fasih.instagramapplication.Utils.Utils.GridImageAdapter;
import com.example.fasih.instagramapplication.Utils.Utils.ImageManager;
import com.example.fasih.instagramapplication.Utils.Utils.StorageFilePaths;
import com.example.fasih.instagramapplication.Utils.Utils.UniversalImageLoader;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private GridView gridView;
    private ProgressBar gallery_img_progress, gridview_progress;
    private AppCompatSpinner spinner;
    private ImageView cross, gallery_image;
    private TextView next;
    private String mSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        setupFragmentWidgets(view);
        setupSpinner();
        return view;
    }

    private void setupSpinner() {
        final ArrayList<String> directoriesList = FileDirectorySearch.getDirectories(new StorageFilePaths().PicturesPath);
        directoriesList.addAll(FileDirectorySearch.getDirectories(new StorageFilePaths().CameraPath));
            spinner
                    .setAdapter(new ArrayAdapter(getContext()
                            , R.layout.support_simple_spinner_dropdown_item
                            , ImageManager.getIndividualDirectoryNames(directoriesList)));

            spinner
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                private ArrayList<String> filesPath = new ArrayList<>();

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    filesPath = FileDirectorySearch.getFiles(directoriesList.get(position), "file://");
                    setupImageGrid(filesPath);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }


    public void setupFragmentWidgets(View view) {
        gridView = view.findViewById(R.id.gridView);
        gallery_img_progress = view.findViewById(R.id.gallery_img_progress);
        spinner = view.findViewById(R.id.spinner);
        cross = view.findViewById(R.id.cross);
        next = view.findViewById(R.id.next);
        gallery_image = view.findViewById(R.id.gallery_image);
        gridview_progress = view.findViewById(R.id.gallery_gridview_progress);

        //Listeners
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((ShareActivity)getActivity()).getTask()==0){
                    startActivity(new Intent(getActivity()
                            ,NextActivity.class)
                            .putExtra(getString(R.string.selected_gallery_image),mSelected));
                }
                else{
                    startActivity(new Intent(getActivity(), AccountSettingActivity.class)
                            .putExtra(getString(R.string.selected_gallery_image),mSelected)
                            .putExtra(getString(R.string.return_to_fragment_editprofile),getString(R.string.edit_profile_fragment))
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    );
                    getActivity().finish();
                }
            }
        });

    }

    public void setupImageGrid(final ArrayList<String> filesPath) {
        GridImageAdapter adapter = new GridImageAdapter(getContext());
        adapter.setArrayList(filesPath);
        adapter.setGridProgress(gridview_progress);
        gridView.setAdapter(adapter);
        UniversalImageLoader.setImage(filesPath.get(0), gallery_image, gallery_img_progress, "");
        mSelected=filesPath.get(0);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                UniversalImageLoader.setImage(filesPath.get(position), gallery_image, gallery_img_progress, "");
                mSelected=filesPath.get(position);
            }
        });
    }
}
