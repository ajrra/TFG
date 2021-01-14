package com.example.tfg.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.example.tfg.Data.Project
import androidx.room.RoomDatabase

@Database(entities = [Project::class], version = 1, exportSchema = false)
abstract class ProjectDatabase : RoomDatabase(){

    abstract fun projectDao(): ProjectDao

    companion object{
        @Volatile
        private var INSTANCE: ProjectDatabase? = null

        fun getDatabase(context: Context): ProjectDatabase{
            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                                        ProjectDatabase::class.java, "project_database").build()
                INSTANCE= instance
                return instance

            }
        }
    }
}