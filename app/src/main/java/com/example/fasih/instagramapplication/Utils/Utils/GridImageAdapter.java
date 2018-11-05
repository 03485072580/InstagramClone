package com.example.fasih.instagramapplication.Utils.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.fasih.instagramapplication.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Fasih on 10/17/18.
 */

public class GridImageAdapter extends BaseAdapter {
    private Context mContext;
    private ProgressBar gridProgress;

    public GridImageAdapter(Context mContext) {
        this.mContext = mContext;
        initGridImages();
    }


    private ArrayList arrayList;


    private void initGridImages() {
        if (!ImageLoader.getInstance().isInited())
            ImageLoader.getInstance().init(new UniversalImageLoader(mContext).getConfig());
    }


    WidgetsHandler widgetsHandler;

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater systemService = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = systemService.inflate(R.layout.actvity_profile_grid, parent, false);
            widgetsHandler = new WidgetsHandler(convertView);
            convertView.setTag(widgetsHandler);
        } else {
            Object tag = convertView.getTag();
            widgetsHandler = (WidgetsHandler) tag;
        }
        UniversalImageLoader.setImage((String) arrayList.get(position), widgetsHandler.profile_grid_image,
                gridProgress, "");

        return convertView;
    }

    public void setArrayList(ArrayList arrayList) {
        this.arrayList = arrayList;
    }

    public void setGridProgress(ProgressBar gridProgress) {
        this.gridProgress = gridProgress;
    }

    private class WidgetsHandler {
        ImageView profile_grid_image;

        public WidgetsHandler(View convertView) {
            profile_grid_image = convertView.findViewById(R.id.profile_grid_image);
        }
    }
}
