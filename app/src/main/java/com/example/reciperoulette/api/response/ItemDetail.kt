package com.example.reciperoulette.api.response

data class ItemDetail(
    val isIngredient: Boolean = false,
    val vegetarian: Boolean = false,
    val pescatarian: Boolean = false,
    val nutFree: Boolean = false,
    val dairyFree: Boolean = false
)
