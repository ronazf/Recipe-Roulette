package com.example.reciperoulette.activities.screens.libraryScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.reciperoulette.R
import com.example.reciperoulette.activities.components.Title
import com.example.reciperoulette.activities.screens.homeScreen.HomeConstants

@Composable
fun LibraryScreen() {
    ConstraintLayout {
        val (title) = createRefs()

        Title(
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, margin = HomeConstants.DISPLAYED_MARGIN)
            },
            title = stringResource(id = R.string.library)
        )
    }
}