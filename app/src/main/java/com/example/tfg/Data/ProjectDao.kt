package com.example.tfg.Data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProject(project: Project)

    @Query("SELECT * FROM project_table WHERE id=:id")
    fun getProject(id: Int): Project

    @Query("SELECT * FROM project_table")
    fun getAllProjects(): LiveData<List<Project>>


}