package com.example.reciperoulette.activities.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.reciperoulette.R
import com.example.reciperoulette.activities.recipeGeneratorActivity.RecipeGeneratorActivity

@Composable
fun DropDownItem(
    modifier: Modifier,
    name: String,
    selectedItems: List<String>,
    onSelect: (String) -> Unit,
    closeDropDown: () -> Unit,
    duplicateWarningResource: Int
) {
    val context = LocalContext.current
    val ingredientAlreadySelected = stringResource(duplicateWarningResource)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(RecipeGeneratorActivity.ITEM_SIZE)
                .shadow(RecipeGeneratorActivity.DROP_SHADOW_ELEVATION)
                .background(colorResource(R.color.white))
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart)
                    .padding(start = RecipeGeneratorActivity.IMAGE_TEXT_PADDING),
                text = name,
                fontSize = RecipeGeneratorActivity.ITEM_TEXT_FONT_SIZE,
                fontFamily = FontFamily(Font(R.font.judson_regular))
            )
            DropdownMenuItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (name in selectedItems) {
                        showWarning(
                            context,
                            ingredientAlreadySelected,
                            Toast.LENGTH_SHORT
                        )
                        return@DropdownMenuItem
                    }
                    onSelect(name)
                    closeDropDown()
                }
            ) {}
        }
    }
}