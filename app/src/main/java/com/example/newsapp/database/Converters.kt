package com.example.newsapp.database

import androidx.room.TypeConverter
import com.example.newsapp.networking.Source
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun toString(source: Source): String {
        return Gson().toJson(source)
    }

    @TypeConverter
    fun fromString(stringJson: String): Source {
        return Gson().fromJson(stringJson, Source::class.java)
    }
}