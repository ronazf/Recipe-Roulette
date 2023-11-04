package com.example.reciperoulette.dependencyInjection

import android.content.Context
import androidx.room.Room
import com.example.reciperoulette.database.ingredients.IngredientDatabase
import com.example.reciperoulette.database.recipes.RecipeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val ingredientDBName = "ingredient_database"
    private const val recipeDBName = "recipe_database"

    @Singleton
    @Provides
    fun provideIngredientDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        IngredientDatabase::class.java,
        ingredientDBName
    ).createFromAsset("database/ingredients.db").build()

    @Singleton
    @Provides
    fun provideRecipeDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        RecipeDatabase::class.java,
        recipeDBName
    ).createFromAsset("database/recipes.db").build()

    @Singleton
    @Provides
    fun provideIngredientDao(database: IngredientDatabase) = database.ingredientDao()

    @Singleton
    @Provides
    fun provideRecipeDao(database: RecipeDatabase) = database.recipeDao()
}
