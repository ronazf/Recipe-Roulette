package com.ronazfarahmand.reciperoulette.presentation.screens.ingredientScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ronazfarahmand.reciperoulette.R
import com.ronazfarahmand.reciperoulette.data.local.ingredients.details.CategoryDetail
import com.ronazfarahmand.reciperoulette.data.local.ingredients.entities.Ingredient
import com.ronazfarahmand.reciperoulette.presentation.GeneralConstants
import com.ronazfarahmand.reciperoulette.presentation.components.ItemRow
import com.ronazfarahmand.reciperoulette.presentation.components.SearchTab
import com.ronazfarahmand.reciperoulette.presentation.components.Title
import com.ronazfarahmand.reciperoulette.presentation.components.alertDialog.components.ErrorAlertDialog
import com.ronazfarahmand.reciperoulette.presentation.components.alertDialog.components.InputAlertDialog
import com.ronazfarahmand.reciperoulette.presentation.components.alertDialog.components.SuccessAlertDialog
import com.ronazfarahmand.reciperoulette.presentation.components.buttons.GenericButton
import com.ronazfarahmand.reciperoulette.presentation.components.dropdown.components.Dropdown
import com.ronazfarahmand.reciperoulette.presentation.components.dropdown.components.InnerDropdown
import com.ronazfarahmand.reciperoulette.presentation.components.dropdown.components.LayeredDropdown
import com.ronazfarahmand.reciperoulette.presentation.components.filter.ingredientFilter.components.FilterTab
import com.ronazfarahmand.reciperoulette.presentation.components.image.components.BackgroundImage
import com.ronazfarahmand.reciperoulette.presentation.screens.homeScreen.HomeConstants
import com.ronazfarahmand.reciperoulette.presentation.screens.ingredientScreen.userActions.Filter
import com.ronazfarahmand.reciperoulette.presentation.screens.ingredientScreen.userActions.IngredientEvent
import com.ronazfarahmand.reciperoulette.presentation.screens.ingredientScreen.userActions.IngredientState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun IngredientSelectionScreen(
    state: IngredientState,
    navigateToRecipe: (String) -> Unit,
    onIngredientEvent: (IngredientEvent) -> Unit,
    onLoad: (resource: Int, resourceDescription: String) -> Unit,
    onResult: () -> Unit
) {
    var forceCloseDropdown by remember { mutableStateOf(false) }
    var blurRadius by remember { mutableStateOf(GeneralConstants.UN_BLUR_RADIUS) }

    ConstraintLayout(
        modifier = Modifier
            .pointerInteropFilter { state.loading || state.verifyingIngredient }
            .blur(blurRadius)
    ) {
        val (title, ingredients, generateBtn, dropDown, backgroundImage) = createRefs()

        Title(
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, margin = IngredientConstants.DISPLAYED_MARGIN)
                absoluteLeft.linkTo(parent.absoluteLeft)
                absoluteRight.linkTo(parent.absoluteRight)
            },
            title = stringResource(id = R.string.ingredients)
        )

        AddDropdown(
            modifier = Modifier.constrainAs(dropDown) {
                top.linkTo(title.bottom, margin = IngredientConstants.DISPLAYED_MARGIN)
                absoluteLeft.linkTo(parent.absoluteLeft)
                absoluteRight.linkTo(parent.absoluteRight)
            },
            forceClose = forceCloseDropdown,
            onDismiss = { onIngredientEvent(IngredientEvent.ClearSearch) }
        ) { onDismiss ->
            AddSearchFilter(
                filter = state.filter,
                onIngredientEvent = onIngredientEvent
            )
            if (state.searchText.isEmpty()) {
                AddLayeredDropdown(
                    mappedIngredients = state.mappedIngredients,
                    selectedIngredients = state.selectedIngredients,
                    onIngredientEvent = onIngredientEvent,
                    onDismiss = onDismiss
                )
            } else {
                AddInnerDropdown(
                    modifier = Modifier,
                    ingredients = state.ingredients,
                    selectedIngredients = state.selectedIngredients,
                    onIngredientEvent = onIngredientEvent,
                    onDismiss = onDismiss
                )
            }
            AddCustomIngredientButton(
                modifier = Modifier.fillMaxWidth(),
                searchText = state.searchText,
                onIngredientEvent = onIngredientEvent
            ) {
                onIngredientEvent(
                    IngredientEvent.ClearSearch
                )
            }
        }

        BackgroundImage(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(backgroundImage) {
                    top.linkTo(dropDown.bottom)
                    bottom.linkTo(generateBtn.top)
                },
            painter = painterResource(id = R.drawable.arranging),
            description = stringResource(id = R.string.arrange)
        )

        IngredientList(
            modifier = Modifier.constrainAs(ingredients) {
                top.linkTo(dropDown.bottom, margin = IngredientConstants.DISPLAYED_MARGIN)
                absoluteLeft.linkTo(parent.absoluteLeft)
                absoluteRight.linkTo(parent.absoluteRight)
                bottom.linkTo(generateBtn.top, margin = IngredientConstants.DISPLAYED_MARGIN)
                height = Dimension.fillToConstraints
            },
            ingredients = state.selectedIngredients,
            removeIngredient = { name ->
                onIngredientEvent(
                    IngredientEvent.RemoveSelectedIngredient(name)
                )
            }
        )

        GenericButton(
            modifier = Modifier.constrainAs(generateBtn) {
                bottom.linkTo(parent.bottom, margin = IngredientConstants.CLICKABLE_MARGIN)
                absoluteLeft.linkTo(parent.absoluteLeft)
                absoluteRight.linkTo(parent.absoluteRight)
            },
            text = stringResource(id = R.string.generate_recipe),
            containerColor = colorResource(id = R.color.green),
            contentColor = colorResource(id = R.color.black),
            shape = RoundedCornerShape(HomeConstants.CORNER_ROUNDING),
            fontSize = HomeConstants.BUTTON_FONT_SIZE,
            onClick = {
                navigateToRecipe(state.selectedIngredients.joinToString())
            },
            enabled = state.selectedIngredients.isNotEmpty()
        )
    }

    if (state.loading || state.verifyingIngredient) {
        onLoad(
            R.drawable.vegetable_bag,
            stringResource(id = R.string.grocery_bag_animation)
        )
        blurRadius = GeneralConstants.BLUR_RADIUS
        forceCloseDropdown = true
    } else {
        onResult()
        blurRadius = GeneralConstants.UN_BLUR_RADIUS
        forceCloseDropdown = false
    }

    if (state.error.isNotEmpty()) {
        ErrorAlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismiss = { onIngredientEvent(IngredientEvent.ClearError) },
            errorText = state.error
        )
    }

    if (state.success.isNotEmpty() && !state.loading) {
        SuccessAlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismiss = { onIngredientEvent(IngredientEvent.ClearSuccess) },
            successText = state.success
        )
    }
}

