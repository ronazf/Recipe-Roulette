package com.example.reciperoulette.dependencyInjection

import com.example.reciperoulette.api.ApiService
import com.example.reciperoulette.database.ingredients.dao.IngredientDao
import com.example.reciperoulette.database.recipes.dao.RecipeDao
import com.example.reciperoulette.repositories.ingredientsRepository.IngredientsRepository
import com.example.reciperoulette.repositories.ingredientsRepository.IngredientsRepositoryImpl
import com.example.reciperoulette.repositories.recipeRepository.RecipeRepository
import com.example.reciperoulette.repositories.recipeRepository.RecipeRepositoryImpl
import com.example.reciperoulette.useCase.ingredientsUC.IngredientsUseCases
import com.example.reciperoulette.useCase.ingredientsUC.apiUC.ValidateIngredientUC
import com.example.reciperoulette.useCase.ingredientsUC.databaseUC.deleteIngredient.DeleteIngredientUC
import com.example.reciperoulette.useCase.ingredientsUC.databaseUC.getIngredients.GetIngredientsUC
import com.example.reciperoulette.useCase.ingredientsUC.databaseUC.upsertIngredient.UpsertIngredientUC
import com.example.reciperoulette.useCase.recipesUC.RecipeLibraryUseCases
import com.example.reciperoulette.useCase.recipesUC.RecipesUseCases
import com.example.reciperoulette.useCase.recipesUC.apiUC.GetRecipeUC
import com.example.reciperoulette.useCase.recipesUC.databaseUC.deleteRecipe.DeleteRecipeUC
import com.example.reciperoulette.useCase.recipesUC.databaseUC.favouriteRecipe.SetRecipeFavouriteUC
import com.example.reciperoulette.useCase.recipesUC.databaseUC.getRecipe.GetRecipeByIdUC
import com.example.reciperoulette.useCase.recipesUC.databaseUC.getRecipe.GetRecipesUC
import com.example.reciperoulette.useCase.recipesUC.databaseUC.upsertRecipe.UpsertRecipeUC
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
        return IngredientsUseCases(
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
        return RecipesUseCases(
            getRecipe = GetRecipeUC(recipesRepository),
            upsertRecipe = UpsertRecipeUC(recipesRepository),
            getRecipeById = GetRecipeByIdUC(recipesRepository)
        )
    }

    @Singleton
    @Provides
    fun provideLibraryUseCases(
        recipesRepository: RecipeRepository
    ): RecipeLibraryUseCases {
        return RecipeLibraryUseCases(
            getRecipes = GetRecipesUC(recipesRepository),
            getRecipe = GetRecipeUC(recipesRepository),
            deleteRecipe = DeleteRecipeUC(recipesRepository),
            setRecipeFavourite = SetRecipeFavouriteUC(recipesRepository)
        )
    }
}
