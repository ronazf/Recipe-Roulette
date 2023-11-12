package com.example.reciperoulette.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.reciperoulette.R
import com.example.reciperoulette.presentation.GeneralConstants

@Composable
fun SearchTab(
    modifier: Modifier,
    color: Color,
    shape: Shape = RectangleShape,
    placeHolder: String,
    value: String = "",
    filterIcon: Painter,
    filterIconDescription: String,
    onFilter: () -> Unit,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(GeneralConstants.ITEM_SIZE)
                .shadow(
                    shape = shape,
                    elevation = GeneralConstants.DROP_SHADOW_ELEVATION
                )
                .clip(shape)
                .background(color)
        ) {
            CustomTextField(
                modifier = modifier
                    .height(GeneralConstants.ITEM_SIZE)
                    .clip(shape = RoundedCornerShape(GeneralConstants.CORNER_ROUNDING)),
                value = value,
                onValueChange = onValueChange,
                placeHolder = placeHolder,
                leadingIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(
                                horizontal = GeneralConstants.IMAGE_TEXT_PADDING
                            ),
                        painter = painterResource(R.drawable.search),
                        contentDescription = stringResource(id = R.string.search_description)
                    )
                },
                trailingIcon = { visibility, onClick ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (visibility) {
                            IconButton(onClick = { onClick() }) {
                                Icon(
                                    modifier = Modifier
                                        .padding(
                                            horizontal = GeneralConstants.IMAGE_TEXT_PADDING
                                        ),
                                    painter = painterResource(R.drawable.close),
                                    contentDescription = stringResource(id = R.string.close_description)
                                )
                            }
                            VerticalDivider(
                                modifier = Modifier
                                    .height(GeneralConstants.VERTICAL_DIVIDER_HEIGHT)
                                    .shadow(elevation = GeneralConstants.VERTICAL_DIVIDER_SHADOW),
                                color = colorResource(id = R.color.medium_grey)
                            )
                        }
                        IconButton(onClick = { onFilter() }) {
                            Icon(
                                modifier = Modifier
                                    .padding(
                                        horizontal = GeneralConstants.IMAGE_TEXT_PADDING
                                    ),
                                painter = filterIcon,
                                contentDescription = filterIconDescription
                            )
                        }
                    }
                },
                singleLine = true
            )
        }
    }
}
