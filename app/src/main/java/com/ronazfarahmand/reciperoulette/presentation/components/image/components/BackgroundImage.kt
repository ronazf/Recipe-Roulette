package com.ronazfarahmand.reciperoulette.presentation.components.image.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun BackgroundImage(
    modifier: Modifier,
    painter: Painter,
    description: String
) {
    Image(
        modifier = modifier,
        painter = painter,
        contentDescription = description
    )
}
