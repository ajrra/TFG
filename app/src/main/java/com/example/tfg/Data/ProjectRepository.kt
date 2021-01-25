package com.example.tfg.Data

import androidx.lifecycle.LiveData

class ProjectRepository(private val projectDao: ProjectDao, private val pageDao: PageDao) {


    val getAllProjects: LiveData<List<Project>> = projectDao.getAllProjects()
    val getAllPages: LiveData<List<Page>> = pageDao.getAllPages()

    suspend fun addProject(project: Project) = projectDao.insertProject(project)

    suspend fun getProject(id:Int ): Project {
        return projectDao.getProject(id)
    }

    suspend fun addPage(page: Page) = pageDao.insertPage(page)

    suspend fun getPage(id:Int ,pId:Int): Page {
        return pageDao.getPage(id,pId)
    }
}