package com.example.tfg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.tfg.Data.LocalStorageAccess;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DaoAccesTests {

    LocalStorageAccess dao = new LocalStorageAccess(InstrumentationRegistry.getInstrumentation().getTargetContext());

    @Test
    public void save_img_1(){



    }
}
