package com.example.tfg.Data


class XcelConverter {

    fun listBooleanToString(list: ArrayList <Boolean>): String
    {
        var value = ""
        for(i in list)
        {
            if(i) value += "1,"
            else value += "0,"
        }
        value +="\n"
        return value
    }


}