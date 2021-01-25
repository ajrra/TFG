package com.example.tfg.Data;

import android.graphics.RectF;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;

@Entity(tableName = "page_table", foreignKeys = {@ForeignKey(entity = Project.class,
        parentColumns = "id",
        childColumns = "Project_fk")}  )
public class Page{
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "Project_fk")
    public int Project_fk;
    // public Mat img;

    public ArrayList<RectF> quizL;
}