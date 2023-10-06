package com.example.reciperoulette.activities.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.reciperoulette.R
import com.example.reciperoulette.activities.recipeGeneratorActivity.RecipeGeneratorActivity

@Composable
fun SearchTab(
    modifier: Modifier,
    color: Color,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(RecipeGeneratorActivity.ITEM_SIZE)
                .shadow(
                    elevation = RecipeGeneratorActivity.DROP_SHADOW_ELEVATION
                )
                .clip(RectangleShape)
                .background(color)
        ) {
            CustomTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .clip(shape = RoundedCornerShape(RecipeGeneratorActivity.CORNER_ROUNDING)),
                value = "",
                onValueChange = onValueChange,
                placeHolder = stringResource(id = R.string.search_ingredient),
                leadingIcon = { onClick ->
                    Image(
                        modifier = Modifier
                            .padding(
                                start = RecipeGeneratorActivity.IMAGE_TEXT_PADDING,
                                end = RecipeGeneratorActivity.IMAGE_TEXT_PADDING
                            )
                            .clickable {
                                onClick()
                            },
                        painter = painterResource(R.drawable.search),
                        contentDescription = stringResource(id = R.string.search_description)
                    )
                },
                trailingIcon = { onClick ->
                    Image(
                        modifier = Modifier
                            .padding(
                                start = RecipeGeneratorActivity.IMAGE_TEXT_PADDING,
                                end = RecipeGeneratorActivity.IMAGE_TEXT_PADDING
                            )
                            .clickable {
                                onClick()
                            },
                        painter = painterResource(R.drawable.close),
                        contentDescription = stringResource(id = R.string.close_description)
                    )
                },
                singleLine = true
            )
        }
    }
}