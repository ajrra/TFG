package com.example.tfg;
/*

* https://bretahajek.com/2017/01/scanning-documents-photos-opencv/
* */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;


public class PaperIdentification extends AppCompatActivity {


    private ImageView imageView;
    private String imagePath;
    Bitmap img;


    private Mat mRgba;
    private Mat                    mIntermediateMat;
    private Mat                    mGray;
    Mat hierarchy;


    List<MatOfPoint> contours;

    BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(PaperIdentification.this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS: {
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (OpenCVLoader.initDebug()) {
            Log.i("TAG", "OpenCV initialize success");
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        } else {
            Log.i("TAG", "OpenCV initialize failed");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_12, this, baseLoaderCallback);
        }
        setContentView(R.layout.show_paper_id);
        this.imageView = (ImageView)this.findViewById(R.id.imageView2);
        Bundle extras = this.getIntent().getExtras();
        imagePath = extras.getString("image");
        img=findEdges((Bitmap) BitmapFactory.decodeFile(imagePath));

        imageView.setImageBitmap(img);

    }


    private Bitmap findEdges(Bitmap map){
        mRgba = new Mat(map.getHeight(),map.getWidth(), CvType.CV_8UC4);
        Size s_mRgba = mRgba.size();
        Bitmap tmp = map.copy(Bitmap.Config.ARGB_8888,true);
        Utils.bitmapToMat(tmp, mRgba);
        CV_Paper.Quadrilateral puntos = CV_Paper.findDocument(mRgba);
        if(puntos !=null) {
            ArrayList<MatOfPoint> contour = new ArrayList<MatOfPoint>();
            contour.add(puntos.contour);
            Imgproc.drawContours(mRgba, contours, -1,  new Scalar(0, 0, 255, 255),50);

        }

        Imgproc.resize(mRgba,mRgba,s_mRgba);
        Utils.matToBitmap(mRgba,map);
        return map;


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            Log.i("TAG", "OpenCV initialize success");
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        } else {
            Log.i("TAG", "OpenCV initialize failed");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_12, this, baseLoaderCallback);
        }
    }

}