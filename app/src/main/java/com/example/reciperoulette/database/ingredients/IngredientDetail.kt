package com.example.reciperoulette.database.ingredients

enum class IngredientDetail(val strName: String) {
    NAME("name"),
    IS_INGREDIENT("isIngredient"),
    IS_VEGETARIAN("isVegetarian"),
    IS_PESCATARIAN("isPescatarian"),
    IS_NUT_FREE("isNutFree"),
    IS_DAIRY_FREE("isDairyFree"),
    CATEGORY("category")
}
