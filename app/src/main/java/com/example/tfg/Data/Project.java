package com.example.tfg.Data;

import android.graphics.RectF;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import org.opencv.core.Mat;
import org.opencv.utils.Converters;

import java.util.ArrayList;

@Entity(tableName = "project_table")
public class Project  {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @Ignore
    public ArrayList<Page> pages;
    public String name;
}

