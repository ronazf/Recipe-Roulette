package com.example.reciperoulette.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.reciperoulette.R
import com.example.reciperoulette.presentation.GeneralConstants

@Composable
fun EditableText(
    modifier: Modifier,
    label: String? = null,
    text: String? = null,
    placeHolder: String? = null,
    isEditing: Boolean,
    numerical: Boolean = false,
    onValueChange: ((text: String) -> Unit)? = null,
    onRemove: (() -> Unit)? = null
) {
    val removableItemWidth = if (onRemove == null) 1F else 0.9F

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
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

        if (isEditing && placeHolder != null) {
            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth(removableItemWidth)
                    .padding(
                        horizontal = GeneralConstants.IMAGE_TEXT_PADDING
                    )
                    .border(
                        border = BorderStroke(
                            width = GeneralConstants.BORDER_WIDTH,
                            color = colorResource(id = R.color.grey)
                        ),
                        shape = RoundedCornerShape(GeneralConstants.CORNER_ROUNDING)
                    ),
                placeHolder = placeHolder,
                value = editText ?: "",
                onValueChange = {
                    if (
                        (numerical && it.isDigitsOnly()) ||
                        !numerical
                    ) {
                        editText = it
                    }
                    onValueChange?.let { onValueChange ->
                        onValueChange(it)
                    }
                },
                singleLine = true
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
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
