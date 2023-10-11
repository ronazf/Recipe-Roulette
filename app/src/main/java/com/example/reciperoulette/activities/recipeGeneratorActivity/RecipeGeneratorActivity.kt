package com.example.reciperoulette.activities.recipeGeneratorActivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.reciperoulette.R
import com.example.reciperoulette.activities.components.image.components.BackgroundImage
import com.example.reciperoulette.activities.components.alertDialog.components.InputAlertDialog
import com.example.reciperoulette.activities.components.dropdown.components.Dropdown
import com.example.reciperoulette.activities.components.alertDialog.components.ErrorAlertDialog
import com.example.reciperoulette.activities.components.GenericBtn
import com.example.reciperoulette.activities.components.dropdown.components.InnerDropdown
import com.example.reciperoulette.activities.components.ItemRow
import com.example.reciperoulette.activities.components.dropdown.components.LayeredDropdown
import com.example.reciperoulette.activities.components.Loading
import com.example.reciperoulette.activities.components.SearchTab
import com.example.reciperoulette.activities.components.alertDialog.components.SuccessAlertDialog
import com.example.reciperoulette.activities.components.Title
import com.example.reciperoulette.activities.components.filter.components.FilterTab
import com.example.reciperoulette.activities.homeActivity.HomeActivity
import com.example.reciperoulette.activities.recipeGeneratorActivity.userActions.Filter
import com.example.reciperoulette.activities.recipeGeneratorActivity.userActions.IngredientEvent
import com.example.reciperoulette.activities.recipeGeneratorActivity.userActions.IngredientState
import com.example.reciperoulette.database.ingredients.CategoryDetail
import com.example.reciperoulette.database.ingredients.entities.Ingredient
import com.example.reciperoulette.viewModels.recipeViewModel.RecipeViewModel
import com.example.reciperoulette.ui.theme.RecipeRuletteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeGeneratorActivity : ComponentActivity() {

    // TODO: check for companion similarities
    companion object {
        val ITEM_TEXT_FONT_SIZE = 15.sp
        val DISPLAYED_MARGIN = 25.dp
        val CLICKABLE_MARGIN = 50.dp
        val ITEM_SPACING = 16.dp
        val ITEM_SIZE = 45.dp
        val IMAGE_TEXT_PADDING = 10.dp
        val ITEM_ROW_MARGIN = 4.dp
        const val ROW_ITEM_WIDTH = 0.75F
        const val CORNER_ROUNDING = 35
    }

    private val recipeViewModel: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by recipeViewModel.state.collectAsState()

            RecipeRuletteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RenderScreen(
                        state,
                        recipeViewModel::onIngredientEvent
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RenderScreen(
    state: IngredientState,
    onIngredientEvent: (IngredientEvent) -> Unit
) {
    val context = LocalContext.current
    var forceCloseDropdown by remember { mutableStateOf(false) }

    ConstraintLayout(
        modifier = Modifier.pointerInteropFilter { state.loading || state.verifyingIngredient }
    ) {
        val (title, ingredients, generateBtn, dropDown, backgroundImage) = createRefs()

        Title(
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, margin = RecipeGeneratorActivity.DISPLAYED_MARGIN)
                absoluteLeft.linkTo(parent.absoluteLeft)
                absoluteRight.linkTo(parent.absoluteRight)
            },
            title = stringResource(id = R.string.ingredients)
        )

        AddDropdown(
            modifier = Modifier.constrainAs(dropDown) {
                top.linkTo(title.bottom, margin = RecipeGeneratorActivity.DISPLAYED_MARGIN)
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
                top.linkTo(dropDown.bottom, margin = RecipeGeneratorActivity.DISPLAYED_MARGIN)
                absoluteLeft.linkTo(parent.absoluteLeft)
                absoluteRight.linkTo(parent.absoluteRight)
                bottom.linkTo(generateBtn.top, margin = RecipeGeneratorActivity.DISPLAYED_MARGIN)
                height = Dimension.fillToConstraints
            },
            ingredients = state.selectedIngredients,
            removeIngredient = { name ->
                onIngredientEvent(
                    IngredientEvent.RemoveSelectedIngredient(name)
                )
            }
        )

        GenericBtn(
            modifier = Modifier.constrainAs(generateBtn) {
                bottom.linkTo(parent.bottom, margin = RecipeGeneratorActivity.CLICKABLE_MARGIN)
                absoluteLeft.linkTo(parent.absoluteLeft)
                absoluteRight.linkTo(parent.absoluteRight)
            },
            text = stringResource(id = R.string.generate_recipe),
            containerColor = colorResource(id = R.color.green),
            contentColor = colorResource(id = R.color.black),
            shape = RoundedCornerShape(HomeActivity.CORNER_ROUNDING),
            fontSize = HomeActivity.BUTTON_FONT_SIZE,
            onClick = {
                val intent = Intent(context, RecipeGeneratorActivity::class.java)
                context.startActivity(intent)
            }
        )
    }

    if (state.loading || state.verifyingIngredient) {
        forceCloseDropdown = true
        Loading(
            resource = R.drawable.vegetable_bag,
            resourceDescription = stringResource(id = R.string.grocery_bag_animation)
        )
    } else {
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
        modifier = modifier.fillMaxWidth(RecipeGeneratorActivity.ROW_ITEM_WIDTH),
        name = stringResource(id = R.string.add_ingredient),
        isLastLevel = false,
        forceClose = forceClose,
        color = colorResource(id = R.color.green),
        shape = RoundedCornerShape(RecipeGeneratorActivity.CORNER_ROUNDING),
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
            entry = entry
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

    SearchTab(
        modifier = Modifier.fillMaxWidth(),
        color = colorResource(id = R.color.white),
        filterIcon = painterResource(id = filterIcon),
        filterIconDescription = stringResource(id = filterIconDescription),
        onFilter = { showFilter = !showFilter }
    ) { it: String ->
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

    GenericBtn(
        modifier = Modifier
            .fillMaxSize()
            .height(RecipeGeneratorActivity.ITEM_SIZE),
        width = RecipeGeneratorActivity.ROW_ITEM_WIDTH,
        text = stringResource(id = R.string.add_custom_ingredient),
        textAlignment = TextAlign.Left,
        containerColor = colorResource(id = R.color.light_grey),
        contentColor = colorResource(id = R.color.black),
        shape = RectangleShape,
        fontSize = RecipeGeneratorActivity.ITEM_TEXT_FONT_SIZE,
        contentPadding = PaddingValues(0.dp),
        onClick = { showAlert = true },
        image = {
            Image(
                modifier = Modifier.padding(horizontal = RecipeGeneratorActivity.IMAGE_TEXT_PADDING),
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
            .fillMaxWidth(RecipeGeneratorActivity.ROW_ITEM_WIDTH),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(RecipeGeneratorActivity.ITEM_SPACING)
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

    RenderScreen(
        state = previewState,
        onIngredientEvent = {}
    )
}