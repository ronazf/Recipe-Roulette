package com.example.reciperoulette.presentation.screens.recipeScreen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.example.reciperoulette.data.local.recipes.RecipeIngredient
import com.example.reciperoulette.data.local.recipes.RecipeStep
import com.example.reciperoulette.data.local.recipes.entities.Recipe
import com.example.reciperoulette.presentation.GeneralConstants
import com.example.reciperoulette.presentation.components.EditableText
import com.example.reciperoulette.presentation.components.ShowWarning
import com.example.reciperoulette.presentation.components.Title
import com.example.reciperoulette.presentation.components.alertDialog.components.ErrorAlertDialog
import com.example.reciperoulette.presentation.components.alertDialog.components.SuccessAlertDialog
import com.example.reciperoulette.presentation.components.buttons.GenericButton
import com.example.reciperoulette.presentation.components.dialog.EditableDialog
import com.example.reciperoulette.presentation.components.dragDropList.DragDropList
import com.example.reciperoulette.presentation.components.image.components.BackgroundImage
import com.example.reciperoulette.presentation.screens.homeScreen.HomeConstants
import com.example.reciperoulette.presentation.screens.recipeScreen.userActions.RecipeEvent
import com.example.reciperoulette.presentation.screens.recipeScreen.userActions.RecipeEvent.DragRecipe
import com.example.reciperoulette.presentation.screens.recipeScreen.userActions.RecipeInfoEvent
import com.example.reciperoulette.presentation.screens.recipeScreen.userActions.RecipeState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RecipeScreen(
    state: RecipeState,
    onRecipeEvent: (RecipeEvent) -> Unit,
    onRecipeInfoEvent: (RecipeInfoEvent) -> Unit,
    navigateBack: () -> Unit,
    onLoad: (resource: Int, resourceDescription: String) -> Unit,
    onResult: () -> Unit,
    generated: Boolean = false
) {
    val context = LocalContext.current
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

        IconButton(
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
                },
            onClick = { navigateBack() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = stringResource(id = R.string.back)
            )
        }

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
            GenericButton(
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

            GenericButton(
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
        state.editRecipe?.let {
            var editing by remember { mutableStateOf(false) }

            EditableDialog(
                modifier = Modifier
                    .fillMaxWidth(),
                onEdit = {
                    if (editing) {
                        onRecipeEvent(RecipeEvent.SaveEditRecipe)
                    }
                    editing = !editing
                },
                onCancel = {
                    editing = false
                    onRecipeInfoEvent(RecipeInfoEvent.ResetEditRecipe)
                },
                onDismiss = { onRecipeEvent(RecipeEvent.DismissInfo) },
                icon = { modifier ->
                    Icon(
                        modifier = modifier,
                        painter = painterResource(id = R.drawable.info),
                        contentDescription = stringResource(id = R.string.info)
                    )
                },
                title = { modifier ->
                    Text(
                        modifier = modifier,
                        text = stringResource(id = R.string.show_info),
                        fontSize = GeneralConstants.BUTTON_FONT_SIZE,
                        fontFamily = GeneralConstants.FONT_FAMILY
                    )
                }
            ) { modifier ->
                RecipeInfo(
                    modifier = modifier,
                    name = it.recipeName,
                    ingredients = it.ingredients,
                    serves = it.serves,
                    editing = editing,
                    onRecipeInfoEvent = onRecipeInfoEvent
                )
            }
        }
    }

    if (state.error.isNotEmpty()) {
        if (state.info) {
            ShowWarning(context = context, state.error)
            onRecipeEvent(RecipeEvent.ClearError)
        } else {
            ErrorAlertDialog(
                modifier = Modifier.fillMaxWidth(),
                onDismiss = { onRecipeEvent(RecipeEvent.ClearError) },
                errorText = state.error
            )
        }
    }

    if (state.success.isNotEmpty() && !state.loading) {
        if (state.info) {
            ShowWarning(context = context, state.success)
            onRecipeEvent(RecipeEvent.ClearSuccess)
        } else {
            SuccessAlertDialog(
                modifier = Modifier.fillMaxWidth(),
                onDismiss = { onRecipeEvent(RecipeEvent.ClearSuccess) },
                successText = state.success
            )
        }
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
                IconButton(
                    modifier = Modifier
                        .sizeIn(
                            maxHeight = GeneralConstants.MAX_ICON_BUTTON_SIZE,
                            maxWidth = GeneralConstants.MAX_ICON_BUTTON_SIZE
                        ),
                    onClick = {
                        clipBoardManager.setText(AnnotatedString(it))
                        ShowWarning(context, linkCopied)
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.copy_link),
                        contentDescription = stringResource(id = R.string.copy_link)
                    )
                }
            }
            if (recipe.link == null) {
                Spacer(modifier = Modifier)
            }
            IconButton(
                modifier = Modifier
                    .sizeIn(
                        maxHeight = GeneralConstants.MAX_ICON_BUTTON_SIZE,
                        maxWidth = GeneralConstants.MAX_ICON_BUTTON_SIZE
                    ),
                onClick = {
                    onRecipeEvent(RecipeEvent.ShowInfo)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.info),
                    contentDescription = stringResource(id = R.string.info)
                )
            }
        }
        RecipeSteps(
            steps = recipe.steps,
            onRecipeEvent = onRecipeEvent
        )
    }
}

