package com.example.tfg.Data;

import android.graphics.RectF;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.opencv.core.Mat;

import java.util.ArrayList;

@Entity(tableName = "project_table")
public class Project  {
    public class Page{
        public Mat img;
        public ArrayList<RectF> quizL;

        public Page(Mat img, ArrayList<RectF> quizL) {
            this.img = img;
            this.quizL = quizL;
        }
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    public ArrayList<Page> pages;
}

