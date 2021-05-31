package com.example.tfg;

import android.app.Activity;
import android.content.Intent;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.tfg.Data.Project;
import com.example.tfg.Data.ProjectAndAll;
import com.example.tfg.Data.ProjectViewModel;
import com.example.tfg.Views.RectSubSamplingScaleImage;
import com.google.android.material.textfield.TextInputEditText;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.Date;

public class AnswerFillingActivity  extends AppCompatActivity {

    ProjectAndAll actual;
    private Bitmap bm;
    private ProjectViewModel mProjectViewModel;
    private RectSubSamplingScaleImage imageView;
    TextInputEditText text;
    private int MY_REQUEST_CODE = 600;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.answer_filling);
        Button but = this.findViewById(R.id.answerButton1);
        but.setText("Photo");
        but.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("MainActivity", "called onClick");
                Intent i = new Intent(getApplicationContext(), PlantillaActivity.class);
                startActivityForResult(i,MY_REQUEST_CODE);


            }
        });
        Bundle extras = this.getIntent().getExtras();
        mProjectViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);
        int projectId = extras.getInt("project");
        actual = mProjectViewModel.getAllProject(projectId);
        text = this.findViewById(R.id.answer_project_name);
        text.setText(actual.getProject().name);

        bm = mProjectViewModel.getLocalStorageAccess().loadImageFromStorage(actual.getProject().name);


        mProjectViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);
        imageView = (RectSubSamplingScaleImage) this.findViewById(R.id.answerImage);
        imageView.rectangles=actual.getListPagesAndAnswers().get(0).getPage().quizL;


        imageView.setImage(ImageSource.bitmap(bm));





    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MY_REQUEST_CODE) {
                if (data != null) {
                    //TODO

                    bm = CV_Paper.unpackMat(data.getExtras().getLong("image"));
                    imageView.setImage(ImageSource.bitmap(bm));

                }

            }
        }
    }

    private void evalItem(){


    }


}
