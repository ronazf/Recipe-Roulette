package com.ronazfarahmand.reciperoulette.data.local.typeConverters

import androidx.room.TypeConverter
import com.ronazfarahmand.reciperoulette.data.local.recipes.RecipeIngredient
import com.ronazfarahmand.reciperoulette.data.local.recipes.RecipeStep
import com.ronazfarahmand.reciperoulette.data.local.recipes.details.RecipeIngredientDetail
import com.ronazfarahmand.reciperoulette.data.local.recipes.details.RecipeStepDetail
import org.json.JSONObject
import java.time.LocalDateTime

class Converters {
    companion object {
        private const val SPLIT_SEQUENCE = "\", \""
        private const val REGEX_PATTERN = "[^A-Za-z0-9() ]"
        private const val FAHRENHEIT_STR = "u00b0"
        private const val FAHRENHEIT_CHAR = "\u2109"
        private const val ARRAY_PREFIX = "["
        private const val ARRAY_SUFFIX = "]"
        private const val ARRAY_STRING_CHAR = '"'
    }

    @TypeConverter
    fun stringToTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun timeToString(date: LocalDateTime?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun stringToIngredientList(str: String): List<RecipeIngredient> {
        return str.removeSurrounding(ARRAY_PREFIX, ARRAY_SUFFIX)
            .split(SPLIT_SEQUENCE)
            .map {
                try {
                    if (it.isBlank()) {
                        return emptyList()
                    }

                    val obj = JSONObject(it)
                    RecipeIngredient(
                        id = obj.getLong(RecipeIngredientDetail.ID.strName),
                        ingredient = obj.getString(RecipeIngredientDetail.INGREDIENT.strName)
                    )
                } catch (e: Exception) {
                    RecipeIngredient(
                        ingredient = removeSpecialCharacters(
                            it.trim(ARRAY_STRING_CHAR).trim()
                        )
                    )
                }
            }
    }

    @TypeConverter
    fun ingredientListToString(list: List<RecipeIngredient>): String {
        val obj = JSONObject()
        return list.joinToString(
            separator = SPLIT_SEQUENCE,
            prefix = ARRAY_PREFIX,
            postfix = ARRAY_SUFFIX
        ) {
            obj.put(RecipeIngredientDetail.ID.strName, it.id)
            obj.put(RecipeIngredientDetail.INGREDIENT.strName, it.ingredient)
            obj.toString()
        }
    }

    @TypeConverter
    fun stringToRecipeStepList(str: String): List<RecipeStep> {
        return str.removeSurrounding(ARRAY_PREFIX, ARRAY_SUFFIX)
            .split(SPLIT_SEQUENCE)
            .map {
                try {
                    if (it.isBlank()) {
                        return emptyList()
                    }

                    val obj = JSONObject(it)
                    RecipeStep(
                        instructions = obj.getString(RecipeStepDetail.INSTRUCTIONS.strName)
                            .replace(FAHRENHEIT_STR, FAHRENHEIT_CHAR),
                        minutes = if (obj.has(RecipeStepDetail.MINUTES.strName)) {
                            if (obj.isNull(RecipeStepDetail.MINUTES.strName)) {
                                null
                            } else {
                                obj.getInt(RecipeStepDetail.MINUTES.strName)
                            }
                        } else { null }
                    )
                } catch (e: Exception) {
                    RecipeStep(
                        instructions = removeSpecialCharacters(
                            it.trim(ARRAY_STRING_CHAR).trim()
                        ).replace(
                            FAHRENHEIT_STR,
                            FAHRENHEIT_CHAR
                        )
                    )
                }
            }
    }

    @TypeConverter
    fun recipeStepListToString(list: List<RecipeStep>): String {
        val obj = JSONObject()
        return list.joinToString(
            separator = SPLIT_SEQUENCE,
            prefix = ARRAY_PREFIX,
            postfix = ARRAY_SUFFIX
        ) {
            obj.put(RecipeStepDetail.INSTRUCTIONS.strName, it.instructions)
            obj.put(RecipeStepDetail.MINUTES.strName, it.minutes ?: JSONObject.NULL)
            obj.toString()
        }
    }

    private fun removeSpecialCharacters(input: String): String {
        val regex = Regex(REGEX_PATTERN)
        return input.replace(regex, "")
    }
}
