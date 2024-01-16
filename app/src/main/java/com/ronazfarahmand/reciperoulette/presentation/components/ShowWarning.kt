package com.ronazfarahmand.reciperoulette.presentation.components

import android.content.Context
import android.widget.Toast

fun ShowWarning(
    context: Context,
    message: String,
    length: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(context, message, length).show()
}
