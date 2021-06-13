package com.example.tfg;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    // Used for logging success or failure messages

    ActivityResultLauncher<Intent> mStartForResult_Fill = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {


                        Intent next = new Intent(getApplicationContext(),AnswerFillingActivity.class);
                        next.putExtra("project",result.getData().getExtras().getInt("project"));
                        startActivity(next);
                        // Handle the Intent
                    }
                }
            });

    ActivityResultLauncher<Intent> mStartForResult_Answer = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent next = new Intent(getApplicationContext(),AnswerReviewSelectionActivity.class);
                        next.putExtra("project",result.getData().getExtras().getInt("project"));
                        startActivity(next);
                        // Handle the Intent
                    }
                }
            });

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {


                        Intent next = new Intent(getApplicationContext(),PerspectiveAdjustedActivity.class);
                        next.putExtra("image",result.getData().getExtras().getString("image"));
                        startActivity(next);


                        // Handle the Intent
                    }
                }
            });



    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity", "called onCreate");

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.main_activity);
        Button button1 = this.findViewById(R.id.button1);
        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("MainActivity", "called onClick");
                Intent i = new Intent(getApplicationContext(), CameraCVActivity.class);
                mStartForResult.launch(i);
            }
        });

        Button button2 = this.findViewById(R.id.button2);
        button2.setOnClickListener(v -> {
            Log.i("MainActivity", "called onClick");
            Intent i = new Intent(getApplicationContext(),ProjectSelectionActivity.class);
            mStartForResult_Fill.launch(i);
        });
        Button but3 = this.findViewById(R.id.button3);
        but3.setOnClickListener(v -> {
            Log.i("MainActivity", "called onClick_Answer");
            Intent i = new Intent(getApplicationContext(),ProjectSelectionActivity.class);
            mStartForResult_Answer.launch(i);
        });


        Button but4 = this.findViewById(R.id.button4);
        but4.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(),AboutActivity.class);
            startActivity(i);
        });
    }





    private static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }




}