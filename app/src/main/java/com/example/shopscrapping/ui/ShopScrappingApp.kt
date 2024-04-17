package com.example.shopscrapping.ui

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopscrapping.bbdd.PreferencesManager
import com.example.shopscrapping.ui.screens.ScrapingScreen
import com.example.shopscrapping.ui.screens.ScrappingHomeContent
import com.example.shopscrapping.ui.utils.ScreenSize
import com.example.shopscrapping.viewmodel.ScrapViewModel

@Composable
fun ShopScrappingApp(
    context: Context,
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
) {



//    val sharedPref = remember { context.getSharedPreferences("budge_preferences", Context.MODE_PRIVATE) }
//    var budgeListScrap by remember { mutableStateOf(sharedPref.getInt("budge_list", 0)) }
//
//    LaunchedEffect(Unit) {
//        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
//            if (key == "budge_list") {
//                budgeListScrap = sharedPref.getInt("budge_list", 0)
//            }
//        }
//        sharedPref.registerOnSharedPreferenceChangeListener(listener)
//
//    }

    // Use the data variable in your Composable


    ScrappingHomeContent(
        modifier = modifier
    )
}
