package com.example.tfg;

import android.graphics.Bitmap;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

@Entity
class Quadrilateral {
    @PrimaryKey(autoGenerate = true)
    private int id;
    public MatOfPoint contour;
    public Point[] points;
    public Bitmap img;

    public Quadrilateral(MatOfPoint contour, Point[] points) {
        this.contour = contour;
        this.points = points;
    }
}

@Dao
public interface MyDaoInterface {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUsers(Quadrilateral... quad);


}
