package com.example.fasih.instagramapplication.Utils.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Fasih on 10/28/18.
 */

public class ImageManager {

    public static Bitmap getBitmap(String url) {
        Bitmap bitmap = null;
        InputStream stream = null;
        if(url!=null) {
            File file = new File(url);
            try {
                stream = new FileInputStream(file);
                bitmap = BitmapFactory.decodeStream(stream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
            return bitmap;
    }

    public static byte[] getBytes(Bitmap bitmap,int quality) {
        ByteArrayOutputStream stream=null;
        byte[]bytes={0};
        if(quality>-1 && quality<101) {
            stream = new ByteArrayOutputStream();
            if(bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)){
                return stream.toByteArray();
            }

        }
        return bytes;
    }

    public static String[] getIndividualDirectoryNames(ArrayList<String> directoriesList) {

        final String[] directories = new String[directoriesList.size()];
        if (!directoriesList.isEmpty()) {
            for (int i = 0; i < directoriesList.size(); i++) {
                directories[i] = directoriesList.get(i).substring(directoriesList.get(i).lastIndexOf("/") + 1);
            }
        }
        return directories;
    }
}

