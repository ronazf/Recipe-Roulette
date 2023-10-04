package com.example.reciperoulette.use_case.ingredientsUC.apiUC.validate_ingredient

import com.example.reciperoulette.api.request.chatGPT.Role
import com.example.reciperoulette.api.response.Completion
import com.example.reciperoulette.api.response.InvalidResponseException
import com.example.reciperoulette.api.response.Resource
import com.example.reciperoulette.api.response.ingredientValidation.Separators
import com.example.reciperoulette.database.ingredients.IngredientDetail
import com.example.reciperoulette.database.ingredients.entities.Ingredient
import com.example.reciperoulette.database.ingredients.entities.InvalidIngredientException
import com.example.reciperoulette.repositories.ingredientsRepository.IngredientsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.jvm.Throws

class ValidateIngredientUC @Inject constructor(
    private val ingredientsRepository: IngredientsRepository
) {
    companion object {
        const val INVALID_INGREDIENT = "Invalid cooking ingredient."
        const val INVALID_RESPONSE = "Invalid server response."
    }

    @Throws(InvalidIngredientException::class, InvalidResponseException::class)
    operator fun invoke(ingredient: String): Flow<Resource<Ingredient>> = flow {
        emit(Resource.Loading())
        val result = ingredientsRepository.validateIngredient(ingredient)
        // TODO: the problem here is that nothing will be emitted if there something goes wrong
        //  and both data and message are null. Check for alternative or timeout but good enough for now
        if (result.data != null) {
            emit(Resource.Success(processResult(result.data)))
        }
        if (result.message != null) {
            emit(Resource.Error(message = result.message))
        }
    }

    @Throws(InvalidIngredientException::class, InvalidResponseException::class)
    private fun processResult(res: Completion): Ingredient {
        val response = res.choices
            .last {
                it.message.role == Role.ASSISTANT.type
            }.message.content
            .trim()
            .split(Separators.PAIR_SEPARATOR)
            .associate {
                val (key, value) = it.split(Separators.KEY_VAL_SEPARATOR)
                key.trim() to value.trim()
            }

        validIngredientCheck(response)
        return mapToIngredient(response)
    }

    @Throws(InvalidIngredientException::class, InvalidResponseException::class)
    private fun validIngredientCheck(response: Map<String, String>) {
        val isIngredient = response[IngredientDetail.IS_INGREDIENT.strName]
            ?.toBoolean() ?: throw InvalidResponseException(INVALID_RESPONSE)
        if (!isIngredient) {
            throw InvalidIngredientException(INVALID_INGREDIENT)
        }
    }

    @Throws(InvalidResponseException::class)
    private fun mapToIngredient(response: Map<String, String>): Ingredient {
        return Ingredient(
            ingredientName = response[IngredientDetail.NAME.strName]
                ?.lowercase()?.replaceFirstChar {
                    it.uppercase()
                } ?: throw InvalidResponseException(INVALID_RESPONSE),
            categoryId = response[IngredientDetail.CATEGORY.strName]
                ?.toLong() ?: throw InvalidResponseException(INVALID_RESPONSE),
            isVegetarian = response[IngredientDetail.IS_VEGETARIAN.strName]
                ?.lowercase()?.toBoolean() ?: throw InvalidResponseException(INVALID_RESPONSE),
            isPescatarian = response[IngredientDetail.IS_PESCATARIAN.strName]
                ?.lowercase()?.toBoolean() ?: throw InvalidResponseException(INVALID_RESPONSE),
            isNutFree = response[IngredientDetail.IS_NUT_FREE.strName]
                ?.lowercase()?.toBoolean() ?: throw InvalidResponseException(INVALID_RESPONSE),
            isDairyFree = response[IngredientDetail.IS_DAIRY_FREE.strName]
                ?.lowercase()?.toBoolean() ?: throw InvalidResponseException(INVALID_RESPONSE)
        )
    }
}