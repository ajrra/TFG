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

import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import java.util.List;


public class PaperIdentification extends AppCompatActivity {


    private ImageView imageView;
    private String imagePath;
    Bitmap img;


    private Mat                    mRgba;
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
        mRgba = new Mat(map.getHeight(),map.getWidth(), CvType.CV_8UC1);
       new CV_Paper.Quadrilateral Puntos = CV_Paper
        try {


        }catch (CvException ex){
            Log.d("PAPER", "findEdges: fail");
        }

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