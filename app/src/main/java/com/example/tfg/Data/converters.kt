package com.example.tfg.Data

import android.graphics.RectF
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class convertidor {



    @TypeConverter
    fun fromSource(source: ArrayList<RectF>) :String{
        return Gson().toJson(source)
    }
    @TypeConverter
    fun  tvSource(name: String): ArrayList<RectF> {
        val type = object : TypeToken<ArrayList<RectF>>() {}.type
        return Gson().fromJson(name,type)
    }

}