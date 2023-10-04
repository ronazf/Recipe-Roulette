package com.example.reciperoulette.api.response

import com.example.reciperoulette.api.request.Message
import com.google.gson.annotations.SerializedName

data class Choice(
    @SerializedName("index") val index: Int,
    @SerializedName("message") val message: Message,
    @SerializedName("finish_reason") val finishReason: String
)