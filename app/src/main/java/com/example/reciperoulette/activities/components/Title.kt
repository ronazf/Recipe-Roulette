package com.example.reciperoulette.activities.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import com.example.reciperoulette.R
import com.example.reciperoulette.activities.homeActivity.HomeActivity

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
        fontSize = HomeActivity.TITLE_FONT_SIZE,
        textAlign = TextAlign.Center,
        fontFamily = FontFamily(Font(R.font.nanummyeongjo_regular))
    )
}