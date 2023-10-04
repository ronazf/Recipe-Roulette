package com.example.reciperoulette.activities.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.reciperoulette.R
import com.example.reciperoulette.activities.recipeGeneratorActivity.RecipeGeneratorActivity

@Composable
fun Dropdown(
    modifier: Modifier,
    name: String,
    itemLevel: Boolean,
    colorResource: Color,
    shape: Shape,
    content: @Composable (() -> Unit) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val vector = if (isExpanded) R.drawable.expand_less else R.drawable.expand_more
    val vectorDescription = if (isExpanded) R.string.expand_less else R.string.expand_more


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(RecipeGeneratorActivity.ITEM_SIZE)
                .shadow(
                    elevation = RecipeGeneratorActivity.DROP_SHADOW_ELEVATION,
                    shape = shape
                )
                .clip(RectangleShape)
                .clip(shape = shape)
                .background(colorResource)
                .clickable
                { isExpanded = !isExpanded },
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart)
                    .padding(start = RecipeGeneratorActivity.IMAGE_TEXT_PADDING),
                text = name,
                fontSize = RecipeGeneratorActivity.ITEM_TEXT_FONT_SIZE,
                fontFamily = FontFamily(Font(R.font.judson_regular))
            )
            if (!itemLevel) {
                DropdownMenu(
                    modifier = Modifier
                        .fillMaxWidth(RecipeGeneratorActivity.ROW_ITEM_WIDTH)
                        .align(Alignment.Center)
                        .heightIn(max = RecipeGeneratorActivity.MAX_DROPDOWN_HEIGHT),
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    content { isExpanded = false }
                }
            }
            Image(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = RecipeGeneratorActivity.IMAGE_TEXT_PADDING),
                painter = painterResource(id = vector),
                contentDescription = stringResource(id = vectorDescription)
            )
        }
    }
    if (itemLevel && isExpanded) {
        content { isExpanded = false }
    }
}