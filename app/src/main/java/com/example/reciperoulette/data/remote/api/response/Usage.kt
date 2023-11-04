package com.example.reciperoulette.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class Usage(
    @SerializedName("prompt_tokens") val promptTokens: Int,
    @SerializedName("completion_tokens") val completionTokens: Int,
    @SerializedName("total_tokens") val totalTokens: Int
)
