package com.example.reciperoulette.database.recipes.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.reciperoulette.database.recipes.entities.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Query(
        "SELECT recipes.* " +
            "FROM recipes " +
            "WHERE (NOT :favourite OR recipes.favourite = :favourite) AND " +
            "(NOT :generated OR recipes.link IS NULL) AND " +
            "recipe_name LIKE '%' || :searchText || '%'" +
            "ORDER BY " +
            "recipes.recipe_name ASC"
    )
    fun getRecipes(
        favourite: Boolean = false,
        generated: Boolean = false,
        searchText: String = ""
    ): Flow<List<Recipe>>

    @Query(
        "SELECT recipes.* " +
            "FROM recipes " +
            "WHERE recipes.recipe_id = :id"
    )
    fun getRecipeById(id: Long): Recipe

    @Query(
        "UPDATE recipes " +
            "SET favourite = NOT favourite " +
            "WHERE recipes.recipe_id = :id"
    )
    fun setRecipeFavourite(id: Long)

    @Upsert
    suspend fun upsertRecipe(recipe: Recipe)

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)
}
