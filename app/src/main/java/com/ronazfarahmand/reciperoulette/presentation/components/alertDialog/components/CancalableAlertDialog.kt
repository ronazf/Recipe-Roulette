package com.ronazfarahmand.reciperoulette.presentation.components.alertDialog.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ronazfarahmand.reciperoulette.R
import com.ronazfarahmand.reciperoulette.presentation.GeneralConstants
import com.ronazfarahmand.reciperoulette.presentation.components.alertDialog.AlertDialogConstants
import com.ronazfarahmand.reciperoulette.presentation.components.buttons.GenericTextButton

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
            GenericTextButton(
                modifier = Modifier
                    .fillMaxWidth(AlertDialogConstants.CANCELABLE_WIDTH)
                    .wrapContentWidth(Alignment.End),
                text = stringResource(id = R.string.confirm),
                color = colorResource(id = R.color.green)
            ) {
                onConfirm()
            }
        },
        dismissButton = {
            GenericTextButton(
                modifier = Modifier
                    .fillMaxWidth(AlertDialogConstants.CANCELABLE_WIDTH)
                    .wrapContentWidth(Alignment.Start),
                text = stringResource(id = R.string.cancel),
                color = colorResource(id = R.color.red)
            ) {
                onCancel()
            }
        }
    )
}
