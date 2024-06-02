package com.example.shopscrapping.ui

import android.app.Activity
import android.content.Context
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.shopscrapping.ui.screens.ScrappingHomeContent

@Composable
fun ShopScrappingApp(
    context: Context,
    windowSize: WindowWidthSizeClass,
    activity: Activity,
    modifier: Modifier = Modifier,
    requestNotifications: () -> Unit,
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
        activity = activity,
        requestNotifications = requestNotifications,
        modifier = modifier
    )
}