@Composable
fun RecipeInfo(
    modifier: Modifier,
    name: String,
    ingredients: List<RecipeIngredient>,
    serves: Int?,
    editing: Boolean,
    onRecipeInfoEvent: (RecipeInfoEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(RecipeConstants.ROW_ITEM_WIDTH),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(RecipeConstants.ITEM_SPACING)
    ) {
        EditableText(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart),
            label = "Name",
            text = name,
            placeHolder = "Recipe Name",
            isEditing = editing,
            onValueChange = {
                onRecipeInfoEvent(
                    RecipeInfoEvent.EditRecipeName(it)
                )
            }
        )
        EditableText(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart),
            label = "Ingredients",
            isEditing = editing
        )
        LazyColumn(
            modifier = Modifier
                .heightIn(max = RecipeConstants.MAX_DIALOG_ITEM_HEIGHT)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(RecipeConstants.ITEM_SPACING)
        ) {
            itemsIndexed(ingredients) { index, recipeIngredient ->
                key(recipeIngredient.id) {
                    EditableText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.TopStart),
                        text = recipeIngredient.ingredient,
                        placeHolder = "Recipe Ingredient",
                        isEditing = editing,
                        onValueChange = {
                            onRecipeInfoEvent(
                                RecipeInfoEvent.EditIngredient(
                                    recipeIngredient,
                                    index
                                )
                            )
                        },
                        onRemove = {
                            onRecipeInfoEvent(
                                RecipeInfoEvent.RemoveIngredient(recipeIngredient.id)
                            )
                        }
                    )
                }
            }
            if (editing) {
                item {
                    FilledIconButton(
                        modifier = Modifier
                            .padding(
                                GeneralConstants.IMAGE_TEXT_PADDING
                            )
                            .sizeIn(
                                maxHeight = GeneralConstants.MAX_ICON_BUTTON_SIZE,
                                maxWidth = GeneralConstants.MAX_ICON_BUTTON_SIZE
                            ),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = colorResource(id = R.color.light_blue_green)
                        ),
                        onClick = {
                            onRecipeInfoEvent(
                                RecipeInfoEvent.AddIngredient
                            )
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.add),
                            contentDescription = stringResource(id = R.string.add_icon)
                        )
                    }
                }
            }
        }
        if (serves != null || editing) {
            EditableText(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopStart),
                label = "Serves",
                text = serves?.toString(),
                placeHolder = "Serving Number",
                isEditing = editing,
                numerical = true,
                onValueChange = {
                    onRecipeInfoEvent(
                        RecipeInfoEvent.EditRecipeServing(
                            if (it.isEmpty()) null else it.toInt()
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun RecipeSteps(
    steps: List<RecipeStep>,
    onRecipeEvent: (RecipeEvent) -> Unit
) {
    DragDropList(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(RecipeConstants.ITEM_SPACING),
        contentPadding = PaddingValues(vertical = RecipeConstants.IMAGE_TEXT_PADDING),
        items = steps,
        onDrop = { begin, end ->
            onRecipeEvent(DragRecipe(begin, end))
        },
        listContent = {
            item { AddRecipeStep() }

            item { Spacer(modifier = Modifier.padding(vertical = RecipeConstants.RECIPE_LIST_SPACER)) }
        }
    ) { index, item, dragging ->
        val elevation by animateDpAsState(
            targetValue = if (dragging) {
                RecipeConstants.RECIPE_CARD_DRAGGING_ELEVATION
            } else {
                RecipeConstants.RECIPE_CARD_ELEVATION
            },
            label = ""
        )
        val shadowElevation by animateDpAsState(
            targetValue = if (dragging) {
                RecipeConstants.RECIPE_SHADOW_DRAGGING_ELEVATION
            } else {
                RecipeConstants.RECIPE_SHADOW_ELEVATION
            },
            label = ""
        )
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = shadowElevation,
                    shape = RoundedCornerShape(RecipeConstants.RECIPE_CORNER_ROUNDING)
                ),
            shape = RoundedCornerShape(RecipeConstants.RECIPE_CORNER_ROUNDING),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.light_grey)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = elevation
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
                item.minutes?.let {
                    Text(
                        modifier = Modifier
                            .wrapContentSize(Alignment.TopEnd)
                            .fillMaxWidth(),
                        text = "${item.minutes} Minutes",
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
                text = item.instructions,
                fontSize = GeneralConstants.TEXT_FONT_SIZE,
                textAlign = TextAlign.Start,
                fontFamily = GeneralConstants.FONT_FAMILY
            )
        }
    }
}

@Composable
fun AddRecipeStep() {
    val borderColor = colorResource(id = R.color.black)
    val dashedStyle = Stroke(
        width = RecipeConstants.BORDER_WIDTH,
        pathEffect = PathEffect.dashPathEffect(
            floatArrayOf(
                RecipeConstants.RECIPE_CORNER_ROUNDING.toFloat(),
                RecipeConstants.RECIPE_CORNER_ROUNDING.toFloat()
            )
        )
    )
    Column {
        Box(
            modifier = Modifier
                .height(GeneralConstants.ITEM_SIZE)
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
                .drawBehind {
                    drawRoundRect(
                        color = borderColor,
                        cornerRadius = CornerRadius(
                            RecipeConstants.RECIPE_CORNER_ROUNDING.toFloat()
                        ),
                        style = dashedStyle
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(RecipeConstants.TEXT_ICON_SPACING)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = stringResource(id = R.string.add_icon)
                )
                Text(
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center),
                    text = stringResource(id = R.string.add_recipe_step),
                    fontSize = GeneralConstants.TEXT_FONT_SIZE,
                    textAlign = TextAlign.Center,
                    fontFamily = GeneralConstants.FONT_FAMILY
                )
            }
        }
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
        onRecipeInfoEvent = {},
        navigateBack = {},
        onLoad = { _, _ -> },
        onResult = {}
    )
}
