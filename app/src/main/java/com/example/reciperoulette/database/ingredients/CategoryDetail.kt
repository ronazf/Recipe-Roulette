package com.example.reciperoulette.database.ingredients

// TODO: test duplicate ids
enum class CategoryDetail(val strName: String, /* unique */ val id: Int) {
    MEAT_PROTEIN("Meats and Proteins", 1),
    FRUIT_VEGETABLE("Fruits and Vegetables", 2),
    GRAIN_CEREAL("Grains and Cereals", 3),
    DAIRY_ALTERNATIVE("Dairy and Alternatives", 4),
    HERB_SPICE("Herbs and Spices", 5),
    OIL_FAT("Oils and Fats", 6),
    NUT_SEED("Nuts and Seeds", 7),
    CONDIMENT_SAUCE("Condiments and Sauces", 8),
    SWEETENER("Sweeteners", 9),
    BAKING("Baking", 10),
    CANNED_JARRED("Canned and Jarred", 11)
}

class InvalidCategoryException(message: String): Exception(message)