package com.example.reciperoulette.presentation.components.alertDialog.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.reciperoulette.R
import com.example.reciperoulette.presentation.GeneralConstants
import com.example.reciperoulette.presentation.components.alertDialog.AlertDialogConstants

@Composable
fun CancelableAlertDialog(
    modifier: Modifier,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    iconImage: Painter,
    iconDescription: String,
    title: String,
    questionText: String
) {
    AlertDialog(
        modifier = modifier,
        containerColor = colorResource(id = R.color.light_grey),
        shape = RoundedCornerShape(AlertDialogConstants.CORNER_ROUNDING),
        icon = {
            Icon(
                painter = iconImage,
                contentDescription = iconDescription
            )
        },
        title = {
            Text(
                text = title,
                fontSize = GeneralConstants.BUTTON_FONT_SIZE,
                fontFamily = GeneralConstants.FONT_FAMILY
            )
        },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = questionText,
                textAlign = TextAlign.Center
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
                    onConfirm()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.confirm),
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
