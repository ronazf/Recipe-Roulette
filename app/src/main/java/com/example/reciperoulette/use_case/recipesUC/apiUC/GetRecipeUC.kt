package com.example.reciperoulette.use_case.recipesUC.apiUC

import android.util.Log
import com.example.reciperoulette.api.request.chatGPT.Role
import com.example.reciperoulette.api.response.Completion
import com.example.reciperoulette.api.response.InvalidResponseException
import com.example.reciperoulette.api.response.Resource
import com.example.reciperoulette.database.ingredients.entities.InvalidIngredientException
import com.example.reciperoulette.database.recipes.RecipeStep
import com.example.reciperoulette.database.recipes.details.IngredientNameDetail
import com.example.reciperoulette.database.recipes.details.RecipeDetail
import com.example.reciperoulette.database.recipes.details.RecipeStepDetail
import com.example.reciperoulette.database.recipes.entities.InvalidRecipeException
import com.example.reciperoulette.database.recipes.entities.Recipe
import com.example.reciperoulette.repositories.recipeRepository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject
import kotlin.jvm.Throws

class GetRecipeUC @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    companion object {
        const val INVALID_RECIPE = "Invalid recipe."
        const val INVALID_RESPONSE = "Invalid server response."
        const val UNEXPECTED_ERROR = "An unexpected error has occurred."
        const val TAG = "GetRecipeUC"
    }

    operator fun invoke(selectedIngredients: List<String>): Flow<Resource<Recipe>> = flow {
        emit(Resource.Loading())
        val result = recipeRepository.getRecipe(selectedIngredients)
        if (result.data != null) {
            try {
                emit(Resource.Success(processResult(result.data)))
            } catch (e: InvalidIngredientException) {
                emit(Resource.Error(message = e.localizedMessage ?: INVALID_RECIPE))
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

    @Throws(InvalidRecipeException::class, InvalidResponseException::class)
    private fun processResult(res: Completion): Recipe {
        val response = getJsonObj(res.choices
            .last {
                it.message.role == Role.ASSISTANT.type
            }.message.content
        )

        return mapToRecipe(response)
    }

    private fun getJsonObj(resContent: String): JSONObject {
        return try {
            JSONObject(resContent)
        } catch (e: JSONException) {
            JSONArray(resContent).getJSONObject(0)
        }
    }

    private fun mapToRecipe(response: JSONObject): Recipe {
        return try {
            Recipe(
                recipeName = response.getString(RecipeDetail.NAME.strName),
                ingredients = getIngredients(
                    response.getJSONArray(RecipeDetail.INGREDIENTS.strName)
                ),
                serves = response.getInt(RecipeDetail.SERVES.strName),
                steps = getSteps(
                    response.getJSONArray(RecipeDetail.STEPS.strName)
                )
            )
        } catch (e: JSONException) {
            throw InvalidResponseException(message = INVALID_RESPONSE)
        }
    }

    private fun getIngredients(array: JSONArray): List<String> {
        val ingredients = arrayListOf<String>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            ingredients.add(
                obj.getString(IngredientNameDetail.AMOUNT.strName) +
                " " +
                obj.getString(IngredientNameDetail.NAME.strName)
            )
        }
        return ingredients
    }

    private fun getSteps(array: JSONArray): List<RecipeStep> {
        val steps = arrayListOf<RecipeStep>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            steps.add(
                RecipeStep(
                    stepNumber = obj.getInt(RecipeStepDetail.STEP.strName),
                    instructions = obj.getString(RecipeStepDetail.INSTRUCTIONS.strName),
                    minutes = obj.getInt(RecipeStepDetail.MINUTES.strName)
                )
            )
        }
        return steps
    }
}