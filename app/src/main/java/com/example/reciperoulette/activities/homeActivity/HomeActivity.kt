package com.example.reciperoulette.activities.homeActivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.reciperoulette.R
import com.example.reciperoulette.activities.components.image.components.BackgroundImage
import com.example.reciperoulette.activities.components.GenericBtn
import com.example.reciperoulette.activities.components.Title
import com.example.reciperoulette.activities.recipeGeneratorActivity.RecipeGeneratorActivity
import com.example.reciperoulette.ui.theme.RecipeRuletteTheme

class HomeActivity : ComponentActivity() {
    companion object {
        val LOGO_MARGIN = 35.dp
        val DISPLAYED_MARGIN = 25.dp
        val CLICKABLE_MARGIN = 50.dp
        val LOGO_WIDTH = 100.dp
        val LOGO_SHADOW = 4.dp
        val TITLE_FONT_SIZE = 48.sp
        val BUTTON_FONT_SIZE = 20.sp
        const val BUTTON_WIDTH = 0.60F
        const val CORNER_ROUNDING = 50
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeRuletteTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RenderScreen()
                }
            }
        }
    }
}

@Composable
fun RenderScreen() {
    val context = LocalContext.current

    ConstraintLayout {
        val (logo, title, shelfGif, startBtn) = createRefs()

        Logo(
            modifier = Modifier.constrainAs(logo) {
                top.linkTo(parent.top, margin = HomeActivity.LOGO_MARGIN)
            }
        )
        Title(
            modifier = Modifier.constrainAs(title) {
                top.linkTo(logo.bottom, margin = HomeActivity.DISPLAYED_MARGIN)
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
                bottom.linkTo(parent.bottom, margin = HomeActivity.CLICKABLE_MARGIN)
                absoluteLeft.linkTo(parent.absoluteLeft)
                absoluteRight.linkTo(parent.absoluteRight)
            },
            text = stringResource(id = R.string.find_recipe),
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
                .size(HomeActivity.LOGO_WIDTH)
                .shadow(HomeActivity.LOGO_SHADOW, shape = CircleShape)
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
    RenderScreen()
}