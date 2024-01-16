package com.ronazfarahmand.reciperoulette.data.local.recipes

data class RecipeIngredient(
    val id: Long,
    val ingredient: String
) {
    constructor(ingredient: String) : this(id = generateId(), ingredient = ingredient)

    companion object {
        private var idCounter: Long = 0L

        private fun generateId(): Long {
            return idCounter++
        }
    }
}
