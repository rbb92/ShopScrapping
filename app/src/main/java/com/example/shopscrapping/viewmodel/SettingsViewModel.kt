package com.example.shopscrapping.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.shopscrapping.bbdd.PreferencesManager
import com.example.shopscrapping.data.HomeUIState
import com.example.shopscrapping.data.SettingsUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel (application: Application) : AndroidViewModel(application){

        @SuppressLint("StaticFieldLeak")
        private val context = getApplication<Application>().applicationContext
        private val preferencesManager = PreferencesManager(context)

        private val _settingsUIState = MutableStateFlow(SettingsUIState())
        val settingsUIState: StateFlow<SettingsUIState> = _settingsUIState

        init {
            clearSettingsUiState()
            loadSettings()
        }


        fun clearSettingsUiState() {
            _settingsUIState.value = SettingsUIState()
        }

        fun loadSettings() {
            Log.d("ablancom","preferencesManager.isOnlyWifi() ${preferencesManager.isOnlyWifi()}")
            Log.d("ablancom","preferencesManager.isSecundaryNotificationsDisabled() ${preferencesManager.isSecundaryNotificationsDisabled()}")
            _settingsUIState.update {
                it.copy(isWifiOnly = preferencesManager.isOnlyWifi(),
                    isSecundaryNotificationsDisabled = preferencesManager.isSecundaryNotificationsDisabled())
            }
        }

    fun updateIsWifi(state: Boolean){
        _settingsUIState.update {
            preferencesManager.setOnlyWifi(state)
            it.copy(isWifiOnly = preferencesManager.isOnlyWifi())
        }

        Log.d("ablancom","preferencesManager.isOnlyWifi() ${preferencesManager.isOnlyWifi()}")
        Log.d("ablancom","preferencesManager.isSecundaryNotificationsDisabled() ${preferencesManager.isSecundaryNotificationsDisabled()}")
    }
    fun updateisSecundaryNotificationsDisabled(state: Boolean){
        _settingsUIState.update {
            preferencesManager.setSecundaryNotificationsDisabled(state)
            it.copy(isSecundaryNotificationsDisabled = preferencesManager.isSecundaryNotificationsDisabled())
        }

        Log.d("ablancom","preferencesManager.isOnlyWifi() ${preferencesManager.isOnlyWifi()}")
        Log.d("ablancom","preferencesManager.isSecundaryNotificationsDisabled() ${preferencesManager.isSecundaryNotificationsDisabled()}")
    }

    fun enableTutorials()
    {
        preferencesManager.enableMainTutorial()
        preferencesManager.enableScrapScreenTutorial()
    }



}