package com.example.tfg;


import android.content.Intent;
import android.graphics.Bitmap;

import com.example.tfg.Data.LocalStorageAccess;
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
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.tfg.Data.ProjectViewModel;
import com.example.tfg.Views.RectSubSamplingScaleImage;
import com.google.android.material.textfield.TextInputEditText;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PerspectiveAdjustedActivity extends AppCompatActivity {

    private ArrayList<RectF> quizL;
    private Mat m;
    private Bitmap bm;
    private PointF topL, botR;
    private RectSubSamplingScaleImage imageView;
    private int count =0;
    private ProjectViewModel mProjectViewModel;
    TextInputEditText text;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        quizL=new ArrayList<>();
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.show_paper_id);
        Button but = this.findViewById(R.id.button_add_rect);
        Bundle extras = this.getIntent().getExtras();
        mProjectViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);
        text = this.findViewById(R.id.project_name);

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss");
        String name = myDateObj.format(myFormatObj);


        text.setText(name);

        bm = mProjectViewModel.getLocalStorageAccess().loadImageFromStorage("opencv_mat");

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRect();
                imageView.invalidate();
            }
        });
        Button but2 = this.findViewById(R.id.button_save);
        but2.setOnClickListener(new SaveButtonClick());


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
                            count=0;
                            break;
                        default:
                            count=0;
                            break;
                    }
                }
                return true;
            }
        });

        Button b3 = this.findViewById(R.id.button_pop);
        b3.setOnClickListener(v -> popRect());
        imageView.setImage(ImageSource.bitmap(bm));
        imageView.setOnTouchListener((view, motionEvent) -> gestureDetector.onTouchEvent(motionEvent));
        m = new Mat();
        Bitmap bm32 = bm.copy(Bitmap.Config.ARGB_8888,true);
        Utils.bitmapToMat(bm32,m);





    }


private void addRect(){
    if(topL!=null && botR!=null ){

        RectF newAdd = CV_Paper.adjustItemTemplate(m,new RectF(topL.x,topL.y,botR.x,botR.y));
       if(newAdd != null){
        quizL.add(newAdd);
        }
    }
}

private void popRect(){
        if (quizL!=null && !quizL.isEmpty())
            quizL.remove(quizL.size()-1);
        imageView.invalidate();
}



    class SaveButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            saveData();


        }
    }


 void saveData(){


     Project nProject = new Project();
     Page page = new Page();
     nProject.name =text.getText().toString();
    nProject.name.trim();
    if(nProject.name.isEmpty()){
        Toast.makeText(this,"Empty title",Toast.LENGTH_SHORT).show();return;}
     LocalStorageAccess localDao = new LocalStorageAccess(getApplicationContext());





     nProject.id= (int)mProjectViewModel.addProject(nProject);
     page.img =   localDao.saveToInternalStorage(bm,nProject.name+"_"+nProject.id );

     page.quizL= quizL;
     page.Project_fk = nProject.id;
     mProjectViewModel.addPage(page);
    finish();
 }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        m.release();
    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }




}
