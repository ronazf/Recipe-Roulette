package com.example.reciperoulette.activities.components.image.components

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.reciperoulette.activities.components.image.ImageConstants

@Composable
fun GifImage(
    modifier: Modifier,
    resource: Int,
    resourceDescription: String
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= ImageConstants.GIF_MINIMUM_SDK) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.build()
    Image(
        modifier = modifier.fillMaxWidth(),
        painter = rememberAsyncImagePainter(
            model = ImageRequest
                .Builder(context)
                .data(data = resource)
                .apply(
                    block = { size(Size.ORIGINAL) }
                )
                .build(),
            imageLoader = imageLoader,
        ),
        contentDescription = resourceDescription
    )
}