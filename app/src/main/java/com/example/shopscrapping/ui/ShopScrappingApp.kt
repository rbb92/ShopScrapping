package com.example.shopscrapping.ui

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.shopscrapping.ui.screens.ScrappingHomeContent

@Composable
fun ShopScrappingApp(
    activity: Activity,
    modifier: Modifier = Modifier,
    requestNotifications: () -> Unit,
    intent: Intent,
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
        intent = intent,
        modifier = modifier
    )
}
