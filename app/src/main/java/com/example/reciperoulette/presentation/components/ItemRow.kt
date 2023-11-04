package com.example.reciperoulette.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.reciperoulette.R
import com.example.reciperoulette.presentation.GeneralConstants

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
                    top.linkTo(parent.top, margin = GeneralConstants.ITEM_ROW_MARGIN)
                }
                .fillMaxWidth()
                .height(GeneralConstants.ITEM_SIZE)
                .shadow(
                    GeneralConstants.DROP_SHADOW_ELEVATION,
                    shape = RoundedCornerShape(GeneralConstants.CORNER_ROUNDING)
                )
                .clip(RectangleShape)
                .clip(shape = RoundedCornerShape(GeneralConstants.CORNER_ROUNDING))
                .background(colorResource(R.color.light_grey))
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart)
                    .padding(start = GeneralConstants.IMAGE_TEXT_PADDING),
                maxLines = 1,
                text = ingredient,
                overflow = TextOverflow.Ellipsis,
                fontSize = GeneralConstants.TEXT_FONT_SIZE,
                fontFamily = GeneralConstants.FONT_FAMILY
            )
        }
        FloatingActionButton(
            modifier = Modifier
                .constrainAs(removeBtn) {
                    top.linkTo(itemBox.top)
                    bottom.linkTo(itemBox.bottom)
                    absoluteRight.linkTo(itemBox.absoluteRight)
                }
                .size(GeneralConstants.ITEM_SIZE),
            onClick = { removeIngredient(ingredient) },
            shape = RoundedCornerShape(
                topEndPercent = GeneralConstants.CORNER_ROUNDING,
                bottomEndPercent = GeneralConstants.CORNER_ROUNDING
            ),
            containerColor = colorResource(id = R.color.red)
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center),
                painter = painterResource(R.drawable.delete),
                contentDescription = stringResource(id = R.string.delete)
            )
        }
    }
}
