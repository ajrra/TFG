package com.example.tfg;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;


public class PaperActivity extends AppCompatActivity {


    private ImageView imageView;
    private File imageFile;
    Bitmap img;
    ActivityResultLauncher<Intent> mStartForResult = prepareCall(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        img=(Bitmap) BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        imageView.setImageBitmap(img);
                        // Handle the Intent
                    }
                }
            });

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_paper);
        this.imageView = (ImageView)this.findViewById(R.id.imageView1);
        Button photoButton = (Button) this.findViewById(R.id.buttonTakePhoto);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pass in the mime type you'd like to allow the user to select
                // as the input
                String fileName="photo";
                File StorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    imageFile =  File.createTempFile(fileName,".jpg",StorageDir);
                    Uri imageUri = FileProvider.getUriForFile(PaperActivity.this,"com.example.tfg.fileprovider",imageFile);
                    Intent intentCamera = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
                    intentCamera.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    mStartForResult.launch(intentCamera);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
       Button nextButton = (Button) this.findViewById(R.id.buttonNext);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageFile!=null){
                Intent next = new Intent(getApplicationContext(),PaperIdentification.class);
                next.putExtra("image",imageFile.getAbsolutePath());
                startActivity(next);
                    }
                }
            });
        }
    }




