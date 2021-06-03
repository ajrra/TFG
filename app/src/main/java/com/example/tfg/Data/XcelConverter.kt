package com.example.tfg.Data


class XcelConverter {

    fun listBooleanToString(list: ArrayList <Boolean>): String
    {
        var value = ""
        for(i in list)
        {
            if(i) value += "T,"
            else value += "F,"
        }
        value +="\n"
        return value
    }


}