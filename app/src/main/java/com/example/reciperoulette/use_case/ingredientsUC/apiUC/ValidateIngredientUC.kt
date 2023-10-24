package com.example.reciperoulette.use_case.ingredientsUC.apiUC

import android.util.Log
import com.example.reciperoulette.api.request.chatGPT.Role
import com.example.reciperoulette.api.response.Completion
import com.example.reciperoulette.api.response.InvalidResponseException
import com.example.reciperoulette.api.response.Resource
import com.example.reciperoulette.database.ingredients.details.IngredientDetail
import com.example.reciperoulette.database.ingredients.entities.Ingredient
import com.example.reciperoulette.database.ingredients.entities.InvalidIngredientException
import com.example.reciperoulette.repositories.ingredientsRepository.IngredientsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject
import kotlin.jvm.Throws

class ValidateIngredientUC @Inject constructor(
    private val ingredientsRepository: IngredientsRepository
) {
    companion object {
        const val INVALID_INGREDIENT = "Invalid cooking ingredient."
        const val INVALID_RESPONSE = "Invalid server response."
        const val UNEXPECTED_ERROR = "An unexpected error has occurred."
        const val TAG = "ValidateIngredientUC"
    }

    operator fun invoke(ingredient: String): Flow<Resource<Ingredient>> = flow {
        emit(Resource.Loading())
        val result = ingredientsRepository.validateIngredient(ingredient)
        // TODO: the problem here is that nothing will be emitted if there something goes wrong
        //  and both data and message are null. Check for alternative or timeout but good enough for now
        if (result.data != null) {
            try {
                emit(Resource.Success(processResult(result.data)))
            } catch (e: InvalidIngredientException) {
                emit(Resource.Error(message = e.localizedMessage ?: INVALID_INGREDIENT))
            } catch (e: InvalidResponseException) {
                emit(Resource.Error(message = e.localizedMessage ?: INVALID_RESPONSE))
            } catch (e: Exception) {
                Log.e(TAG, e.localizedMessage ?: UNEXPECTED_ERROR)
                emit(Resource.Error(message = UNEXPECTED_ERROR))
            }
        }
        if (result.message != null) {
            emit(Resource.Error(message = result.message))
        }
    }

    @Throws(InvalidIngredientException::class, InvalidResponseException::class)
    private fun processResult(res: Completion): Ingredient {

        val response = getJsonObj(res.choices
            .last {
                it.message.role == Role.ASSISTANT.type
            }.message.content
        )

        validIngredientCheck(response)
        return mapToIngredient(response)
    }

    private fun getJsonObj(resContent: String): JSONObject {
        return try {
            JSONObject(resContent)
        } catch (e: JSONException) {
            JSONArray(resContent).getJSONObject(0)
        }
    }

    @Throws(InvalidIngredientException::class)
    private fun validIngredientCheck(response: JSONObject) {
        val isIngredient = response.getBoolean(IngredientDetail.IS_INGREDIENT.strName)
        if (!isIngredient) {
            throw InvalidIngredientException(INVALID_INGREDIENT)
        }
    }

    @Throws(InvalidResponseException::class)
    private fun mapToIngredient(response: JSONObject): Ingredient {
        return try {
            Ingredient(
                ingredientName = response.getString(IngredientDetail.NAME.strName)
                    .lowercase().replaceFirstChar {
                        it.uppercase()
                    },
                categoryId = response.getLong(IngredientDetail.CATEGORY.strName),
                isVegetarian = response.getBoolean(IngredientDetail.IS_VEGETARIAN.strName),
                isPescatarian = response.getBoolean(IngredientDetail.IS_PESCATARIAN.strName),
                isNutFree = response.getBoolean(IngredientDetail.IS_NUT_FREE.strName),
                isDairyFree = response.getBoolean(IngredientDetail.IS_DAIRY_FREE.strName)
            )
        } catch (e: JSONException) {
            throw InvalidResponseException(message = INVALID_RESPONSE)
        }
    }
}