package com.example.tfg;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tfg.Data.ProjectViewModel;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;

import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class PlantillaActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    // Used for logging success or failure messages
    private static final String TAG = "OCVSample::Activity";

    // Loads camera view of OpenCV for us to use. This lets us see using OpenCV
    private JavaCameraView mOpenCvCameraView;
    private ProjectViewModel mProjectViewModel;

    Mat mRGBA, mRGBAT;
    Quadrilateral quad;

    BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(PlantillaActivity.this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS: {
                    mOpenCvCameraView.enableView();
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
    public void onCreate(Bundle savedInstanceState) {
        Log.i("plantillaActivity", "called onCreate");
        Bundle extras = this.getIntent().getExtras();

        mProjectViewModel = new ViewModelProvider(this).get(ProjectViewModel .class);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.show_camera);
        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.show_camera_activity_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        Button nextButton = (Button) this.findViewById(R.id.button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRGBAT!=null && quad !=null){
                    Intent next = new Intent();
                    Mat tmp = CV_Paper.perspectiveAdjust(mRGBAT,quad);
                    Core.rotate(tmp,tmp,Core.ROTATE_180);
                    Bitmap bm = Bitmap.createBitmap(tmp.cols(), tmp.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(tmp, bm);
                    next.putExtra("image",mProjectViewModel.getLocalStorageAccess().saveToInternalSorage(bm,"opencv_mat"));
                    setResult(Activity.RESULT_OK, next);
                    finish();
                }
            }
        });
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRGBA = new Mat(height, width, CvType.CV_8SC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRGBA.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRGBA = inputFrame.rgba();
        mRGBAT = new Mat();
        mRGBAT = mRGBA.clone();

        Mat tmp = mRGBAT.clone();
        quad= CV_Paper.findDocument(tmp,tmp.size(), new Point(0,0));
        if(quad !=null) {
            ArrayList<MatOfPoint> contour = new ArrayList<MatOfPoint>();
            contour.add(quad.contour);
            Imgproc.drawContours(tmp, contour, -1,  new Scalar(0, 255, 0, 255),5);
        }
        return tmp;
    }




    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            Log.i("TAG", "OpenCV initialize success");
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        } else {
            Log.i("TAG", "OpenCV initialize failed");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, baseLoaderCallback);
        }
    }

}

