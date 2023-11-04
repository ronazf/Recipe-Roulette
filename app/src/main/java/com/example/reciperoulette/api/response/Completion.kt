package com.example.reciperoulette.api.response

import com.google.gson.annotations.SerializedName

data class Completion(
    @SerializedName("id") val id: String,
    @SerializedName("object") val responseObject: String,
    @SerializedName("created") val created: Long,
    @SerializedName("model") val model: String,
    @SerializedName("choices") val choices: Array<Choice>,
    @SerializedName("usage") val usage: Usage
)

class InvalidResponseException(message: String) : Exception(message)
