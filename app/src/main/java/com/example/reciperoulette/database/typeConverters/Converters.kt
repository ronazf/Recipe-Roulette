package com.example.reciperoulette.database.typeConverters

import androidx.room.TypeConverter
import com.example.reciperoulette.database.recipes.RecipeStep
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    @TypeConverter
    fun jsonToStringList(json: String): List<String> {
        val type = object: TypeToken<List<String>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun stringListToJson(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun jsonToRecipeStepList(json: String): List<RecipeStep> {
        val type = object: TypeToken<List<RecipeStep>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun recipeStepListToJson(list: List<RecipeStep>): String {
        return Gson().toJson(list)
    }
}