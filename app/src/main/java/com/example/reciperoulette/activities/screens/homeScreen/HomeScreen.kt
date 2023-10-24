package com.example.reciperoulette.activities.screens.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.reciperoulette.R
import com.example.reciperoulette.activities.components.GenericBtn
import com.example.reciperoulette.activities.components.Title
import com.example.reciperoulette.activities.components.image.components.BackgroundImage

@Composable
fun HomeScreen(navigateToIngredient: () -> Unit) {
    ConstraintLayout {
        val (logo, title, shelfGif, startBtn) = createRefs()

        Logo(
            modifier = Modifier.constrainAs(logo) {
                top.linkTo(parent.top, margin = HomeConstants.LOGO_MARGIN)
            }
        )
        Title(
            modifier = Modifier.constrainAs(title) {
                top.linkTo(logo.bottom, margin = HomeConstants.DISPLAYED_MARGIN)
            },
            title = stringResource(id = R.string.app_name)
        )
        BackgroundImage(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(shelfGif) {
                    top.linkTo(title.bottom)
                    bottom.linkTo(startBtn.top)
                },
            painter = painterResource(id = R.drawable.cooking),
            description = stringResource(id = R.string.home_background)
        )
        GenericBtn(
            modifier = Modifier.constrainAs(startBtn) {
                bottom.linkTo(parent.bottom, margin = HomeConstants.CLICKABLE_MARGIN)
                absoluteLeft.linkTo(parent.absoluteLeft)
                absoluteRight.linkTo(parent.absoluteRight)
            },
            text = stringResource(id = R.string.find_recipe),
            containerColor = colorResource(id = R.color.green),
            contentColor = colorResource(id = R.color.black),
            shape = RoundedCornerShape(HomeConstants.CORNER_ROUNDING),
            fontSize = HomeConstants.BUTTON_FONT_SIZE,
            onClick = {
                navigateToIngredient()
            }
        )
    }
}

@Composable
fun Logo(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    ) {
        Box(
            modifier = Modifier
                .size(HomeConstants.LOGO_WIDTH)
                .shadow(HomeConstants.LOGO_SHADOW, shape = CircleShape)
                .clip(CircleShape)
                .background(colorResource(R.color.green))
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.Center),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(id = R.string.logo_description)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {

    HomeScreen {}
}