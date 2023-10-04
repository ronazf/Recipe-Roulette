package com.example.reciperoulette.api.request.chatGPT

enum class Role(val type: String) {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant"),
    FUNCTION("function")
}