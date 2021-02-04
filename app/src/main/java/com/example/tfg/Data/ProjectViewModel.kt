package com.example.tfg.Data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

 class ProjectViewModel(application: Application): AndroidViewModel(application) {
    public val  allProject : LiveData<List<Project>>
    public val allData  : LiveData<List<ProjectAndAll>>
    public val allPage : LiveData<List<Page>>
    private val repository : ProjectRepository

    init{
        val database =ProjectDatabase.getDatabase(application)
        val projectDao = database.projectDao()
        val pageDao = database.pageDao()
        val answerDao = database.AnswerDao()

        repository = ProjectRepository(projectDao,pageDao,  answerDao)
        allProject = repository.getAllProjects
        allPage= repository.getAllPages
        allData = repository.getAll
    }

    fun addProject(project: Project):Long{
        //need this on main thread
        return  repository.addProject(project)
    }
     fun addPage(page:Page){
         viewModelScope.launch(Dispatchers.IO) { repository.addPage(page) }

     }

}