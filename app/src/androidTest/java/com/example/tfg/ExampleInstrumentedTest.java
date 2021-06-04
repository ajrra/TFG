package com.example.tfg;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Before
    public void cv_load() {
        if (!OpenCVLoader.initDebug()) {

            fail();
        } else {

        }
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.tfg", appContext.getPackageName());
    }


    @Test
    public void addition_isCorrect() {
        int w = 500, h = 500;

        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(w, h, conf);
        Mat m = new Mat();
        Bitmap bm32 = bmp.copy(Bitmap.Config.ARGB_8888,true);
        Utils.bitmapToMat(bm32,m);

        assertNotNull(m);


    }




   /* @Test
    public Mat onCameraFrame(){



        //Mat mRGBA = ;
       // Mat mRGBAT = mRGBA.clone();

        Mat tmp = mRGBAT.clone();
        Quadrilateral quad= CV_Paper.findDocument(tmp,tmp.size(), new Point(0,0));
        if(quad !=null) {
            ArrayList<MatOfPoint> contour = new ArrayList<MatOfPoint>();
            contour.add(quad.contour);
            Imgproc.drawContours(tmp, contour, -1,  new Scalar(0, 255, 0, 255),5);
        }

        return tmp;

        }*/


    }


