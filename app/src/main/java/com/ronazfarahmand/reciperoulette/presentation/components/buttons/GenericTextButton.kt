package com.ronazfarahmand.reciperoulette.presentation.components.buttons

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ronazfarahmand.reciperoulette.presentation.GeneralConstants

@Composable
fun GenericTextButton(
    modifier: Modifier,
    text: String,
    color: Color,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = {
            onClick()
        },
        enabled = enabled
    ) {
        Text(
            text = text,
            color = color,
            fontSize = GeneralConstants.TEXT_FONT_SIZE,
            fontFamily = GeneralConstants.FONT_FAMILY
        )
    }
}
