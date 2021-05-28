package com.example.tfg.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "answer_table", foreignKeys = {@ForeignKey(entity = Page.class,
        parentColumns = "id",
        childColumns = "Page_fk", onDelete = CASCADE)}  )
public class Answer {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "Page_fk")
    public int Page_fk;
    String value;
}
