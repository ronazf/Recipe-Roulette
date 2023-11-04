package com.example.reciperoulette.api.request

import com.example.reciperoulette.api.request.chatGPT.Role
import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("role") val role: String = Role.USER.type,
    @SerializedName("content") val content: String
)
