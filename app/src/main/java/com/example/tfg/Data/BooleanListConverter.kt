package com.example.tfg.Data

import androidx.room.TypeConverter

class BooleanListConverter {

    @TypeConverter fun stringToListBoolean(value: String): ArrayList<Boolean>
    {
        val list = ArrayList<Boolean>()
        value.forEach { c ->
            if(c == 'F') list.add(false)
            else list.add(true)
        }
        return list
    }
    @TypeConverter
    fun listBooleanToString(list: ArrayList <Boolean>): String
    {
        var value = ""
        for(i in list)
        {
            if(i) value += "T"
            else value += "F"
        }
        return value
    }
}