package com.example.tfg;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

public class AboutActivity extends AppCompatActivity {

    PDFView pdfView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        pdfView=findViewById(R.id.pdfv);
        pdfView.fromAsset("About.pdf").load();



    }
}
