package com.example.reciperoulette.activities.components.alertDialog.components

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.reciperoulette.R
import com.example.reciperoulette.activities.components.alertDialog.AlertDialogConstants
import com.example.reciperoulette.activities.GeneralConstants

@Composable
fun ErrorAlertDialog(
    modifier: Modifier,
    onDismiss: () -> Unit,
    errorText: String
) {
    AlertDialog(
        modifier = modifier,
        containerColor = colorResource(id = R.color.light_grey),
        shape = RoundedCornerShape(AlertDialogConstants.CORNER_ROUNDING),
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.error),
                contentDescription = stringResource(id = R.string.error)
            )
        },
        title = {
            Text(
                text = stringResource(id = R.string.error_occured),
                fontSize = GeneralConstants.BUTTON_FONT_SIZE,
                fontFamily = GeneralConstants.FONT_FAMILY
            )
        },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = errorText,
                textAlign = TextAlign.Center
            )
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {},
        dismissButton = {
            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally),
                onClick = {
                    onDismiss()
                }

            ) {
                Text(
                    text = stringResource(id = R.string.dismiss),
                    color = colorResource(id = R.color.dark_blue),
                    fontSize = GeneralConstants.TEXT_FONT_SIZE,
                    fontFamily = GeneralConstants.FONT_FAMILY
                )
            }
        }
    )
}