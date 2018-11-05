package com.example.fasih.instagramapplication.Utils.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.fasih.instagramapplication.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by Fasih on 10/14/18.
 */

public class UniversalImageLoader {
    private static final int defaultImage = R.drawable.ic_android;
    private Context mContext;

    public UniversalImageLoader(Context mContext) {
        this.mContext = mContext;
    }

    public ImageLoaderConfiguration getConfig() {
        DisplayImageOptions.Builder imageOptionsBuilder = new DisplayImageOptions.Builder();
        imageOptionsBuilder.showImageOnLoading(defaultImage);
        imageOptionsBuilder.showImageForEmptyUri(defaultImage);
        imageOptionsBuilder.showImageOnFail(defaultImage);
        imageOptionsBuilder.cacheOnDisk(true).cacheInMemory(true);
        imageOptionsBuilder.cacheOnDisk(true).resetViewBeforeLoading(true);
        imageOptionsBuilder.imageScaleType(ImageScaleType.EXACTLY);
        imageOptionsBuilder.displayer(new FadeInBitmapDisplayer(300));
        DisplayImageOptions defaultOptions=imageOptionsBuilder.build();
        ImageLoaderConfiguration.Builder configurationBuilder=
                new ImageLoaderConfiguration.Builder(mContext);
        configurationBuilder.defaultDisplayImageOptions(defaultOptions);
        configurationBuilder.memoryCache(new WeakMemoryCache());
        configurationBuilder.diskCacheSize(100*1024*1024);
        ImageLoaderConfiguration configuration=configurationBuilder.build();
        return configuration;
    }
    public static void setImage(String url, ImageView image, final ProgressBar mProgressBar,String append){
        ImageLoader imageLoader=ImageLoader.getInstance();
        imageLoader.displayImage(append + url, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if(mProgressBar!=null){
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if(mProgressBar!=null){
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(mProgressBar!=null){
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if(mProgressBar!=null){
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

    }

}
