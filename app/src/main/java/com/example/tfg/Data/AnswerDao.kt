package com.example.tfg.Data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface AnswerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAnswer(answer: Answer)


    @Query("SELECT * FROM answer_table")
    fun getAllAnswers(): LiveData<List<Answer>>

    @Query("SELECT * FROM answer_table WHERE Page_fk = :id")
    fun getAllAnswersFromPage(id:Int): LiveData<List<Answer>>
}