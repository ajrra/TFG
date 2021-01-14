package com.example.tfg.Data

import androidx.lifecycle.LiveData

class ProjectRepository(private val projectDao: ProjectDao) {


    val getAllProjects: LiveData<List<Project>> = projectDao.getAllProjects()

    suspend fun addProject(project: Project){
        projectDao.insertProject(project)
    }

    suspend fun getProject(id:Int ):Project{
        return projectDao.getProject(id)
    }
}