package com.example.tfg;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.tfg.Data.Answer;
import com.example.tfg.Data.Page;
import com.example.tfg.Data.Project;
import com.example.tfg.Data.ProjectAndAll;
import com.example.tfg.Data.ProjectViewModel;
import com.example.tfg.Views.RectSubSamplingScaleImage;
import com.google.android.material.textfield.TextInputEditText;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class AnswerFillingActivity  extends AppCompatActivity {

    ProjectAndAll actual;
    private Bitmap bm;
    private ProjectViewModel mProjectViewModel;
    private RectSubSamplingScaleImage imageView;
    TextInputEditText text;
    ArrayList answerBools;

    private Mat actual_photo_answer ;

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        bm = mProjectViewModel.getLocalStorageAccess().loadImageFromStorage("opencv_mat");
                        imageView.setImage(ImageSource.bitmap(bm));
                        actual_photo_answer = new Mat();
                        Bitmap bm32 = bm.copy(Bitmap.Config.ARGB_8888,true);
                        Utils.bitmapToMat(bm32,actual_photo_answer);
                        actual_photo_answer = CV_Paper.preprocess_item(actual_photo_answer);
                        Imgproc.erode(actual_photo_answer,actual_photo_answer, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3)));
                        bm = Bitmap.createBitmap(actual_photo_answer.cols(), actual_photo_answer.rows(),Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(actual_photo_answer, bm);
                        imageView.setImage(ImageSource.bitmap(bm));
                        OpenCVLoader.initDebug();
                        // Handle the Intent
                    }
                }
            });

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.answer_filling);
        Button but = this.findViewById(R.id.answerButton_Photo);
        but.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("answerfilling", "called onClick");
                Intent i = new Intent(getApplicationContext(), PlantillaActivity.class);
                mStartForResult.launch(i);



            }
        });

        but = this.findViewById(R.id.answerButton_CHECK);
        but.setOnClickListener(v -> AnswerFillingActivity.this.evalItem());

        but = this.findViewById(R.id.answerButton_SAVE);
        but.setOnClickListener(v->saveAnswers());
        Bundle extras = this.getIntent().getExtras();
        mProjectViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);
        int projectId = extras.getInt("project");
        actual = mProjectViewModel.getAllProject(projectId);
        text = this.findViewById(R.id.answer_project_name);
        text.setText(actual.getProject().name);

        bm = mProjectViewModel.getLocalStorageAccess().loadImageFromStorage(actual.getProject().name+"_"+actual.getProject().id);


        mProjectViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);
        imageView = (RectSubSamplingScaleImage) this.findViewById(R.id.answerImage);
        imageView.rectangles=actual.getListPagesAndAnswers().get(0).getPage().quizL;


        imageView.setImage(ImageSource.bitmap(bm));





    }


private void saveAnswers (){
        if(answerBools!=null && !answerBools.isEmpty()) {
            Page mActualPage = actual.getListPagesAndAnswers().get(0).getPage();
            Answer mNew = new Answer();
            mNew.Page_fk = mActualPage.id;
            mNew.value = new ArrayList<>();
            mNew.value.addAll(answerBools);
            mProjectViewModel.insertAnswer(mNew);
            answerBools.removeAll(answerBools);
            Toast.makeText(this,"Respuestas Guardadas", Toast.LENGTH_SHORT).show();
            imageView.invalidate();
        }
    }


    private void evalItem(){
        answerBools = new ArrayList();
        for (RectF x : imageView.rectangles){
                answerBools.add(CV_Paper.Eval(actual_photo_answer, x));
        }
        imageView.rect_val = answerBools;
        imageView.invalidate();
    }


}
