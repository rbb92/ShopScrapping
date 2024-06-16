package com.rbb92.cazachollo.ui.utils

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.util.CoilUtils
import com.rbb92.cazachollo.scrapingTool.Constants
import okhttp3.OkHttpClient
import okhttp3.Request



fun createImageLoader(context: Context): ImageLoader {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest: Request = chain.request()
            Log.d("Coil", "Intercepting request to ${originalRequest.url}")
            val newRequest = originalRequest.newBuilder()
                .header("User-Agent", Constants.USER_AGENT)
                .build()
            chain.proceed(newRequest)
        }
        .cache(CoilUtils.createDefaultCache(context))
        .build()

    return ImageLoader.Builder(context)
        .okHttpClient { okHttpClient }
        .build()
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CustomImageLoader(imageUrl: String): Painter {
    val context = LocalContext.current
    val imageLoader = remember { createImageLoader(context) }

    return rememberImagePainter(
        data = imageUrl,
        imageLoader = imageLoader,
        builder = {
            allowHardware(false)
            size(coil.size.OriginalSize)
        }
    )
}

