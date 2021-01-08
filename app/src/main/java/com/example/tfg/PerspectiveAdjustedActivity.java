package com.example.tfg;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.ortiz.touchview.TouchImageView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

public class PerspectiveAdjustedActivity extends AppCompatActivity {

    private TouchImageView imageView;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.show_paper_id);
        this.imageView = this.findViewById(R.id.imageSingle);
        Bundle extras = this.getIntent().getExtras();
        long imagePath = extras.getLong("image");
        Mat tempImg = new Mat( imagePath );
        Mat m = tempImg.clone();
        Bitmap bm = Bitmap.createBitmap(m.cols(), m.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(m, bm);
        imageView.setImageBitmap(bm);

    }

}
