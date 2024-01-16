package com.ronazfarahmand.reciperoulette.presentation.screens.ingredientScreen.userActions

data class Filter(
    val vegetarian: Boolean = false,
    val pescatarian: Boolean = false,
    val nutFree: Boolean = false,
    val dairyFree: Boolean = false
)
