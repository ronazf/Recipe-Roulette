package com.example.reciperoulette.repositories.ingredientsRepository

import com.example.reciperoulette.activities.recipeGeneratorActivity.userActions.Filter
import com.example.reciperoulette.api.ApiService
import com.example.reciperoulette.api.request.Message
import com.example.reciperoulette.api.request.Request
import com.example.reciperoulette.api.response.Completion
import com.example.reciperoulette.api.response.Resource
import com.example.reciperoulette.api.response.ingredientValidation.Separators
import com.example.reciperoulette.database.ingredients.CategoryDetail
import com.example.reciperoulette.database.ingredients.IngredientDetail
import com.example.reciperoulette.database.ingredients.dao.IngredientsDao
import com.example.reciperoulette.database.ingredients.entities.Ingredient
import com.example.reciperoulette.dependencyInjection.IoDispatcher
import com.example.reciperoulette.repositories.apiCallHandling.ApiHandleRepository
import com.example.reciperoulette.repositories.apiCallHandling.ApiPostReqFunc
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class IngredientsRepositoryImpl @Inject constructor(
    private val ingredientsDao: IngredientsDao,
    private val recipeApi: ApiService,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : IngredientsRepository, ApiHandleRepository(ioDispatcher) {

    // TODO: Change string creation to be more consistent and easy to understand
    private val answer = "answer the following question "
    private val trueFalse = "with true or false only. "
    private val trueFalseNA = "with true or false or N/A (only answer with N/A if you answered" +
            "the previous question as false). "
    private val keyVal = "For key named (name in quotes) "
    private val categories = enumValues<CategoryDetail>().forEachIndexed { index, categoryDetail ->
        if (index != CategoryDetail.values().size - 1) {
            "${categoryDetail.strName} with id ${categoryDetail.id}, "
        } else {
            "${categoryDetail.strName} with id ${categoryDetail.id}."
        }
    }
    private val verifyIngredientReq = "Your task is to return a list of key value pairs where each " +
            "key is separated from the value by the following character (character in quotes) \"${Separators.KEY_VAL_SEPARATOR}\" " +
            "and each item in the list is separated by the following character (character in quotes) \"${Separators.PAIR_SEPARATOR}\"." +
            keyVal +  "\"${IngredientDetail.NAME.strName}\"" + answer + ". What is the name of the ingredient inputted?" +
            keyVal + "\"${IngredientDetail.IS_INGREDIENT.strName}\"" + answer + trueFalse +
            "Is the following item considered to be an edible cooking/baking ingredient? " +
            keyVal + "\"${IngredientDetail.IS_VEGETARIAN.strName}\"" + answer + trueFalseNA +
            "Is it vegetarian?" +
            keyVal + "\"${IngredientDetail.IS_PESCATARIAN.strName}\"" + answer + trueFalseNA +
            "Is it pescatarian?" +
            keyVal + "\"${IngredientDetail.IS_NUT_FREE.strName}\"" + answer + trueFalseNA +
            "Is it nut free?" +
            keyVal + "\"${IngredientDetail.IS_DAIRY_FREE.strName}\"" + answer + trueFalseNA +
            "Is it dairy free?" +
            "Additionally, if it is an ingredient, under what category is it labeled?" +
            keyVal + "\"${IngredientDetail.CATEGORY.strName}\" " +
            "choose from the following categories (the value you return for this key must " +
            "only consist of the id number corresponding " +
            "to the category you've chosen (i.e. category:1)): " + categories

    override fun getIngredients(filter: Filter, searchText: String): Flow<List<Ingredient>> {
        return ingredientsDao.getIngredients(
            isVegetarian = filter.vegetarian,
            isPescatarian = filter.pescatarian,
            isNutFree = filter.nutFree,
            isDairyFree = filter.dairyFree,
            searchText = searchText
        ).flowOn(ioDispatcher)
    }

    override suspend fun upsertIngredient(ingredient: Ingredient) {
        return withContext(ioDispatcher) {
            ingredientsDao.upsertIngredient(ingredient)
        }
    }

    override suspend fun deleteIngredient(ingredient: Ingredient) {
        return withContext(ioDispatcher) {
            ingredientsDao.deleteIngredient(ingredient)
        }
    }

    override suspend fun validateIngredient(ingredient: String): Resource<Completion> {
        val reqContent = "$verifyIngredientReq\n$ingredient"
        val request = Request(messages = arrayOf(Message(content = reqContent)))
        val apiPostReqFunc: ApiPostReqFunc<Completion> = { req -> recipeApi.getData(req) }
        return apiPostReq(apiPostReqFunc, request)
    }
}