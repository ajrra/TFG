package com.example.tfg.Data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

 class ProjectViewModel(application: Application): AndroidViewModel(application) {
    private val  allData : LiveData<List<Project>>
    private val repository : ProjectRepository

    init{
        val userDao = ProjectDatabase.getDatabase(application).projectDao()
        repository = ProjectRepository(userDao)
        allData = repository.getAllProjects

    }

    fun addProject(project: Project){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addProject(project)
        }

    }
}