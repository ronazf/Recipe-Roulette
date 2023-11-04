package com.example.reciperoulette.data.local.ingredients.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "ingredients",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["category_id"],
            childColumns = ["category_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ingredient_id")
    val ingredientId: Long = 0L,
    @ColumnInfo(name = "ingredient_name")
    val ingredientName: String,
    @ColumnInfo(name = "category_id")
    val categoryId: Long,
    @ColumnInfo(name = "last_used_date")
    val lastUsedDate: LocalDateTime? = null,
    @ColumnInfo(name = "is_vegetarian")
    val isVegetarian: Boolean,
    @ColumnInfo(name = "is_nut_free")
    val isNutFree: Boolean,
    @ColumnInfo(name = "is_pescatarian")
    val isPescatarian: Boolean,
    @ColumnInfo(name = "is_dairy_free")
    val isDairyFree: Boolean
)

class InvalidIngredientException(message: String) : Exception(message)
