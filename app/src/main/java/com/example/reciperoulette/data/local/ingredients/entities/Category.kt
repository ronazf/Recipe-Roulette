package com.example.reciperoulette.data.local.ingredients.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    val categoryId: Long = 0L,
    @ColumnInfo(name = "category_name")
    val categoryName: String
)
