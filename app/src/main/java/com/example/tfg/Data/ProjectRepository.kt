package com.example.tfg.Data

import androidx.lifecycle.LiveData

class ProjectRepository(private val projectDao: ProjectDao, private val pageDao: PageDao) {


    val getAllProjects: LiveData<List<Project>> = projectDao.getAllProjects()
    val getAllPages: LiveData<List<Page>> = pageDao.getAllPages()

     fun addProject(project: Project):Long = projectDao.insertProject(project)

     fun getProject(id:Int ): Project {
        return projectDao.getProject(id)
    }

     fun addPage(page: Page) = pageDao.insertPage(page)

     fun getPage(id:Int ,pId:Int): Page {
        return pageDao.getPage(id,pId)
    }
}