package com.example.tfg.Data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

 class ProjectViewModel(application: Application): AndroidViewModel(application) {
    private val  allData : LiveData<List<Project>>
    private val allPage : LiveData<List<Page>>
    private val repository : ProjectRepository

    init{
        val database =ProjectDatabase.getDatabase(application)
        val projectDao = database.projectDao()
        val pageDao = database.pageDao()

        repository = ProjectRepository(projectDao,pageDao)
        allData = repository.getAllProjects
        allPage= repository.getAllPages
    }

    fun addProject(project: Project):Long{
        //need this on main thread
        return  repository.addProject(project)
    }
     fun addPage(page:Page){
         viewModelScope.launch(Dispatchers.IO) { repository.addPage(page) }

     }

}