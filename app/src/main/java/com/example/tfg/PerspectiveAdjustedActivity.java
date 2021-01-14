package com.example.tfg;


import android.graphics.Bitmap;

import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.tfg.Data.*;
import com.example.tfg.Views.RectSubSamplingScaleImage;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.ArrayList;

public class PerspectiveAdjustedActivity extends AppCompatActivity {

    private ArrayList<RectF> quizL;
    private Mat m;
    private PointF topL, botR;
    private RectSubSamplingScaleImage imageView;
    private int count =0;
    private ProjectViewModel mUserViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        quizL=new ArrayList<>();
        mUserViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.show_paper_id);
        Button but = this.findViewById(R.id.button5);
        Bundle extras = this.getIntent().getExtras();
        long imagePath = extras.getLong("image");
        m = new Mat( imagePath );
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRect();
                imageView.invalidate();
            }
        });
        Bitmap bm = Bitmap.createBitmap(m.cols(), m.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(m, bm);


        imageView = (RectSubSamplingScaleImage) this.findViewById(R.id.imageSingle);
        imageView.rectangles=quizL;




        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (imageView.isReady()) {
                    PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());
                    Log.i("Tap", "onSingleTapConfirmed: "+count);
                    count++;
                    switch (count){
                        case 1:
                            topL = new PointF(sCoord.x,sCoord.y);
                            imageView.setPin(topL);
                            break;
                        case 2:
                            botR= new PointF(sCoord.x,sCoord.y);
                            imageView.setPin(botR);
                            break;
                        default:
                            count=0;
                            break;
                    }
                }
                return true;
            }
        });
        imageView.setImage(ImageSource.bitmap(bm));
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });




    }


private void addRect(){
    if(topL!=null && botR!=null ){
       quizL.add(new RectF(topL.x,topL.y,botR.x,botR.y));
    }
}


private void save(){
 Project nProject = new Project();
}


}
