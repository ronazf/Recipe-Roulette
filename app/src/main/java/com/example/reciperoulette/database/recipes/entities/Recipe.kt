package com.example.reciperoulette.database.recipes.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.reciperoulette.database.recipes.RecipeStep

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "recipe_id")
    val recipeId: Long = 0L,
    @ColumnInfo(name = "title")
    val recipeName: String,
    @ColumnInfo(name = "ingredients")
    val ingredients: List<String>,
    @ColumnInfo(name = "serves")
    val serves: Int? = null,
    @ColumnInfo(name = "directions")
    val steps: List<RecipeStep>,
    @ColumnInfo(name = "link")
    val link: String? = null
)

class InvalidRecipeException(message: String): Exception(message)
