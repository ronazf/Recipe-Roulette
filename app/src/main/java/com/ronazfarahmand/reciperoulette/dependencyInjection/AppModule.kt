package com.ronazfarahmand.reciperoulette.dependencyInjection

import com.ronazfarahmand.reciperoulette.data.local.ingredients.dao.IngredientDao
import com.ronazfarahmand.reciperoulette.data.local.recipes.dao.RecipeDao
import com.ronazfarahmand.reciperoulette.data.remote.service.ApiService
import com.ronazfarahmand.reciperoulette.data.repository.IngredientsRepositoryImpl
import com.ronazfarahmand.reciperoulette.data.repository.RecipeRepositoryImpl
import com.ronazfarahmand.reciperoulette.domain.repository.IngredientsRepository
import com.ronazfarahmand.reciperoulette.domain.repository.RecipeRepository
import com.ronazfarahmand.reciperoulette.domain.useCase.ingredientsUC.IngredientsUseCases
import com.ronazfarahmand.reciperoulette.domain.useCase.ingredientsUC.apiUC.ValidateIngredientUC
import com.ronazfarahmand.reciperoulette.domain.useCase.ingredientsUC.databaseUC.deleteIngredient.DeleteIngredientUC
import com.ronazfarahmand.reciperoulette.domain.useCase.ingredientsUC.databaseUC.getIngredients.GetIngredientsUC
import com.ronazfarahmand.reciperoulette.domain.useCase.ingredientsUC.databaseUC.upsertIngredient.UpsertIngredientUC
import com.ronazfarahmand.reciperoulette.domain.useCase.recipesUC.RecipeLibraryUseCases
import com.ronazfarahmand.reciperoulette.domain.useCase.recipesUC.RecipesUseCases
import com.ronazfarahmand.reciperoulette.domain.useCase.recipesUC.apiUC.GetRecipeUC
import com.ronazfarahmand.reciperoulette.domain.useCase.recipesUC.databaseUC.deleteRecipe.DeleteRecipeUC
import com.ronazfarahmand.reciperoulette.domain.useCase.recipesUC.databaseUC.favouriteRecipe.SetRecipeFavouriteUC
import com.ronazfarahmand.reciperoulette.domain.useCase.recipesUC.databaseUC.getRecipe.GetRecipeByIdUC
import com.ronazfarahmand.reciperoulette.domain.useCase.recipesUC.databaseUC.getRecipe.GetRecipesUC
import com.ronazfarahmand.reciperoulette.domain.useCase.recipesUC.databaseUC.upsertRecipe.UpsertRecipeUC
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
