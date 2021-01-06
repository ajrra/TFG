package com.example.tfg;

import android.graphics.Bitmap;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

import static androidx.room.ForeignKey.CASCADE;


@Entity
class Entry {
    @PrimaryKey(autoGenerate = true)
    private int id;


}

@Entity(foreignKeys = @ForeignKey(entity = Entry.class,
        parentColumns = "id",
        childColumns = "pageId",
        onDelete = CASCADE))
class Quadrilateral {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int entryId;
    public MatOfPoint contour;
    public Point[] points;

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
