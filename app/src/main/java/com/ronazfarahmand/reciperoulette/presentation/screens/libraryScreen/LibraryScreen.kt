package com.ronazfarahmand.reciperoulette.presentation.screens.libraryScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.ronazfarahmand.reciperoulette.R
import com.ronazfarahmand.reciperoulette.data.local.recipes.RecipeIngredient
import com.ronazfarahmand.reciperoulette.data.local.recipes.entities.Recipe
import com.ronazfarahmand.reciperoulette.presentation.GeneralConstants
import com.ronazfarahmand.reciperoulette.presentation.components.SearchTab
import com.ronazfarahmand.reciperoulette.presentation.components.Title
import com.ronazfarahmand.reciperoulette.presentation.components.alertDialog.components.CancelableAlertDialog
import com.ronazfarahmand.reciperoulette.presentation.components.filter.recipeFilter.components.RecipeFilterTab
import com.ronazfarahmand.reciperoulette.presentation.components.image.components.BackgroundImage
import com.ronazfarahmand.reciperoulette.presentation.screens.libraryScreen.userActions.LibraryEvent
import com.ronazfarahmand.reciperoulette.presentation.screens.libraryScreen.userActions.LibraryState
import com.ronazfarahmand.reciperoulette.presentation.screens.libraryScreen.userActions.RecipeFilter

@Composable
fun LibraryScreen(
    state: LibraryState,
    navigateToRecipe: (Long?) -> Unit,
    onLibraryEvent: (LibraryEvent) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                containerColor = colorResource(id = R.color.dark_blue),
                onClick = {
                    navigateToRecipe(null)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = stringResource(id = R.string.add_icon)
                )
            }
        }
    ) { padding ->
        ConstraintLayout(
            modifier = Modifier.padding(padding)
        ) {
            val (title, searchTab, recipes, backgroundImage) = createRefs()

            Title(
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(parent.top, margin = LibraryConstants.DISPLAYED_MARGIN)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                },
                title = stringResource(id = R.string.library)
            )

            AddSearchFilter(
                modifier = Modifier
                    .constrainAs(searchTab) {
                        top.linkTo(title.bottom, margin = LibraryConstants.DISPLAYED_MARGIN)
                        absoluteLeft.linkTo(parent.absoluteLeft)
                        absoluteRight.linkTo(parent.absoluteRight)
                    }
                    .padding(horizontal = GeneralConstants.IMAGE_TEXT_PADDING),
                searchText = state.searchText,
                filter = state.filter,
                onLibraryEvent = onLibraryEvent
            )

            BackgroundImage(
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(backgroundImage) {
                        top.linkTo(searchTab.bottom)
                        bottom.linkTo(parent.bottom)
                    },
                painter = painterResource(id = R.drawable.library_background),
                description = stringResource(id = R.string.library_background)
            )

            RecipeColumn(
                modifier = Modifier
                    .constrainAs(recipes) {
                        top.linkTo(searchTab.bottom, margin = LibraryConstants.DISPLAYED_MARGIN)
                        absoluteLeft.linkTo(parent.absoluteLeft)
                        absoluteRight.linkTo(parent.absoluteRight)
                    },
                recipes = state.recipes,
                onLibraryEvent = onLibraryEvent,
                navigateToRecipe = navigateToRecipe
            )
        }
    }
}

@Composable
fun AddSearchFilter(
    modifier: Modifier,
    searchText: String,
    filter: RecipeFilter,
    onLibraryEvent: (LibraryEvent) -> Unit
) {
    var showFilter by remember { mutableStateOf(false) }
    val filterIcon = if (showFilter) R.drawable.close_filter else R.drawable.filter
    val filterIconDescription = if (showFilter) R.string.close_filter else R.string.filter
    var text by remember { mutableStateOf(searchText) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        SearchTab(
            modifier = Modifier
                .fillMaxWidth(),
            color = colorResource(id = R.color.white),
            shape = RoundedCornerShape(GeneralConstants.CORNER_ROUNDING),
            placeHolder = stringResource(id = R.string.search_recipe),
            value = text,
            filterIcon = painterResource(id = filterIcon),
            filterIconDescription = stringResource(id = filterIconDescription),
            onFilter = { showFilter = !showFilter }
        ) { it: String ->
            text = it
            onLibraryEvent(
                LibraryEvent.SearchRecipe(it)
            )
        }
        if (showFilter) {
            RecipeFilterTab(
                modifier = Modifier.fillMaxWidth(),
                filter = filter,
                onLibraryEvent = onLibraryEvent
            )
        }
    }
}

