package com.example.shopscrapping.ui

import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopscrapping.ui.screens.ScrapingScreen
import com.example.shopscrapping.ui.screens.ScrappingHomeContent
import com.example.shopscrapping.ui.utils.ScreenSize
import com.example.shopscrapping.viewmodel.ScrapViewModel

@Composable
fun ShopScrappingApp(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
) {

    val screenSize = ScreenSize.PORTRAIT
    /***TODO: Para detectar el tipo de pantalla y adaptar posteriormente la GUI a ello ***/
//    val navigationType: ReplyNavigationType
//
//    when (windowSize) {
//        WindowWidthSizeClass.Compact -> {
//            navigationType = ReplyNavigationType.BOTTOM_NAVIGATION
//        }
//        WindowWidthSizeClass.Medium -> {
//            navigationType = ReplyNavigationType.NAVIGATION_RAIL
//        }
//        WindowWidthSizeClass.Expanded -> {
//            navigationType = ReplyNavigationType.PERMANENT_NAVIGATION_DRAWER
//        }
//        else -> {
//            navigationType = ReplyNavigationType.BOTTOM_NAVIGATION
//        }
//    }

    ScrappingHomeContent(
        modifier = modifier
    )
//    ScrapingScreen(
//        scrapUiState = scrapUiState,
//        onButtonPress = { url: String ->
//            Log.d("ablanco","boton pulsado")
//            viewModel.scrapeUrl(url)
//        },
//        modifier = modifier
//    )
}
