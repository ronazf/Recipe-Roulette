package com.example.reciperoulette.activities.recipeGeneratorActivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.reciperoulette.R
import com.example.reciperoulette.activities.components.Dropdown
import com.example.reciperoulette.activities.components.GenericBtn
import com.example.reciperoulette.activities.components.InnerDropdown
import com.example.reciperoulette.activities.components.ItemRow
import com.example.reciperoulette.activities.components.LayeredDropdown
import com.example.reciperoulette.activities.components.Title
import com.example.reciperoulette.activities.homeActivity.HomeActivity
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
        val MAX_DROPDOWN_HEIGHT = 350.dp
        val DROP_SHADOW_ELEVATION = 8.dp
        val PREVIEW_SELECTED_ITEMS = listOf("Carrot", "Beef Broth", "Potatoes")
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
                    color = MaterialTheme.colors.background
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

@Composable
fun RenderScreen(
    state: IngredientState,
    onIngredientEvent: (IngredientEvent) -> Unit
) {
    val context = LocalContext.current

    ConstraintLayout {
        val (title, ingredients, generateBtn, dropDown) = createRefs()

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
            searchText = state.searchText,
            ingredients = state.ingredients,
            ingredientMap = state.mappedIngredients,
            selectedIngredients = state.selectedIngredients,
            onIngredientEvent = onIngredientEvent
        )

        IngredientList(
            modifier = Modifier.constrainAs(ingredients) {
                top.linkTo(dropDown.bottom, margin = RecipeGeneratorActivity.DISPLAYED_MARGIN)
                absoluteLeft.linkTo(parent.absoluteLeft)
                absoluteRight.linkTo(parent.absoluteRight)
                bottom.linkTo(generateBtn.top)
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
            backgroundColor = colorResource(id = R.color.green),
            contentColor = colorResource(id = R.color.black),
            onClick = {
                val intent = Intent(context, RecipeGeneratorActivity::class.java)
                context.startActivity(intent)
            }
        )
    }
}

@Composable
fun AddDropdown(
    modifier: Modifier,
    searchText: String,
    ingredients: List<Ingredient>,
    ingredientMap: Map<CategoryDetail, List<Ingredient>>,
    selectedIngredients: List<String>,
    onIngredientEvent: (IngredientEvent) -> Unit,
) {
    Dropdown(
        modifier = modifier.fillMaxWidth(RecipeGeneratorActivity.ROW_ITEM_WIDTH),
        name = stringResource(id = R.string.add_item),
        isLastLevel = false,
        color = colorResource(id = R.color.green),
        shape = RoundedCornerShape(RecipeGeneratorActivity.CORNER_ROUNDING),
        searchable = true,
        searchResult = { it: String ->
            onIngredientEvent(
                IngredientEvent.SearchIngredient(it)
            )
        },
        clearSearch = {
            onIngredientEvent(
                IngredientEvent.ClearSearch
            )
        },
        content = { onDismiss ->
            if (searchText.isEmpty()) {
                ingredientMap.onEach { entry ->
                    LayeredDropdown(
                        modifier = modifier.fillMaxWidth(),
                        entry = entry,
                        selectedItems = selectedIngredients,
                        onSelect = { ingredient: String ->
                            onIngredientEvent(
                                IngredientEvent.SelectIngredient(ingredient)
                            )
                        },
                        onDismiss = onDismiss
                    )
                }
            } else {
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
                        onDismiss = onDismiss
                    )
                }
            }
        })
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
    RecipeRuletteTheme {
        ConstraintLayout {
            val context = LocalContext.current
            val (title, ingredients, generateBtn, dropDown) = createRefs()

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
                searchText = "",
                ingredients = emptyList(),
                ingredientMap = mapOf(),
                selectedIngredients = mutableListOf(),
                onIngredientEvent = {}
            )

            IngredientList(
                modifier = Modifier.constrainAs(ingredients) {
                    top.linkTo(dropDown.bottom, margin = RecipeGeneratorActivity.DISPLAYED_MARGIN)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                    bottom.linkTo(generateBtn.top)
                    height = Dimension.fillToConstraints
                },
                ingredients = RecipeGeneratorActivity.PREVIEW_SELECTED_ITEMS,
                removeIngredient = {}
            )

            GenericBtn(
                modifier = Modifier.constrainAs(generateBtn) {
                    bottom.linkTo(parent.bottom, margin = HomeActivity.CLICKABLE_MARGIN)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                },
                text = stringResource(id = R.string.generate_recipe),
                backgroundColor = colorResource(id = R.color.green),
                contentColor = colorResource(id = R.color.black),
                onClick = {
                    val intent = Intent(context, RecipeGeneratorActivity::class.java)
                    context.startActivity(intent)
                }
            )
        }
    }
}