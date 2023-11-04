package com.example.reciperoulette.presentation.activities.screens.recipeScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.reciperoulette.R
import com.example.reciperoulette.presentation.activities.GeneralConstants
import com.example.reciperoulette.presentation.activities.components.GenericBtn
import com.example.reciperoulette.presentation.activities.components.ShowWarning
import com.example.reciperoulette.presentation.activities.components.Title
import com.example.reciperoulette.presentation.activities.components.alertDialog.components.ErrorAlertDialog
import com.example.reciperoulette.presentation.activities.components.alertDialog.components.InfoAlertDialog
import com.example.reciperoulette.presentation.activities.components.alertDialog.components.SuccessAlertDialog
import com.example.reciperoulette.presentation.activities.components.image.components.BackgroundImage
import com.example.reciperoulette.presentation.activities.screens.homeScreen.HomeConstants
import com.example.reciperoulette.presentation.activities.screens.recipeScreen.userActions.RecipeEvent
import com.example.reciperoulette.presentation.activities.screens.recipeScreen.userActions.RecipeState
import com.example.reciperoulette.data.local.recipes.RecipeStep
import com.example.reciperoulette.data.local.recipes.entities.Recipe

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RecipeScreen(
    state: RecipeState,
    onRecipeEvent: (RecipeEvent) -> Unit,
    navigateBack: () -> Unit,
    onLoad: (resource: Int, resourceDescription: String) -> Unit,
    onResult: () -> Unit,
    generated: Boolean = false
) {
    var blurRadius by remember { mutableStateOf(GeneralConstants.UN_BLUR_RADIUS) }

    ConstraintLayout(
        modifier = Modifier
            .pointerInteropFilter { state.loading }
            .blur(blurRadius)
    ) {
        val (title, backBtn, backgroundImage, recipe, regenerateBtn, saveBtn) = createRefs()

        Title(
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, margin = RecipeConstants.DISPLAYED_MARGIN)
                absoluteLeft.linkTo(parent.absoluteLeft)
                absoluteRight.linkTo(parent.absoluteRight)
            },
            title = stringResource(id = R.string.recipe)
        )

        Icon(
            modifier = Modifier
                .constrainAs(backBtn) {
                    top.linkTo(
                        parent.top,
                        margin = RecipeConstants.BACK_BUTTON_TOP_MARGIN
                    )
                    absoluteLeft.linkTo(
                        parent.absoluteLeft,
                        margin = RecipeConstants.BACK_BUTTON_LEFT_MARGIN
                    )
                }
                .clickable { navigateBack() },
            painter = painterResource(id = R.drawable.back),
            contentDescription = stringResource(id = R.string.back)
        )

        BackgroundImage(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(backgroundImage) {
                    top.linkTo(title.bottom)
                    bottom.linkTo(regenerateBtn.top)
                },
            painter = painterResource(id = R.drawable.recipe_background),
            description = stringResource(id = R.string.recipe_background)
        )

        state.recipe?.let {
            RecipeDetails(
                modifier = Modifier
                    .constrainAs(recipe) {
                        top.linkTo(title.bottom)
                        absoluteLeft.linkTo(parent.absoluteLeft)
                        absoluteRight.linkTo(parent.absoluteRight)
                        bottom.linkTo(regenerateBtn.top, margin = RecipeConstants.DISPLAYED_MARGIN)
                        height = Dimension.fillToConstraints
                    },
                recipe = it,
                onRecipeEvent = onRecipeEvent
            )
        }

        if (generated) {
            GenericBtn(
                modifier = Modifier.constrainAs(regenerateBtn) {
                    bottom.linkTo(saveBtn.top)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                },
                text = stringResource(id = R.string.regenerate_recipe),
                containerColor = colorResource(id = R.color.dark_pink),
                contentColor = colorResource(id = R.color.black),
                shape = RoundedCornerShape(HomeConstants.CORNER_ROUNDING),
                fontSize = HomeConstants.BUTTON_FONT_SIZE,
                onClick = {
                    onRecipeEvent(RecipeEvent.RegenerateRecipe)
                }
            )

            GenericBtn(
                modifier = Modifier.constrainAs(saveBtn) {
                    bottom.linkTo(parent.bottom, margin = RecipeConstants.DISPLAYED_MARGIN)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                },
                text = stringResource(id = R.string.save_recipe),
                containerColor = colorResource(id = R.color.green),
                contentColor = colorResource(id = R.color.black),
                shape = RoundedCornerShape(HomeConstants.CORNER_ROUNDING),
                fontSize = HomeConstants.BUTTON_FONT_SIZE,
                onClick = {
                    onRecipeEvent(RecipeEvent.SaveRecipe)
                }
            )
        }
    }

    blurRadius = if (state.loading) {
        onLoad(
            R.drawable.apple,
            stringResource(id = R.string.apple_animation)
        )
        GeneralConstants.BLUR_RADIUS
    } else {
        onResult()
        GeneralConstants.UN_BLUR_RADIUS
    }

    if (state.info) {
        state.recipe?.let {
            InfoAlertDialog(
                modifier = Modifier.fillMaxWidth(),
                onDismiss = { onRecipeEvent(RecipeEvent.DismissInfo) }
            ) {
                RecipeInfo(
                    name = it.recipeName,
                    ingredients = it.ingredients,
                    serves = it.serves
                )
            }
        }
    }

    if (state.error.isNotEmpty()) {
        ErrorAlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismiss = { onRecipeEvent(RecipeEvent.ClearError) },
            errorText = state.error
        )
    }

    if (state.success.isNotEmpty() && !state.loading) {
        SuccessAlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismiss = { onRecipeEvent(RecipeEvent.ClearSuccess) },
            successText = state.success
        )
    }
}

