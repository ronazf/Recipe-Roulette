package com.ronazfarahmand.reciperoulette.presentation.components.dropdown.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.ronazfarahmand.reciperoulette.R
import com.ronazfarahmand.reciperoulette.presentation.GeneralConstants
import com.ronazfarahmand.reciperoulette.presentation.components.dropdown.DropdownConstants

@Composable
fun Dropdown(
    modifier: Modifier,
    name: String,
    openStacked: Boolean,
    forceClose: Boolean = false,
    color: Color,
    shape: Shape,
    height: Dp = GeneralConstants.ITEM_SIZE,
    onDismiss: () -> Unit = {},
    content: @Composable (onSelect: () -> Unit) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val vector = if (isExpanded) R.drawable.expand_less else R.drawable.expand_more
    val vectorDescription = if (isExpanded) R.string.expand_less else R.string.expand_more

    if (forceClose) {
        onDismiss()
        isExpanded = false
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .shadow(
                    elevation = GeneralConstants.DROP_SHADOW_ELEVATION,
                    shape = shape
                )
                .clip(shape = shape)
                .background(color)
                .clickable {
                    isExpanded = !isExpanded
                }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart)
                    .padding(start = GeneralConstants.IMAGE_TEXT_PADDING),
                text = name,
                fontSize = GeneralConstants.TEXT_FONT_SIZE,
                fontFamily = GeneralConstants.FONT_FAMILY
            )
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(horizontal = GeneralConstants.IMAGE_TEXT_PADDING),
                painter = painterResource(id = vector),
                contentDescription = stringResource(id = vectorDescription)
            )
            if (openStacked) {
                return@Box
            }
            if (isExpanded) {
                MaterialTheme(
                    shapes = MaterialTheme.shapes.copy(
                        extraSmall = RoundedCornerShape(DropdownConstants.CORNER_ROUNDING)
                    )
                ) {
                    DropdownMenu(
                        modifier = modifier
                            .align(Alignment.Center)
                            .heightIn(max = DropdownConstants.MAX_DROPDOWN_HEIGHT)
                            .background(color = colorResource(id = R.color.white)),
                        expanded = isExpanded,
                        onDismissRequest = {
                            onDismiss()
                            isExpanded = false
                        }
                    ) {
                        content {
                            onDismiss()
                            isExpanded = false
                        }
                    }
                }
            }
        }
    }
    if ((openStacked) && isExpanded) {
        content {
            onDismiss()
            isExpanded = false
        }
    }
}

@Composable
fun LayeredDropdown(
    modifier: Modifier,
    name: String,
    shape: Shape = RectangleShape,
    openStacked: Boolean = true,
    content: @Composable () -> Unit
) {
    Dropdown(
        modifier = modifier.fillMaxWidth(),
        name = name,
        openStacked = openStacked,
        color = colorResource(id = R.color.white),
        shape = shape,
        content = {
            content()
        }
    )
}

@Composable
fun InnerDropdown(
    modifier: Modifier,
    name: String,
    selectedItems: List<String>,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit,
    onRemove: (String) -> Unit
) {
    DropDownItem(
        modifier = modifier.fillMaxWidth(),
        name = name,
        selectedItems = selectedItems,
        onSelect = onSelect,
        closeDropDown = { onDismiss() },
        onRemove = onRemove,
        duplicateWarningResource = R.string.ingredient_already_selected
    )
}
