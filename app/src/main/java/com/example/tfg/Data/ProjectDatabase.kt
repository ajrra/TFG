package com.example.tfg.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Project::class, Page::class], version = 1, exportSchema = false)
@TypeConverters(convertidor::class)
abstract class ProjectDatabase : RoomDatabase(){

    abstract fun projectDao(): ProjectDao
    abstract fun pageDao(): PageDao

    companion object{
        @Volatile
        private var INSTANCE: ProjectDatabase? = null

        fun getDatabase(context: Context): ProjectDatabase {
            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                                        ProjectDatabase::class.java, "project-database").build()
                INSTANCE = instance
                return instance

            }
        }
    }
}