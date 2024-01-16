package com.ronazfarahmand.reciperoulette.data.local.recipes

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ronazfarahmand.reciperoulette.data.local.recipes.dao.RecipeDao
import com.ronazfarahmand.reciperoulette.data.local.recipes.entities.Recipe
import com.ronazfarahmand.reciperoulette.data.local.typeConverters.Converters

@Database(
    entities = [Recipe::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}
