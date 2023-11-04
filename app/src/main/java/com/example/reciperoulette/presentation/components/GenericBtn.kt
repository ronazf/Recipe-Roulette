package com.example.reciperoulette.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.example.reciperoulette.presentation.GeneralConstants
import com.example.reciperoulette.presentation.screens.homeScreen.HomeConstants

@Composable
fun GenericBtn(
    modifier: Modifier,
    text: String,
    textAlignment: TextAlign = TextAlign.Center,
    containerColor: Color,
    contentColor: Color,
    shape: Shape,
    fontSize: TextUnit,
    width: Float = HomeConstants.BUTTON_WIDTH,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    image: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        modifier = modifier
            .fillMaxWidth(width)
            .wrapContentSize(Alignment.Center),
        onClick = onClick,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        contentPadding = contentPadding,
        enabled = enabled
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            image?.let {
                image()
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = text,
                fontSize = fontSize,
                textAlign = textAlignment,
                fontFamily = GeneralConstants.FONT_FAMILY
            )
        }
    }
}
