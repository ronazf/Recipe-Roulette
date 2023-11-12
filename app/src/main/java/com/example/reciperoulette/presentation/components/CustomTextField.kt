package com.example.reciperoulette.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import com.example.reciperoulette.R
import com.example.reciperoulette.presentation.GeneralConstants

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    placeHolder: String = "",
    value: String = "",
    onValueChange: (String) -> Unit,
    singleLine: Boolean = false,
    leadingIcon: @Composable ((onClick: () -> Unit) -> Unit)? = null,
    trailingIcon: @Composable ((visibility: Boolean, onClick: () -> Unit) -> Unit)? = null
) {
    val textSelectionColors = TextSelectionColors(
        handleColor = colorResource(id = R.color.grey),
        backgroundColor = colorResource(id = R.color.grey)
    )

    CompositionLocalProvider(value = LocalTextSelectionColors provides textSelectionColors) {
        BasicTextField(
            modifier = modifier,
            textStyle = TextStyle.Default.copy(
                fontSize = GeneralConstants.TEXT_FONT_SIZE,
                fontFamily = GeneralConstants.FONT_FAMILY
            ),
            singleLine = singleLine,
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            cursorBrush = SolidColor(colorResource(id = R.color.black)),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        leadingIcon?.let { leadingIcon {} }
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(GeneralConstants.TEXT_FIELD_PADDING)
                        ) {
                            innerTextField()
                            if (value.isBlank()) {
                                Text(
                                    text = placeHolder,
                                    style = LocalTextStyle.current.copy(
                                        fontSize = GeneralConstants.TEXT_FONT_SIZE,
                                        fontFamily = GeneralConstants.FONT_FAMILY
                                    )
                                )
                            }
                        }
                    }
                    trailingIcon?.let {
                        trailingIcon(value.isNotEmpty()) {
                            onValueChange("")
                        }
                    }
                }
            }
        )
    }
}
