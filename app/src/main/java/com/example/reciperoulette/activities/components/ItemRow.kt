package com.example.reciperoulette.activities.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.reciperoulette.R
import com.example.reciperoulette.activities.recipeGeneratorActivity.RecipeGeneratorActivity

@Composable
fun ItemRow(
    ingredient: String,
    removeIngredient: (ingredient: String) -> Unit
) {
    ConstraintLayout {
        val (itemBox, removeBtn) = createRefs()
        Box(
            modifier = Modifier
                .constrainAs(itemBox) {
                    top.linkTo(parent.top, margin = RecipeGeneratorActivity.ITEM_ROW_MARGIN)
                }
                .fillMaxWidth()
                .height(RecipeGeneratorActivity.ITEM_SIZE)
                .shadow(
                    RecipeGeneratorActivity.DROP_SHADOW_ELEVATION,
                    shape = RoundedCornerShape(RecipeGeneratorActivity.CORNER_ROUNDING)
                )
                .clip(RectangleShape)
                .clip(shape = RoundedCornerShape(RecipeGeneratorActivity.CORNER_ROUNDING))
                .background(colorResource(R.color.light_grey))
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart)
                    .padding(start = RecipeGeneratorActivity.IMAGE_TEXT_PADDING),
                text = ingredient,
                fontSize = RecipeGeneratorActivity.ITEM_TEXT_FONT_SIZE,
                fontFamily = FontFamily(Font(R.font.judson_regular))
            )
        }
        Button(
            modifier = Modifier
                .constrainAs(removeBtn) {
                    top.linkTo(itemBox.top)
                    bottom.linkTo(itemBox.bottom)
                    absoluteRight.linkTo(itemBox.absoluteRight)
                }
                .width(45.dp)
                .height(45.dp),
            onClick = { removeIngredient(ingredient) },
            shape = RoundedCornerShape(
                topEndPercent = RecipeGeneratorActivity.CORNER_ROUNDING,
                bottomEndPercent = RecipeGeneratorActivity.CORNER_ROUNDING
            ),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(R.color.red)
            )
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center),
                painter = painterResource(R.drawable.delete),
                contentDescription = stringResource(id = R.string.remove)
            )
        }
    }
}