package com.example.reciperoulette.presentation.activities.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.reciperoulette.presentation.activities.GeneralConstants

@Composable
fun Title(
    modifier: Modifier,
    title: String
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center),
        text = title,
        fontSize = GeneralConstants.TITLE_FONT_SIZE,
        textAlign = TextAlign.Center,
        fontFamily = GeneralConstants.FONT_FAMILY
    )
}
