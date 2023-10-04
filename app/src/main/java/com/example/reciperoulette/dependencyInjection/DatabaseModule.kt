package com.example.reciperoulette.dependencyInjection

import android.content.Context
import androidx.room.Room
import com.example.reciperoulette.database.ingredients.IngredientsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val ingredientsDBName = "ingredients_database"

    @Singleton
    @Provides
    fun provideIngredientsDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        IngredientsDatabase::class.java,
        ingredientsDBName
    ).createFromAsset("database/ingredients.db").build()

    @Singleton
    @Provides
    fun provideIngredientsDao(database: IngredientsDatabase) = database.ingredientDao()
}