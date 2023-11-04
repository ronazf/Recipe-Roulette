package com.example.reciperoulette.presentation.activities.components.alertDialog.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.reciperoulette.R
import com.example.reciperoulette.presentation.activities.GeneralConstants
import com.example.reciperoulette.presentation.activities.components.CustomTextField
import com.example.reciperoulette.presentation.activities.components.alertDialog.AlertDialogConstants

@Composable
fun InputAlertDialog(
    modifier: Modifier,
    inputText: String,
    onVerify: (String) -> Unit,
    onCancel: () -> Unit
) {
    var inputIngredient by remember { mutableStateOf(inputText) }

    AlertDialog(
        modifier = modifier,
        containerColor = colorResource(id = R.color.light_grey),
        shape = RoundedCornerShape(AlertDialogConstants.CORNER_ROUNDING),
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.add_shopping_cart),
                contentDescription = stringResource(id = R.string.add_shopping_cart)
            )
        },
        title = {
            Text(
                text = stringResource(id = R.string.add_ingredient_dialog),
                fontSize = GeneralConstants.BUTTON_FONT_SIZE,
                fontFamily = GeneralConstants.FONT_FAMILY
            )
        },
        text = {
            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(GeneralConstants.ITEM_SIZE)
                    .clip(shape = RoundedCornerShape(AlertDialogConstants.CORNER_ROUNDING))
                    .background(color = colorResource(id = R.color.light_grey))
                    .border(
                        border = BorderStroke(
                            width = AlertDialogConstants.TEXT_FIELD_BOARDER_WIDTH,
                            color = colorResource(id = R.color.grey)
                        ),
                        shape = RoundedCornerShape(GeneralConstants.CORNER_ROUNDING)
                    ),
                value = inputIngredient,
                onValueChange = { inputIngredient = it },
                placeHolder = stringResource(id = R.string.ingredient_name)
            )
        },
        onDismissRequest = {
            onCancel()
        },
        confirmButton = {
            TextButton(
                modifier = Modifier
                    .fillMaxWidth(AlertDialogConstants.CANCELABLE_WIDTH)
                    .wrapContentWidth(Alignment.End),
                onClick = {
                    onVerify(inputIngredient.trim())
                },
                enabled = inputIngredient.isNotEmpty()
            ) {
                Text(
                    text = stringResource(id = R.string.verify_ingredient),
                    color = colorResource(id = R.color.green),
                    fontSize = GeneralConstants.TEXT_FONT_SIZE,
                    fontFamily = GeneralConstants.FONT_FAMILY
                )
            }
        },
        dismissButton = {
            TextButton(
                modifier = Modifier
                    .fillMaxWidth(AlertDialogConstants.CANCELABLE_WIDTH)
                    .wrapContentWidth(Alignment.Start),
                onClick = {
                    onCancel()
                }

            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    color = colorResource(id = R.color.red),
                    fontSize = GeneralConstants.TEXT_FONT_SIZE,
                    fontFamily = GeneralConstants.FONT_FAMILY
                )
            }
        }
    )
}
