package com.example.reciperoulette.dependencyInjection

import com.example.reciperoulette.api.ApiService
import com.example.reciperoulette.database.ingredients.dao.IngredientsDao
import com.example.reciperoulette.repositories.ingredientsRepository.IngredientsRepository
import com.example.reciperoulette.repositories.ingredientsRepository.IngredientsRepositoryImpl
import com.example.reciperoulette.repositories.recipeRepository.RecipeRepository
import com.example.reciperoulette.repositories.recipeRepository.RecipeRepositoryImpl
import com.example.reciperoulette.use_case.ingredientsUC.IngredientsUseCases
import com.example.reciperoulette.use_case.ingredientsUC.apiUC.validate_ingredient.ValidateIngredientUC
import com.example.reciperoulette.use_case.ingredientsUC.databaseUC.delete_ingredient.DeleteIngredientUC
import com.example.reciperoulette.use_case.ingredientsUC.databaseUC.get_ingredients.GetIngredientsUC
import com.example.reciperoulette.use_case.ingredientsUC.databaseUC.upsert_ingredient.UpsertIngredientUC
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
        recipeApi: ApiService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): RecipeRepository {
        return RecipeRepositoryImpl(recipeApi, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideIngredientRepository(
        ingredientsDao: IngredientsDao,
        recipeApi: ApiService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): IngredientsRepository {
        return IngredientsRepositoryImpl(ingredientsDao, recipeApi, ioDispatcher)
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
}