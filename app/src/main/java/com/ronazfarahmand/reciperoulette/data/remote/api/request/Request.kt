package com.ronazfarahmand.reciperoulette.data.remote.api.request

import com.google.gson.annotations.SerializedName

data class Request(
    @SerializedName("model") val model: String = "gpt-3.5-turbo-16k",
    @SerializedName("messages") val messages: Array<Message>,
    @SerializedName("max_tokens") val maxTokens: Int? = 1000
)
