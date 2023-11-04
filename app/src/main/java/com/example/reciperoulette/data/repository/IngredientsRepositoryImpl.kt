package com.example.reciperoulette.data.repository

import com.example.reciperoulette.presentation.activities.screens.ingredientScreen.userActions.Filter
import com.example.reciperoulette.data.remote.service.ApiService
import com.example.reciperoulette.data.remote.api.request.Message
import com.example.reciperoulette.data.remote.api.request.Request
import com.example.reciperoulette.data.remote.api.response.Completion
import com.example.reciperoulette.common.Resource
import com.example.reciperoulette.data.local.ingredients.dao.IngredientDao
import com.example.reciperoulette.data.local.ingredients.details.CategoryDetail
import com.example.reciperoulette.data.local.ingredients.details.IngredientDetail
import com.example.reciperoulette.data.local.ingredients.entities.Ingredient
import com.example.reciperoulette.domain.repository.IngredientsRepository
import com.example.reciperoulette.dependencyInjection.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class IngredientsRepositoryImpl @Inject constructor(
    private val ingredientDao: IngredientDao,
    private val ingredientApi: ApiService,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : IngredientsRepository, ApiHandleRepository(ioDispatcher) {

    // TODO: Change string creation to be more consistent and easy to understand
    private val answer = "answer the following question "
    private val trueFalse = "with true or false only. "
    private val trueFalseNA = "with true or false or N/A (only answer with N/A if you answered" +
        "the previous question as false). "
    private val keyVal = "For key named (name in quotes) "
    private val verifyIngredientReq = "Your task is to return a list of key value pairs. " +
        keyVal + "\"${IngredientDetail.NAME.strName}\" " + answer +
        ". What is the name of the ingredient inputted?" +
        keyVal + "\"${IngredientDetail.IS_INGREDIENT.strName}\" " + answer + trueFalse +
        "Is the following item considered to be an edible cooking/baking ingredient? " +
        keyVal + "\"${IngredientDetail.IS_VEGETARIAN.strName}\" " + answer + trueFalseNA +
        "Is it vegetarian?" +
        keyVal + "\"${IngredientDetail.IS_PESCATARIAN.strName}\" " + answer + trueFalseNA +
        "Is it pescatarian?" +
        keyVal + "\"${IngredientDetail.IS_NUT_FREE.strName}\" " + answer + trueFalseNA +
        "Is it nut free?" +
        keyVal + "\"${IngredientDetail.IS_DAIRY_FREE.strName}\" " + answer + trueFalseNA +
        "Is it dairy free?" +
        "Additionally, if it is an ingredient, under what category is it labeled?" +
        keyVal + "\"${IngredientDetail.CATEGORY.strName}\" " +
        "choose from the following categories (the value you return for this key must " +
        "only consist of the id number corresponding " +
        "to the category you've chosen (i.e. category:1)): " + getCategories() + ".\n" +
        "Please only return the key value pairs in JSON format."

    override fun getIngredients(filter: Filter, searchText: String): Flow<List<Ingredient>> {
        return ingredientDao.getIngredients(
            isVegetarian = filter.vegetarian,
            isPescatarian = filter.pescatarian,
            isNutFree = filter.nutFree,
            isDairyFree = filter.dairyFree,
            searchText = searchText
        ).flowOn(ioDispatcher)
    }

    override suspend fun upsertIngredient(ingredient: Ingredient) {
        withContext(ioDispatcher) {
            ingredientDao.upsertIngredient(ingredient)
        }
    }

    override suspend fun deleteIngredient(ingredient: Ingredient) {
        withContext(ioDispatcher) {
            ingredientDao.deleteIngredient(ingredient)
        }
    }

    override suspend fun validateIngredient(ingredient: String): Resource<Completion> {
        val reqContent =
            "$verifyIngredientReq\nitem name is (either ingredient or not): $ingredient"
        val request = Request(messages = arrayOf(Message(content = reqContent)))
        val apiPostReqFunc: ApiPostReqFunc<Completion> = { req -> ingredientApi.getData(req) }
        return apiPostReq(apiPostReqFunc, request)
    }

    private fun getCategories(): String {
        var categories = ""
        enumValues<CategoryDetail>().forEachIndexed { index, categoryDetail ->
            categories += if (index != CategoryDetail.values().size - 1) {
                "${categoryDetail.strName} with id ${categoryDetail.id}, "
            } else {
                "${categoryDetail.strName} with id ${categoryDetail.id}."
            }
        }
        return categories
    }
}
