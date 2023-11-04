package com.example.reciperoulette.database.recipes

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.reciperoulette.database.recipes.dao.RecipeDao
import com.example.reciperoulette.database.recipes.entities.Recipe
import com.example.reciperoulette.database.typeConverters.Converters

@Database(
    entities = [Recipe::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}
