package com.example.reciperoulette.database.ingredients

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.reciperoulette.database.ingredients.dao.IngredientsDao
import com.example.reciperoulette.database.ingredients.entities.Category
import com.example.reciperoulette.database.ingredients.entities.Ingredient
import com.example.reciperoulette.database.typeConverters.Converters

@Database(
    entities = [Category::class, Ingredient::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class IngredientsDatabase: RoomDatabase() {
    abstract fun ingredientDao(): IngredientsDao
}