@Composable
fun RecipeDetails(
    modifier: Modifier,
    recipe: Recipe,
    onRecipeEvent: (RecipeEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(RecipeConstants.ROW_ITEM_WIDTH),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(GeneralConstants.IMAGE_TEXT_PADDING),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            recipe.link?.let {
                val clipBoardManager = LocalClipboardManager.current
                val context = LocalContext.current
                val linkCopied = stringResource(id = R.string.link_copy_warning)
                Icon(
                    modifier = Modifier
                        .clickable {
                            clipBoardManager.setText(AnnotatedString(it))
                            ShowWarning(context, linkCopied)
                        },
                    painter = painterResource(id = R.drawable.copy_link),
                    contentDescription = stringResource(id = R.string.copy_link)
                )
            }
            if (recipe.link == null) Spacer(modifier = Modifier)
            Icon(
                modifier = Modifier
                    .clickable { onRecipeEvent(RecipeEvent.ShowInfo) },
                painter = painterResource(id = R.drawable.info),
                contentDescription = stringResource(id = R.string.info)
            )
        }
        RecipeSteps(
            steps = recipe.steps
        )
    }
}

@Composable
fun RecipeInfo(
    name: String,
    ingredients: List<String>,
    serves: Int?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(RecipeConstants.ROW_ITEM_WIDTH)
            .padding(GeneralConstants.IMAGE_TEXT_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(RecipeConstants.ITEM_SPACING)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart),
            text = "Name: $name",
            fontSize = GeneralConstants.TEXT_FONT_SIZE,
            textAlign = TextAlign.Start,
            fontFamily = GeneralConstants.FONT_FAMILY
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart),
            text = "Ingredients:",
            fontSize = GeneralConstants.TEXT_FONT_SIZE,
            textAlign = TextAlign.Start,
            fontFamily = GeneralConstants.FONT_FAMILY
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(RecipeConstants.ITEM_SPACING)
        ) {
            items(ingredients) { ingredient ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.TopStart),
                    text = ingredient,
                    fontSize = GeneralConstants.TEXT_FONT_SIZE,
                    textAlign = TextAlign.Start,
                    fontFamily = GeneralConstants.FONT_FAMILY
                )
            }
        }
        serves?.let {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopStart),
                text = "Serves: $it",
                fontSize = GeneralConstants.TEXT_FONT_SIZE,
                textAlign = TextAlign.Start,
                fontFamily = GeneralConstants.FONT_FAMILY
            )
        }
    }
}

@Composable
fun RecipeSteps(
    steps: List<RecipeStep>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(RecipeConstants.ITEM_SPACING),
        contentPadding = PaddingValues(vertical = RecipeConstants.IMAGE_TEXT_PADDING)
    ) {
        itemsIndexed(steps) { index, step ->
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(RecipeConstants.RECIPE_CORNER_ROUNDING),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.light_grey)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = RecipeConstants.RECIPE_CARD_ELEVATION
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(
                            top = GeneralConstants.IMAGE_TEXT_PADDING,
                            start = GeneralConstants.IMAGE_TEXT_PADDING,
                            end = GeneralConstants.IMAGE_TEXT_PADDING,
                            bottom = GeneralConstants.IMAGE_TEXT_PADDING
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier
                            .wrapContentSize(Alignment.TopStart),
                        text = "Step ${index + 1}",
                        fontSize = RecipeConstants.STEP_FONT_SIZE,
                        textAlign = TextAlign.Start,
                        fontFamily = GeneralConstants.FONT_FAMILY
                    )
                    step.minutes?.let {
                        Text(
                            modifier = Modifier
                                .wrapContentSize(Alignment.TopEnd)
                                .fillMaxWidth(),
                            text = "${step.minutes} Minutes",
                            fontSize = RecipeConstants.MINUTES_FONT_SIZE,
                            textAlign = TextAlign.End,
                            fontFamily = GeneralConstants.FONT_FAMILY
                        )
                    }
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.TopStart)
                        .padding(
                            start = GeneralConstants.IMAGE_TEXT_PADDING,
                            end = GeneralConstants.IMAGE_TEXT_PADDING,
                            bottom = GeneralConstants.IMAGE_TEXT_PADDING
                        ),
                    text = step.instructions,
                    fontSize = GeneralConstants.TEXT_FONT_SIZE,
                    textAlign = TextAlign.Start,
                    fontFamily = GeneralConstants.FONT_FAMILY
                )
            }
        }

        item { Spacer(modifier = Modifier.padding(vertical = RecipeConstants.RECIPE_LIST_SPACER)) }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    val previewState = RecipeState(
        recipe = Recipe(
            recipeName = "recipe",
            ingredients = listOf(),
            steps = listOf(
                RecipeStep(
                    instructions = "instruction1",
                    minutes = 1
                ),
                RecipeStep(
                    instructions = "instruction2",
                    minutes = 2
                )
            ),
            link = "some link"
        )
    )

    RecipeScreen(
        state = previewState,
        onRecipeEvent = {},
        navigateBack = {},
        onLoad = { _, _ -> },
        onResult = {}
    )
}