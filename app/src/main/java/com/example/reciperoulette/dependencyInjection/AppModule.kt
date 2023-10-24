package com.example.reciperoulette.dependencyInjection

import com.example.reciperoulette.api.ApiService
import com.example.reciperoulette.database.ingredients.dao.IngredientDao
import com.example.reciperoulette.database.recipes.dao.RecipeDao
import com.example.reciperoulette.repositories.ingredientsRepository.IngredientsRepository
import com.example.reciperoulette.repositories.ingredientsRepository.IngredientsRepositoryImpl
import com.example.reciperoulette.repositories.recipeRepository.RecipeRepository
import com.example.reciperoulette.repositories.recipeRepository.RecipeRepositoryImpl
import com.example.reciperoulette.use_case.ingredientsUC.IngredientsUseCases
import com.example.reciperoulette.use_case.ingredientsUC.apiUC.ValidateIngredientUC
import com.example.reciperoulette.use_case.ingredientsUC.databaseUC.delete_ingredient.DeleteIngredientUC
import com.example.reciperoulette.use_case.ingredientsUC.databaseUC.get_ingredients.GetIngredientsUC
import com.example.reciperoulette.use_case.ingredientsUC.databaseUC.upsert_ingredient.UpsertIngredientUC
import com.example.reciperoulette.use_case.recipesUC.RecipesUseCases
import com.example.reciperoulette.use_case.recipesUC.apiUC.GetRecipeUC
import com.example.reciperoulette.use_case.recipesUC.databaseUC.delete_recipe.DeleteRecipeUC
import com.example.reciperoulette.use_case.recipesUC.databaseUC.get_recipes.GetRecipesUC
import com.example.reciperoulette.use_case.recipesUC.databaseUC.upsert_recipe.UpsertRecipeUC
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRecipeRepository(
        recipeDao: RecipeDao,
        recipeApi: ApiService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): RecipeRepository {
        return RecipeRepositoryImpl(recipeDao, recipeApi, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideIngredientRepository(
        ingredientDao: IngredientDao,
        ingredientApi: ApiService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): IngredientsRepository {
        return IngredientsRepositoryImpl(ingredientDao, ingredientApi, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideIngredientsUseCases(
        ingredientsRepository: IngredientsRepository
    ): IngredientsUseCases {
        return IngredientsUseCases (
            validateIngredient = ValidateIngredientUC(ingredientsRepository),
            deleteIngredient = DeleteIngredientUC(ingredientsRepository),
            getIngredients = GetIngredientsUC(ingredientsRepository),
            upsertIngredient = UpsertIngredientUC(ingredientsRepository)
        )
    }

    @Singleton
    @Provides
    fun provideRecipesUseCases(
        recipesRepository: RecipeRepository
    ): RecipesUseCases {
        return RecipesUseCases (
            getRecipes = GetRecipesUC(recipesRepository),
            getRecipe = GetRecipeUC(recipesRepository),
            upsertRecipe = UpsertRecipeUC(recipesRepository),
            deleteRecipe = DeleteRecipeUC(recipesRepository)
        )
    }
}