package com.example.reciperoulette.data.local.recipes.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.reciperoulette.data.local.recipes.RecipeIngredient
import com.example.reciperoulette.data.local.recipes.RecipeStep

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "recipe_id")
    val recipeId: Long = 0L,
    @ColumnInfo(name = "recipe_name")
    val recipeName: String,
    @ColumnInfo(name = "ingredients")
    val ingredients: List<RecipeIngredient>,
    @ColumnInfo(name = "serves")
    val serves: Int? = null,
    @ColumnInfo(name = "steps")
    val steps: List<RecipeStep>,
    @ColumnInfo(name = "link")
    val link: String? = null,
    @ColumnInfo(name = "favourite")
    val favourite: Boolean = false
)

class InvalidRecipeException(message: String) : Exception(message)
