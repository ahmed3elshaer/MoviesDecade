package com.ahmed3elshaer.moviesdecade.data.room

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converter {

    @TypeConverter
    fun listToJson(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun stringToList(value: String): List<String> {
        return Gson().fromJson(value, Array<String>::class.java).toList()
    }
}