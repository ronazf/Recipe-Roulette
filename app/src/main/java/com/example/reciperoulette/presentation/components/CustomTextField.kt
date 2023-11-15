package com.example.reciperoulette.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
    isError: Boolean = false,
    error: String = "",
    borderStroke: BorderStroke? = null,
    leadingIcon: @Composable ((onClick: () -> Unit) -> Unit)? = null,
    trailingIcon: @Composable ((visibility: Boolean, onClick: () -> Unit) -> Unit)? = null
) {
    val textSelectionColors = TextSelectionColors(
        handleColor = colorResource(id = R.color.grey),
        backgroundColor = colorResource(id = R.color.grey)
    )
    val scrollState = rememberScrollState()

    CompositionLocalProvider(value = LocalTextSelectionColors provides textSelectionColors) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Bottom
        ) {
            BasicTextField(
                modifier = Modifier
                    .heightIn(max = GeneralConstants.MAX_TEXT_FIELD_HEIGHT)
                    .clip(
                        shape = RoundedCornerShape(
                            GeneralConstants.TEXT_FIELD_CORNER_ROUNDING
                        )
                    )
                    .then(
                        if (isError) {
                            modifier.border(
                                border = BorderStroke(
                                    width = GeneralConstants.BORDER_WIDTH,
                                    color = colorResource(id = R.color.red)
                                ),
                                shape = RoundedCornerShape(
                                    GeneralConstants.TEXT_FIELD_CORNER_ROUNDING
                                )
                            )
                        } else {
                            modifier.then(
                                if (borderStroke != null) {
                                    Modifier.border(
                                        border = borderStroke,
                                        shape = RoundedCornerShape(
                                            GeneralConstants.TEXT_FIELD_CORNER_ROUNDING
                                        )
                                    )
                                } else {
                                    Modifier
                                }
                            )
                        }.wrapContentSize()
                    ),
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
                        modifier = Modifier
                            .fillMaxWidth(),
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
                                        modifier = Modifier
                                            .heightIn(max = GeneralConstants.MAX_TEXT_FIELD_HEIGHT)
                                            .verticalScroll(scrollState),
                                        text = placeHolder,
                                        style = LocalTextStyle.current.copy(
                                            fontSize = GeneralConstants.TEXT_FONT_SIZE,
                                            fontFamily = GeneralConstants.FONT_FAMILY
                                        )
                                    )
                                }
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            if (isError) {
                                Icon(
                                    modifier = Modifier.padding(
                                        horizontal = GeneralConstants.IMAGE_TEXT_PADDING
                                    ),
                                    painter = painterResource(id = R.drawable.info),
                                    contentDescription = stringResource(id = R.string.info),
                                    tint = colorResource(id = R.color.red)
                                )
                            }
                            trailingIcon?.let {
                                trailingIcon(value.isNotEmpty()) {
                                    onValueChange("")
                                }
                            }
                        }
                    }
                }
            )
            if (isError) {
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = GeneralConstants.IMAGE_TEXT_PADDING
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = error,
                        style = LocalTextStyle.current.copy(
                            fontSize = GeneralConstants.TEXT_FONT_SIZE,
                            fontFamily = GeneralConstants.FONT_FAMILY,
                            color = colorResource(id = R.color.red)
                        )
                    )
                }
            }
        }
    }
}
