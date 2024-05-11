package com.example.shopscrapping.bbdd

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun saveData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }
    private fun saveBoolean(key: String, value: Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean(key,value)
        editor.apply()
    }

    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    private fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    //Habilita en sharedPreferences la clave para que sea usada como budge
    fun addBugdeListStar(){
        saveBoolean("budge_list_star",true)
    }
    fun clearBugdeListStar(){
        saveBoolean("budge_list_star",false)
    }
    fun isBugdeListStar() = getBoolean("budge_list_star")
    fun addBugdeListPoint(){
        saveBoolean("budge_list_point",true)
    }
    fun clearBugdeListPoint(){
        saveBoolean("budge_list_point",false)
    }
    fun isBugdeListPoint() = getBoolean("budge_list_point")

    fun isOnlyWifi() = getBoolean("wifi_mode")
    fun setOnlyWifi(enabled:Boolean) = saveBoolean("wifi_mode",enabled)
    fun isSecundaryNotificationsDisabled() = getBoolean("secundary_notifications_disabled")
    fun setSecundaryNotificationsDisabled(disabled:Boolean) = saveBoolean("secundary_notifications_disabled",disabled)

    fun isMainTutorialCompleted() = getBoolean("main_tutorial")
    fun mainTutorialCompleted() = saveBoolean("main_tutorial",true)
    fun enableMainTutorial() = saveBoolean("main_tutorial",false)
    fun isScrapScreenTutorialCompleted() = getBoolean("scrap_screen_tutorial")
    fun scrapScreenTutorialCompleted() = saveBoolean("scrap_screen_tutorial",true)
    fun enableScrapScreenTutorial() = saveBoolean("scrap_screen_tutorial",false)
}