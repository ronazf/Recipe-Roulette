package com.ronazfarahmand.reciperoulette.data.remote.service

import com.ronazfarahmand.reciperoulette.BuildConfig
import com.ronazfarahmand.reciperoulette.data.remote.api.request.Request
import com.ronazfarahmand.reciperoulette.data.remote.api.response.Completion
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// TODO: change to Ktor?
interface ApiService {
    @Headers("Authorization: Bearer " + BuildConfig.API_KEY)
    @POST("v1/chat/completions")
    suspend fun getData(@Body userData: Request): Response<Completion>
}
