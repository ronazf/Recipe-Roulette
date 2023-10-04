package com.example.reciperoulette.activities.components

import android.content.Context
import android.widget.Toast

fun showWarning(
    context: Context,
    message: String,
    length: Int
) {
    Toast.makeText(context, message, length).show()
}