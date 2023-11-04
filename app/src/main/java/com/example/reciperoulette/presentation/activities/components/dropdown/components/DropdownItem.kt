package com.example.reciperoulette.presentation.activities.components.dropdown.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.reciperoulette.R
import com.example.reciperoulette.presentation.activities.GeneralConstants
import com.example.reciperoulette.presentation.activities.components.ShowWarning
import com.example.reciperoulette.presentation.activities.components.alertDialog.components.CancelableAlertDialog

@Composable
fun DropDownItem(
    modifier: Modifier,
    name: String,
    selectedItems: List<String>,
    onSelect: (String) -> Unit,
    closeDropDown: () -> Unit,
    onRemove: (String) -> Unit,
    duplicateWarningResource: Int
) {
    val context = LocalContext.current
    val ingredientAlreadySelected = stringResource(duplicateWarningResource)

    var showAlert by remember { mutableStateOf(false) }

    if (showAlert) {
        ConfirmIngredientRemove(
            name = name,
            onCancel = { showAlert = false },
            onConfirm = {
                onRemove(name)
                showAlert = false
            }
        )
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(GeneralConstants.ITEM_SIZE)
                .shadow(GeneralConstants.DROP_SHADOW_ELEVATION)
                .background(colorResource(R.color.white))
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        modifier = Modifier
                            .padding(start = GeneralConstants.IMAGE_TEXT_PADDING),
                        text = name,
                        fontSize = GeneralConstants.TEXT_FONT_SIZE,
                        fontFamily = GeneralConstants.FONT_FAMILY
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (name in selectedItems) {
                        ShowWarning(
                            context,
                            ingredientAlreadySelected
                        )
                        return@DropdownMenuItem
                    }
                    onSelect(name)
                    closeDropDown()
                },
                trailingIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(start = GeneralConstants.IMAGE_TEXT_PADDING)
                            .clickable { showAlert = true },
                        painter = painterResource(id = R.drawable.remove),
                        contentDescription = stringResource(id = R.string.remove)
                    )
                }
            )
        }
    }
}

@Composable
fun ConfirmIngredientRemove(
    name: String,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    CancelableAlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onConfirm = onConfirm,
        onCancel = onCancel,
        iconImage = painterResource(id = R.drawable.remove_shopping_cart),
        iconDescription = stringResource(id = R.string.remove_shopping_cart),
        title = stringResource(id = R.string.remove_ingredient),
        questionText = stringResource(
            id = R.string.confirm_remove_ingredient
        ) + "\n\n" + name
    )
}
