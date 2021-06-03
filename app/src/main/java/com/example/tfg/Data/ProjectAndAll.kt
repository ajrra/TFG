package com.example.tfg.Data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation

class ProjectAndAll() : Parcelable{
    @Embedded
    var project: Project? = null
    @Relation(parentColumn = "id", entityColumn = "Project_fk", entity = Page::class)
    var listPagesAndAnswers: List<PagesAndAnswers>? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProjectAndAll> {
        override fun createFromParcel(parcel: Parcel): ProjectAndAll {
            return ProjectAndAll(parcel)
        }

        override fun newArray(size: Int): Array<ProjectAndAll?> {
            return arrayOfNulls(size)
        }
    }

}



class PagesAndAnswers {
    @Embedded
    var page: Page? = null

    @Relation(parentColumn = "id", entityColumn = "Page_fk", entity = Answer::class)
    var listAnswers: List<Answer>? = null
}