@Composable
fun AddDropdown(
    modifier: Modifier,
    forceClose: Boolean,
    onDismiss: () -> Unit,
    content: @Composable (() -> Unit) -> Unit
) {
    Dropdown(
        modifier = modifier.fillMaxWidth(IngredientConstants.ROW_ITEM_WIDTH),
        name = stringResource(id = R.string.add_ingredient),
        openStacked = false,
        forceClose = forceClose,
        color = colorResource(id = R.color.green),
        shape = RoundedCornerShape(IngredientConstants.CORNER_ROUNDING),
        onDismiss = onDismiss,
        content = content
    )
}

@Composable
fun AddLayeredDropdown(
    mappedIngredients: Map<CategoryDetail, List<Ingredient>>,
    selectedIngredients: List<String>,
    onIngredientEvent: (IngredientEvent) -> Unit,
    onDismiss: () -> Unit
) {
    mappedIngredients.onEach { entry ->
        LayeredDropdown(
            modifier = Modifier.fillMaxWidth(),
            name = entry.key.strName
        ) {
            AddInnerDropdown(
                modifier = Modifier,
                ingredients = entry.value,
                selectedIngredients = selectedIngredients,
                onIngredientEvent = onIngredientEvent,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
fun AddInnerDropdown(
    modifier: Modifier,
    ingredients: List<Ingredient>,
    selectedIngredients: List<String>,
    onIngredientEvent: (IngredientEvent) -> Unit,
    onDismiss: () -> Unit
) {
    ingredients.forEach {
        InnerDropdown(
            modifier = modifier,
            name = it.ingredientName,
            selectedItems = selectedIngredients,
            onSelect = { ingredient: String ->
                onIngredientEvent(
                    IngredientEvent.SelectIngredient(ingredient)
                )
            },
            onDismiss = onDismiss,
            onRemove = { name ->
                onIngredientEvent(
                    IngredientEvent.RemoveIngredient(name)
                )
            }
        )
    }
}

@Composable
fun AddSearchFilter(
    filter: Filter,
    onIngredientEvent: (IngredientEvent) -> Unit
) {
    var showFilter by remember { mutableStateOf(false) }
    val filterIcon = if (showFilter) R.drawable.close_filter else R.drawable.filter
    val filterIconDescription = if (showFilter) R.string.close_filter else R.string.filter
    var searchText by remember { mutableStateOf("") }

    SearchTab(
        modifier = Modifier.fillMaxWidth(),
        color = colorResource(id = R.color.white),
        value = searchText,
        placeHolder = stringResource(id = R.string.search_ingredient),
        filterIcon = painterResource(id = filterIcon),
        filterIconDescription = stringResource(id = filterIconDescription),
        onFilter = { showFilter = !showFilter }
    ) { it: String ->
        searchText = it
        onIngredientEvent(
            IngredientEvent.SearchIngredient(it)
        )
    }
    if (showFilter) {
        FilterTab(
            modifier = Modifier.fillMaxWidth(),
            filter = filter,
            onIngredientEvent = onIngredientEvent
        )
    }
}

@Composable
fun AddCustomIngredientButton(
    modifier: Modifier,
    searchText: String,
    onIngredientEvent: (IngredientEvent) -> Unit,
    onConfirm: () -> Unit
) {
    var showAlert by remember { mutableStateOf(false) }

    GenericButton(
        modifier = Modifier
            .fillMaxSize()
            .height(IngredientConstants.ITEM_SIZE),
        width = IngredientConstants.ROW_ITEM_WIDTH,
        text = stringResource(id = R.string.add_custom_ingredient),
        textAlignment = TextAlign.Left,
        containerColor = colorResource(id = R.color.light_grey),
        contentColor = colorResource(id = R.color.black),
        shape = RectangleShape,
        fontSize = IngredientConstants.ITEM_TEXT_FONT_SIZE,
        contentPadding = PaddingValues(0.dp),
        onClick = { showAlert = true },
        image = {
            Image(
                modifier = Modifier.padding(horizontal = IngredientConstants.IMAGE_TEXT_PADDING),
                painter = painterResource(id = R.drawable.add),
                contentDescription = stringResource(id = R.string.add_icon)
            )
        }
    )
    if (showAlert) {
        InputAlertDialog(
            modifier = modifier,
            inputText = searchText,
            onVerify = { ingredientName ->
                onConfirm()
                onIngredientEvent(
                    IngredientEvent.AddIngredient(ingredientName)
                )
                showAlert = false
            },
            onCancel = { showAlert = false }
        )
    }
}

@Composable
fun IngredientList(
    modifier: Modifier,
    ingredients: List<String>,
    removeIngredient: (ingredient: String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(IngredientConstants.ROW_ITEM_WIDTH),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(IngredientConstants.ITEM_SPACING)
    ) {
        items(ingredients) { ingredient ->
            ItemRow(
                ingredient,
                removeIngredient
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    val selectedIngredients = listOf("Carrot", "Beef Broth", "Potatoes")
    val previewState = IngredientState(selectedIngredients = selectedIngredients)

    IngredientSelectionScreen(
        navigateToRecipe = {},
        state = previewState,
        onIngredientEvent = {},
        onLoad = { _, _ -> },
        onResult = {}
    )
}
