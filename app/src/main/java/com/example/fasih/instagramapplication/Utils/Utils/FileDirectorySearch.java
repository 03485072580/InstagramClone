package com.example.fasih.instagramapplication.Utils.Utils;

import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 * Created by Fasih on 10/27/18.
 */

public class FileDirectorySearch {

    public static ArrayList<String> getDirectories(String dir) {
        ArrayList<String> directories = new ArrayList<>();

        File file = new File(dir);
        File[] filePaths = file.listFiles();
        for (File file1 : filePaths) {
            if (file1.isDirectory()) {
                directories.add(file1.getAbsolutePath());
            }
        }
        return directories;
    }

    public static ArrayList<String> getFiles(String dir,String append) {
        ArrayList<String> files = new ArrayList<>();
        FileFilter fileFilter=new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if(pathname!=null){
                    String extension=pathname.getAbsolutePath().substring(pathname.getAbsolutePath().lastIndexOf("."));
                    if(extension.equals(".jpg")||extension.equals(".jpeg")|| extension.equals(".png") || extension.equals(".gif")|| extension.equals(".webp")|| extension.equals(".bmp")|| extension.equals("(.heic")|| extension.equals(".heif")){
                        return true;
                    }
                }
                return false;
            }
        };

        File file = new File(dir);
        File[] filePaths = file.listFiles(fileFilter);
        if (file.isDirectory()) {
            for (File file1 : filePaths) {
                if (file1.isFile()) {
                    files.add(append+file1.getAbsolutePath());
                }
            }
        }
        return files;
    }
}
