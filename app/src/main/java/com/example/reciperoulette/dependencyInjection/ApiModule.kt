package com.example.reciperoulette.dependencyInjection

import com.example.reciperoulette.data.remote.service.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private const val baseURL = "https://api.openai.com"
    private const val connectionTimeout = 120L

    @Singleton
    @Provides
    fun provideOhHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(connectionTimeout, TimeUnit.SECONDS)
            .readTimeout(connectionTimeout, TimeUnit.SECONDS)
            .writeTimeout(connectionTimeout, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideApi(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }
}
