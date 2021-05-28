package com.example.tfg.Data

import androidx.lifecycle.LiveData

class ProjectRepository(private val projectDao: ProjectDao, private val pageDao: PageDao, private val answerDao: AnswerDao) {


    val getAllProjects: LiveData<List<Project>> = projectDao.getAllProjects()
    val getAllPages: LiveData<List<Page>> = pageDao.getAllPages()
    val getAll : LiveData<List<ProjectAndAll>> = projectDao.getAllProjectsComplete()
    val getAllAnswer : LiveData<List<Answer>> = answerDao.getAllAnswers()

     fun addProject(project: Project):Long = projectDao.insertProject(project)

     fun getProject(id:Int ): Project {
        return projectDao.getProject(id)
    }

     fun addPage(page: Page) = pageDao.insertPage(page)

     fun getPage(id:Int ,pId:Int): Page {
        return pageDao.getPage(id,pId)
    }

    fun deleteProject(project: Project){
        projectDao.deleteProject(project)
    }
}