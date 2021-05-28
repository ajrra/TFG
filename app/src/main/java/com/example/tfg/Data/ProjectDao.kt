package com.example.tfg.Data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProject(project: Project): Long

    @Query("SELECT * FROM project_table WHERE id=:id")
    fun getProject(id: Int): Project

    @Query("SELECT * FROM project_table")
    fun getAllProjects(): LiveData<List<Project>>

    @Query("SELECT * FROM project_table")
    fun getAllProjectsComplete(): LiveData<List<ProjectAndAll>>

    @Delete
    fun deleteProject(project: Project)
}