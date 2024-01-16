package com.ronazfarahmand.reciperoulette.presentation.components.dialog

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ronazfarahmand.reciperoulette.R
import com.ronazfarahmand.reciperoulette.presentation.components.buttons.GenericTextButton

@Composable
fun EditableDialog(
    modifier: Modifier,
    editStatus: Boolean,
    onEdit: () -> Unit = {},
    onCancel: () -> Unit = {},
    onDismiss: () -> Unit,
    icon: @Composable (modifier: Modifier) -> Unit,
    title: @Composable (modifier: Modifier) -> Unit,
    content: @Composable (modifier: Modifier) -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Surface(
            modifier = modifier,
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = Modifier
                    .background(
                        color = AlertDialogDefaults.containerColor
                    )
                    .padding(dialogPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EditableButtonIconRow(
                    icon = icon,
                    editStatus = editStatus,
                    onEdit = onEdit,
                    onCancel = onCancel
                )
                title(modifier = Modifier.padding(titlePadding))
                content(modifier = Modifier.padding(contentPadding))
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
        }
    }
}

@Composable
internal fun EditableButtonIconRow(
    icon: @Composable (modifier: Modifier) -> Unit,
    editStatus: Boolean,
    onEdit: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val text = if (editStatus) R.string.save else R.string.edit
        val color = if (editStatus) R.color.green else R.color.dark_blue
        val cancelAlpha by animateFloatAsState(targetValue = if (editStatus) 1F else 0F, label = "")

        GenericTextButton(
            modifier = Modifier
                .wrapContentWidth(Alignment.Start),
            text = stringResource(id = text),
            color = colorResource(id = color)
        ) {
            onEdit()
        }
        icon(
            modifier = Modifier
                .wrapContentWidth(Alignment.Start)
        )
        GenericTextButton(
            modifier = Modifier
                .wrapContentWidth(Alignment.End)
                .graphicsLayer(alpha = cancelAlpha),
            text = stringResource(id = R.string.cancel),
            color = colorResource(id = R.color.red),
            enabled = editStatus
        ) {
            onCancel()
        }
    }
}

private val dialogPadding = PaddingValues(all = 24.dp)
private val titlePadding = PaddingValues(bottom = 16.dp)
private val contentPadding = PaddingValues(bottom = 24.dp)
