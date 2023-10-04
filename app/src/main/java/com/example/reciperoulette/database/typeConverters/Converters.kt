package com.example.reciperoulette.database.typeConverters

import androidx.room.TypeConverter
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun stringToTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun timeToString(date: LocalDateTime?): String? {
        return date?.toString()
    }
}