package com.example.tfg;


import android.content.Intent;
import android.graphics.Bitmap;

import com.example.tfg.Data.Page;
import com.example.tfg.Data.Project;
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
import com.example.tfg.Data.ProjectViewModel;
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
    private ProjectViewModel mProjectViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        quizL=new ArrayList<>();


        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.show_paper_id);
        Button but = this.findViewById(R.id.button4);
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
        Button but2 = this.findViewById(R.id.button5);
        but2.setOnClickListener(new SaveButtonClick());

        Bitmap bm = Bitmap.createBitmap(m.cols(), m.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(m, bm);

        mProjectViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);
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
        imageView.setOnTouchListener((view, motionEvent) -> gestureDetector.onTouchEvent(motionEvent));




    }


private void addRect(){
    if(topL!=null && botR!=null ){
       quizL.add(new RectF(topL.x,topL.y,botR.x,botR.y));
    }
}


    class SaveButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            saveData();
            next();

        }
    }


 void saveData(){
     Project nProject = new Project();
     nProject.id= (int)mProjectViewModel.addProject(nProject);
     Page page = new Page();
     page.quizL= quizL;
     page.Project_fk = nProject.id;
     mProjectViewModel.addPage(page);

 }

 void next(){
     Intent go = new Intent(this, MainActivity.class);
     go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
     startActivity(go);

 }


}
