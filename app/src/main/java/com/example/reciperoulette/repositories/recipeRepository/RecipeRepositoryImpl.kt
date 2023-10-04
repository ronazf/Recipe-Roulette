package com.example.reciperoulette.repositories.recipeRepository

import com.example.reciperoulette.api.ApiService
import com.example.reciperoulette.api.request.Message
import com.example.reciperoulette.api.request.Request
import com.example.reciperoulette.api.response.Completion
import com.example.reciperoulette.api.response.Resource
import com.example.reciperoulette.dependencyInjection.IoDispatcher
import com.example.reciperoulette.repositories.apiCallHandling.ApiHandleRepository
import com.example.reciperoulette.repositories.apiCallHandling.ApiPostReqFunc
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val recipeApi: ApiService,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
): RecipeRepository, ApiHandleRepository(ioDispatcher) {

    private val recipeRequest = "The goal of this task is to give you a set of ingredients and have you return a step by step recipe. " +
            "Please only include the recipe in your response and no additional text. " +
            "Additionally, you may assume salt and pepper is available even if not included in the ingredient list. " +
            "Furthermore, you may assume each ingredient is available to any amount. " +
            "However, at the beginning, on a separate line, before giving the recipe information, " +
            "list the amount of each ingredient used (i.e. Veal: 4lbs) and the number of adults it will serve (i.e. Serves: 4 adults). " +
            "Below that also write the name of the recipe (i.e. Recipe: Chicken Fettuccine Alfredo)" +
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
}