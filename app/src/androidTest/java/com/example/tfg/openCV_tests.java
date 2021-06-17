package com.example.tfg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class openCV_tests {

    static{
        OpenCVLoader.initDebug();
    }

    Context con = InstrumentationRegistry.getInstrumentation().getContext();




    @Test
    public void test_1 (){


        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(720, 1280, conf);
        Mat m = new Mat();
        Bitmap bm32 = bmp.copy(Bitmap.Config.ARGB_8888,true);
        Utils.bitmapToMat(bm32,m);

        assertNotNull(m);

    }


//READ IMAGES
    @Test
    public void test_2() throws IOException {
        int i =1;
        for(i=1;i<20;i++) {
            Context ctx = InstrumentationRegistry.getInstrumentation().getContext();
            InputStream is = ctx.getResources().getAssets().open("Test" + i + ".jpg");
            Bitmap bm = BitmapFactory.decodeStream(is);
            assertNotNull(bm);
        }
    }






    @Test
    public void test_4() throws IOException {
        RectF roi = new RectF(250,180,280,140);
        int i =1;
        for(i=1;i<20;i++) {
            Context ctx = InstrumentationRegistry.getInstrumentation().getContext();
            InputStream is = ctx.getResources().getAssets().open("Test" + i + ".jpg");
            Bitmap bm = BitmapFactory.decodeStream(is);
            Mat m = new Mat();
            Bitmap bm32 = bm.copy(Bitmap.Config.ARGB_8888,true);
            Utils.bitmapToMat(bm32,m);
            RectF newAdd = CV_Paper.adjustItemTemplate(m,roi);
            if(newAdd == null){
                fail();
            }
        }
    }
    @Test
    public void test_5() throws IOException {
        RectF roi = new RectF(250,180,280,140);
        boolean[] vals = {false,false,false,true ,false ,true ,false ,true ,false ,false ,true,
                false ,false ,true,true ,true ,false ,false ,true ,false};
        int i =1;
        for(i=1;i<20;i++) {
            Context ctx = InstrumentationRegistry.getInstrumentation().getContext();
            InputStream is = ctx.getResources().getAssets().open("Test" + i + ".jpg");
            Bitmap bm = BitmapFactory.decodeStream(is);
            Mat m = new Mat();
            Bitmap bm32 = bm.copy(Bitmap.Config.ARGB_8888,true);
            Utils.bitmapToMat(bm32,m);
            RectF newAdd = CV_Paper.adjustItemTemplate(m,roi);
            if(newAdd == null){
                fail();
            }
            m = CV_Paper.preprocess_answer(m);
            assertEquals(CV_Paper.Eval(m,newAdd),vals[i-1]);


        }
    }

    @Test
    public void test_6() throws IOException {


        int i =1;
        for(i=1;i<20;i++) {
            Context ctx = InstrumentationRegistry.getInstrumentation().getContext();
            InputStream is = ctx.getResources().getAssets().open("Test" + i + ".jpg");
            Bitmap bm = BitmapFactory.decodeStream(is);
            Mat m = new Mat();
            Bitmap bm32 = bm.copy(Bitmap.Config.ARGB_8888,true);
            Utils.bitmapToMat(bm32,m);
            long start = System.currentTimeMillis();
            CV_Paper.preprocess(m);
            long end = System.currentTimeMillis();
            Assert.assertTrue(end-start<75);

        }
    }


}

