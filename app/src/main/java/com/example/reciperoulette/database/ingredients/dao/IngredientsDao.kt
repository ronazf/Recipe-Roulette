package com.example.reciperoulette.database.ingredients.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.reciperoulette.database.ingredients.entities.Ingredient
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientsDao {

    @Query("SELECT ingredients.* " +
            "FROM ingredients " +
            "JOIN categories ON ingredients.category_id = categories.category_id " +
            "WHERE (NOT :isVegetarian OR ingredients.is_vegetarian = :isVegetarian) AND " +
            "(NOT :isPescatarian OR ingredients.is_pescatarian = :isPescatarian) AND " +
            "(NOT :isNutFree OR ingredients.is_nut_free = :isNutFree) AND " +
            "(NOT :isDairyFree OR ingredients.is_dairy_free = :isDairyFree) " +
            "ORDER BY ingredients.category_id ASC, ingredients.ingredient_name ASC")
    fun getIngredients(
        isVegetarian: Boolean,
        isPescatarian: Boolean,
        isNutFree: Boolean,
        isDairyFree: Boolean
    ): Flow<List<Ingredient>>

    @Upsert
    suspend fun upsertIngredient(ingredient: Ingredient)

    @Delete
    suspend fun deleteIngredient(ingredient: Ingredient)
}