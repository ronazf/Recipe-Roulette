package com.example.reciperoulette.activities.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.reciperoulette.R
import com.example.reciperoulette.activities.recipeGeneratorActivity.RecipeGeneratorActivity

@Composable
fun CustomTextField(
    modifier: Modifier,
    placeHolder: String = "",
    value: String = "",
    onValueChange: (String) -> Unit,
    singleLine: Boolean = false,
    leadingIcon: @Composable ((onClick: () -> Unit) -> Unit)? = null,
    trailingIcon: @Composable ((onClick: () -> Unit) -> Unit)? = null
) {
    var text by remember { mutableStateOf(value) }

    BasicTextField(
        modifier = modifier,
        textStyle = TextStyle.Default.copy(
            fontSize = RecipeGeneratorActivity.ITEM_TEXT_FONT_SIZE,
            fontFamily = FontFamily(Font(R.font.judson_regular))
        ),
        singleLine = singleLine,
        value = text,
        onValueChange = {
            text = it
            onValueChange.invoke(it)
        },
        cursorBrush = SolidColor(colorResource(id = R.color.black)),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    leadingIcon?.let { leadingIcon {} }
                    Box {
                        innerTextField()
                        if (text.isEmpty()) Text(
                            text = placeHolder,
                            style = LocalTextStyle.current.copy(
                                fontSize = RecipeGeneratorActivity.ITEM_TEXT_FONT_SIZE,
                                fontFamily = FontFamily(Font(R.font.judson_regular))
                            )
                        )
                    }
                }
                trailingIcon?.let {
                    if (text.isNotEmpty()) {
                        trailingIcon {
                            text = ""
                            onValueChange.invoke("")
                        }
                    }
                }
            }
        }
    )
}