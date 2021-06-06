package com.example.tfg.Data;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.tfg.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class LocalStorageAccess {

    ContextWrapper cw;
    private String  dir = "imageDir";
    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }


    public LocalStorageAccess(Context context) {
        this.cw = new ContextWrapper(context);
    }

    public String saveToInternalStorage(Bitmap bitmapImage, String name) {

        File directory = cw.getDir(dir, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, name+".png");

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to
            // the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }


    public void deleteImageFromStorage(String name){
            File path1 = cw.getDir(dir, Context.MODE_PRIVATE);
            File f = new File(path1, name+".png");
            f.delete();

    }

    public Bitmap loadImageFromStorage(String name) {

        try {

            File path1 = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File f = new File(path1, name+".png");
            return BitmapFactory.decodeStream(new FileInputStream(f));

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
        return  BitmapFactory.decodeResource(cw.getResources(),
                R.drawable.ic_launcher_background);
    }
}
