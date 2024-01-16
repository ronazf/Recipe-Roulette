package com.ronazfarahmand.reciperoulette.data.remote.api.response

import com.ronazfarahmand.reciperoulette.data.remote.api.request.Message
import com.google.gson.annotations.SerializedName

data class Choice(
    @SerializedName("index") val index: Int,
    @SerializedName("message") val message: Message,
    @SerializedName("finish_reason") val finishReason: String
)