@Composable
fun RecipeColumn(
    modifier: Modifier,
    recipes: List<Recipe>,
    onLibraryEvent: (LibraryEvent) -> Unit,
    navigateToRecipe: (Long) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = GeneralConstants.IMAGE_TEXT_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(LibraryConstants.ITEM_SPACING),
        contentPadding = PaddingValues(vertical = GeneralConstants.IMAGE_TEXT_PADDING)
    ) {
        items(recipes) { recipe ->
            key(recipe.recipeId) {
                RecipeRow(
                    recipe = recipe,
                    setFavourite = { id ->
                        onLibraryEvent(
                            LibraryEvent.SetFavourite(id)
                        )
                    },
                    removeRecipe = { recipeItem ->
                        onLibraryEvent(
                            LibraryEvent.DeleteRecipe(recipeItem)
                        )
                    },
                    navigateToRecipe = navigateToRecipe
                )
            }
        }

        item { Spacer(modifier = Modifier.padding(vertical = LibraryConstants.LIBRARY_LIST_SPACER)) }
    }
}

@Composable
fun RecipeRow(
    recipe: Recipe,
    setFavourite: (id: Long) -> Unit,
    removeRecipe: (recipe: Recipe) -> Unit,
    navigateToRecipe: (Long) -> Unit
) {
    val favouriteIcon =
        if (recipe.favourite) R.drawable.favourite_filled else R.drawable.favourite_boarder
    val favouriteDescription =
        if (recipe.favourite) R.string.favourite_filled else R.string.favourite_boarder
    val favouriteIconTint = if (recipe.favourite) R.color.red else R.color.black

    var showAlert by remember { mutableStateOf(false) }

    if (showAlert) {
        ConfirmRemoveRecipe(
            recipeName = recipe.recipeName,
            onConfirm = {
                removeRecipe(recipe)
                showAlert = false
            },
            onCancel = { showAlert = false }
        )
    }

    ElevatedCard(
        modifier = Modifier
            .height(GeneralConstants.ITEM_SIZE)
            .fillMaxWidth(),
        shape = RoundedCornerShape(GeneralConstants.CORNER_ROUNDING),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.light_grey)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = LibraryConstants.LIBRARY_CARD_ELEVATION
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navigateToRecipe(recipe.recipeId) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(LibraryConstants.LIBRARY_TEXT_WIDTH)
                    .wrapContentSize(Alignment.CenterStart)
                    .padding(
                        start = GeneralConstants.IMAGE_TEXT_PADDING
                    ),
                maxLines = 1,
                text = recipe.recipeName,
                overflow = TextOverflow.Ellipsis,
                fontSize = GeneralConstants.TEXT_FONT_SIZE,
                textAlign = TextAlign.Start,
                fontFamily = GeneralConstants.FONT_FAMILY
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    LibraryConstants.LIBRARY_ROW_IMAGE_SPACING
                )
            ) {
                IconButton(
                    modifier = Modifier
                        .sizeIn(
                            maxHeight = GeneralConstants.MAX_ICON_BUTTON_SIZE,
                            maxWidth = GeneralConstants.MAX_ICON_BUTTON_SIZE
                        ),
                    onClick = { setFavourite(recipe.recipeId) }
                ) {
                    Icon(
                        painter = painterResource(id = favouriteIcon),
                        contentDescription = stringResource(id = favouriteDescription),
                        tint = colorResource(id = favouriteIconTint)
                    )
                }
                recipe.link?.let {
                    val uriHandler = LocalUriHandler.current

                    IconButton(
                        modifier = Modifier
                            .sizeIn(
                                maxHeight = GeneralConstants.MAX_ICON_BUTTON_SIZE,
                                maxWidth = GeneralConstants.MAX_ICON_BUTTON_SIZE
                            ),
                        onClick = {
                            uriHandler.openUri("${LibraryConstants.LINK_URI_PREFIX}$it")
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.open_link),
                            contentDescription = stringResource(id = R.string.open_link)
                        )
                    }
                }
                IconButton(
                    modifier = Modifier
                        .padding(
                            end = GeneralConstants.IMAGE_TEXT_PADDING
                        )
                        .sizeIn(
                            maxHeight = GeneralConstants.MAX_ICON_BUTTON_SIZE,
                            maxWidth = GeneralConstants.MAX_ICON_BUTTON_SIZE
                        ),
                    onClick = { showAlert = true }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = stringResource(id = R.string.delete)
                    )
                }
            }
        }
    }
}

@Composable
fun ConfirmRemoveRecipe(
    recipeName: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    CancelableAlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onConfirm = onConfirm,
        onCancel = onCancel,
        iconImage = painterResource(id = R.drawable.library_remove),
        iconDescription = stringResource(id = R.string.library_remove),
        title = stringResource(id = R.string.remove_recipe),
        questionText = stringResource(
            id = R.string.confirm_remove_recipe
        ) + "\n\n" + recipeName
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    val recipes = listOf(
        Recipe(
            recipeName = "recipe1",
            ingredients =
            listOf(
                RecipeIngredient(ingredient = "Carrot"),
                RecipeIngredient(ingredient = "Beef Broth"),
                RecipeIngredient(ingredient = "Potatoes")
            ),
            steps = listOf()
        ),
        Recipe(
            recipeName = "recipe2",
            ingredients = listOf(),
            steps = listOf(),
            link = "some link"
        )
    )
    val previewState = LibraryState(recipes = recipes)

    LibraryScreen(
        state = previewState,
        navigateToRecipe = {},
        onLibraryEvent = {}
    )
}
