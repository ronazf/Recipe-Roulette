package com.example.reciperoulette.data.repository

import com.example.reciperoulette.common.Resource
import com.example.reciperoulette.data.local.recipes.dao.RecipeDao
import com.example.reciperoulette.data.local.recipes.entities.Recipe
import com.example.reciperoulette.data.remote.api.request.Message
import com.example.reciperoulette.data.remote.api.request.Request
import com.example.reciperoulette.data.remote.api.response.Completion
import com.example.reciperoulette.data.remote.service.ApiService
import com.example.reciperoulette.dependencyInjection.IoDispatcher
import com.example.reciperoulette.domain.repository.RecipeRepository
import com.example.reciperoulette.presentation.activities.screens.libraryScreen.userActions.RecipeFilter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao,
    private val recipeApi: ApiService,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : RecipeRepository, ApiHandleRepository(ioDispatcher) {

    private val recipeRequest = "The goal of this task is to give you a set of ingredients " +
        "and have you return a step by step recipe using only those ingredients in JSON format. " +
        "Return a json object with the following keys:\n" +
        "recipe - the value of this key should be the recipe name.\n" +
        "ingredients the value of this key is a json array with the " +
        "following keys for each json object:\n" +
        "{\n" +
        "ingredientName - the name of the ingredient exactly as inputted.\n" +
        "amount - amount of ingredient used.\n" +
        "}\n" +
        "serves - the number of adults the recipe is expected to serve.\n" +
        "steps - the value of this key is a json array with the following keys " +
        "for each json object inside the array:\n" +
        "{\n" +
        "step - number of current step\n" +
        "instructions - the instructions for this step\n" +
        "minutes - the number of minutes this step is expected to take\n" +
        "}\n" +
        "Please only include the recipe in your response and no additional text. " +
        "Additionally, don't use any other ingredients not listed below in your recipe" +
        "The ingredients are as follows:"

    override suspend fun getRecipe(ingredients: List<String>): Resource<Completion> {
        var reqContent = recipeRequest
        for (item in ingredients) {
            reqContent += "\n$item"
        }
        val request = Request(messages = arrayOf(Message(content = reqContent)))
        val apiPostReqFunc: ApiPostReqFunc<Completion> = { req -> recipeApi.getData(req) }
        return apiPostReq(apiPostReqFunc, request)
    }

    override suspend fun getRecipeById(id: Long): Recipe {
        return withContext(ioDispatcher) {
            recipeDao.getRecipeById(id)
        }
    }

    override suspend fun upsertRecipe(recipe: Recipe) {
        withContext(ioDispatcher) {
            recipeDao.upsertRecipe(recipe)
        }
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        withContext(ioDispatcher) {
            recipeDao.deleteRecipe(recipe)
        }
    }

    override suspend fun setRecipeFavourite(id: Long) {
        withContext(ioDispatcher) {
            recipeDao.setRecipeFavourite(id)
        }
    }

    override fun getRecipes(filter: RecipeFilter, searchText: String): Flow<List<Recipe>> {
        return recipeDao.getRecipes(
            favourite = filter.favourite,
            generated = filter.generated,
            searchText = searchText
        ).flowOn(ioDispatcher)
    }
}
