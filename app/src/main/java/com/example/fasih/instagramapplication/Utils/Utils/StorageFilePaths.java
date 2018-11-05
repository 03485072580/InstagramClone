package com.example.fasih.instagramapplication.Utils.Utils;

import android.os.Environment;

/**
 * Created by Fasih on 10/27/18.
 */

public class StorageFilePaths {

    //    storage/emulated/0
    public String defaultPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    public String CameraPath=defaultPath+"/DCIM";
    public String PicturesPath=defaultPath+"/Pictures";
    public String firebaseStorageDirectoryPath="photos/users";
}
