package com.example.tfg.Data

import androidx.room.Embedded
import androidx.room.Relation

class ProjectAndAll{
    @Embedded
    var project: Project? = null
    @Relation(parentColumn = "id", entityColumn = "Project_fk", entity = Page::class)
    var listPagesAndAnswers: List<PagesAndAnswers>? = null

}



class PagesAndAnswers {
    @Embedded
    var page: Page? = null

    @Relation(parentColumn = "id", entityColumn = "Page_fk", entity = Answer::class)
    var listAnswers: List<Answer>? = null
}