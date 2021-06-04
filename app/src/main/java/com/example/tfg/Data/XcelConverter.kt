package com.example.tfg.Data


class XcelConverter {

    fun listBooleanToString(list: ArrayList <Boolean>): String
    {
        var value = ""
        for(i in list)
        {
            value += if(i) "1,"
            else "0,"
        }
        value =value.dropLast(1);
        value +="\n"
        return value
    }


}