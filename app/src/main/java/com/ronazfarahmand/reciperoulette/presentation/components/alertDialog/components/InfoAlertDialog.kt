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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ronazfarahmand.reciperoulette.R
import com.ronazfarahmand.reciperoulette.presentation.GeneralConstants
import com.ronazfarahmand.reciperoulette.presentation.components.alertDialog.AlertDialogConstants
import com.ronazfarahmand.reciperoulette.presentation.components.buttons.GenericTextButton

@Composable
fun InfoAlertDialog(
    modifier: Modifier,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        containerColor = colorResource(id = R.color.light_grey),
        shape = RoundedCornerShape(AlertDialogConstants.CORNER_ROUNDING),
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.info),
                contentDescription = stringResource(id = R.string.info)
            )
        },
        title = {
            Text(
                text = stringResource(id = R.string.show_info),
                fontSize = GeneralConstants.BUTTON_FONT_SIZE,
                fontFamily = GeneralConstants.FONT_FAMILY
            )
        },
        text = {
            content()
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {},
        dismissButton = {
            GenericTextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.dismiss),
                color = colorResource(id = R.color.dark_blue)
            ) {
                onDismiss()
            }
        }
    )
}
