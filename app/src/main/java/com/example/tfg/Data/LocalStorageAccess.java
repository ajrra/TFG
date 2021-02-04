package com.example.tfg.Data;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class LocalStorageAccess {

    ContextWrapper cw;
    LocalStorageAccess localSingle;



    public LocalStorageAccess(Context context) {
        this.cw = new ContextWrapper(context);
    }

    public String saveToInternalSorage(Bitmap bitmapImage, String name) {

        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
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

    public Bitmap loadImageFromStorage(String path,String name) {

        try {

            File path1 = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File f = new File(path1, name+".png");
            return BitmapFactory.decodeStream(new FileInputStream(f));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
