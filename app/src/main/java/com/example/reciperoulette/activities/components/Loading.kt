package com.example.reciperoulette.activities.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.example.reciperoulette.R
import com.example.reciperoulette.activities.components.image.components.GifImage

@Composable
fun Loading(
    resource: Int,
    resourceDescription: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.loading_background))
    ) {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            GifImage(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                resource = resource,
                resourceDescription = resourceDescription
            )
            GifImage(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                resource = R.drawable.loading,
                resourceDescription = stringResource(id = R.string.loading)
            )
        }
    }
}