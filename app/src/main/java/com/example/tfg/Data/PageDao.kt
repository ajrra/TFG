package com.example.tfg.Data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface PageDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPage(project: Page)

    @Query("SELECT * FROM page_table WHERE id=:id AND Project_fk=:pId")
    fun getPage(id: Int, pId: Int): Page

    @Query("SELECT * FROM page_table")
    fun getAllPages(): LiveData<List<Page>>

}