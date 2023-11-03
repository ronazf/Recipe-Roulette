package com.example.reciperoulette.activities.components.filter.ingredientFilter

import com.example.reciperoulette.R

enum class FilterDetail(val strName: String, val color: Int) {
    IS_VEGETARIAN(strName = "Vegetarian", color = R.color.green),
    IS_PESCATARIAN(strName = "Pescatarian", color = R.color.red),
    IS_NUT_FREE(strName = "Nut Free", color = R.color.dark_brown),
    IS_DAIRY_FREE(strName = "Dairy Free", color = R.color.dark_blue),
}