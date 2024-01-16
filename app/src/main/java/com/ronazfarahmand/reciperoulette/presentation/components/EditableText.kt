package com.ronazfarahmand.reciperoulette.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.text.isDigitsOnly
import com.ronazfarahmand.reciperoulette.R
import com.ronazfarahmand.reciperoulette.presentation.GeneralConstants
import com.ronazfarahmand.reciperoulette.presentation.screens.recipeScreen.RecipeConstants

@Composable
fun EditableText(
    modifier: Modifier,
    label: String? = null,
    text: String? = null,
    placeHolder: String? = null,
    isEditing: Boolean,
    numerical: Boolean = false,
    onValueChange: ((text: String) -> Unit)? = null,
    onRemove: (() -> Unit)? = null,
    nullable: Boolean = true,
    singleLine: Boolean = true,
    content: @Composable (() -> Unit)? = null
) {
    var isValid by remember { mutableStateOf(true) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        label?.let {
            Text(
                text = "$label:",
                fontSize = GeneralConstants.TEXT_FONT_SIZE,
                textAlign = TextAlign.Start,
                fontFamily = GeneralConstants.FONT_FAMILY
            )
        }
        var editText by remember { mutableStateOf(text) }

        if (isEditing &&
            (placeHolder != null || content != null)
        ) {
            content?.let {
                content()
            }

            placeHolder?.let {
                CustomTextField(
                    modifier = Modifier
                        .fillMaxWidth(RecipeConstants.ROW_ITEM_WIDTH)
                        .wrapContentSize()
                        .padding(
                            horizontal = GeneralConstants.IMAGE_TEXT_PADDING
                        ),
                    borderStroke = BorderStroke(
                        width = GeneralConstants.BORDER_WIDTH,
                        color = colorResource(id = R.color.grey)
                    ),
                    placeHolder = placeHolder,
                    value = editText ?: "",
                    onValueChange = {
                        if (
                            (
                                numerical && it.isDigitsOnly() &&
                                    it.length < GeneralConstants.MAX_NUMERICAL_SPACE
                                ) ||
                            !numerical
                        ) {
                            editText = it
                            isValid = !(it.isBlank() && !nullable)
                            onValueChange?.let { onValueChange ->
                                onValueChange(it)
                            }
                        }
                    },
                    singleLine = singleLine,
                    isError = !isValid,
                    error = stringResource(id = R.string.non_empty_field)
                )
                onRemove?.let { remove ->
                    IconButton(
                        modifier = Modifier
                            .sizeIn(
                                maxHeight = GeneralConstants.MAX_ICON_BUTTON_SIZE,
                                maxWidth = GeneralConstants.MAX_ICON_BUTTON_SIZE
                            ),
                        onClick = {
                            remove()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.remove),
                            contentDescription = stringResource(id = R.string.remove)
                        )
                    }
                }
            }
        } else {
            text?.let {
                Text(
                    modifier = Modifier
                        .padding(
                            start = GeneralConstants.IMAGE_TEXT_PADDING
                        ),
                    text = text,
                    fontSize = GeneralConstants.TEXT_FONT_SIZE,
                    textAlign = TextAlign.Start,
                    fontFamily = GeneralConstants.FONT_FAMILY,
                    maxLines = if (singleLine) 1 else Int.MAX_VALUE,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
