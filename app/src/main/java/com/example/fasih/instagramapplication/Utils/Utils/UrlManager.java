package com.example.fasih.instagramapplication.Utils.Utils;

/**
 * Created by Fasih on 10/31/18.
 */

public class UrlManager {

    public static String originalUrl(String imgUrl){
        if(imgUrl!=null)
        imgUrl=imgUrl.substring(imgUrl.indexOf("/")+2,imgUrl.length());
        return imgUrl;
    }
    public static String appendedUrl(String imgUrl,String append){
        imgUrl=append+imgUrl;
        return imgUrl;
    }
}
