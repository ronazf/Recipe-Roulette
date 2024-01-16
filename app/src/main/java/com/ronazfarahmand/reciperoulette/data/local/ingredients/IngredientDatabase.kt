package com.ronazfarahmand.reciperoulette.data.local.ingredients

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ronazfarahmand.reciperoulette.data.local.ingredients.dao.IngredientDao
import com.ronazfarahmand.reciperoulette.data.local.ingredients.entities.Category
import com.ronazfarahmand.reciperoulette.data.local.ingredients.entities.Ingredient
import com.ronazfarahmand.reciperoulette.data.local.typeConverters.Converters

@Database(
    entities = [Category::class, Ingredient::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class IngredientDatabase : RoomDatabase() {
    abstract fun ingredientDao(): IngredientDao
}
