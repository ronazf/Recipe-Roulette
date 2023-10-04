package com.example.reciperoulette.activities.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import com.example.reciperoulette.R
import com.example.reciperoulette.activities.homeActivity.HomeActivity

@Composable
fun GenericBtn(
    modifier: Modifier,
    text: String,
    backgroundColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .width(HomeActivity.BUTTON_WIDTH)
            .height(HomeActivity.BUTTON_HEIGHT)
            .wrapContentSize(Alignment.Center),
        onClick = onClick,
        shape = RoundedCornerShape(HomeActivity.CORNER_ROUNDING),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        )
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center),
            text = text,
            fontSize = HomeActivity.BUTTON_FONT_SIZE,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.judson_regular))
        )
    }
}