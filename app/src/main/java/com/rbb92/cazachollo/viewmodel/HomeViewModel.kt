package com.rbb92.cazachollo.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.rbb92.cazachollo.bbdd.PreferencesManager
import com.rbb92.cazachollo.data.HomeUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private val preferencesManager = PreferencesManager(context)

    private val _homeUIState = MutableStateFlow(HomeUIState())
    val homeUIState: StateFlow<HomeUIState> = _homeUIState

    init {
        clearHomeUIState()
        loadBudgesList()
    }


    fun clearHomeUIState() {
        _homeUIState.value = HomeUIState()
    }

    fun loadBudgesList() {
        Log.d("ablancom","preferencesManager.isBugdeListStar() ${preferencesManager.isBugdeListStar()}")
        Log.d("ablancom","preferencesManager.isBugdeListPoint() ${preferencesManager.isBugdeListPoint()}")
        _homeUIState.update {
            it.copy(budgeListStar = preferencesManager.isBugdeListStar(),
                    budgeListPoint = preferencesManager.isBugdeListPoint()) }
    }
    fun clearBudgeList(){
        preferencesManager.clearBugdeListStar()
        preferencesManager.clearBugdeListPoint()
        _homeUIState.update {
            it.copy(budgeListStar = false,
                    budgeListPoint = false) }
    }

    fun updateScrapeUIState( homeUIdata: HomeUIState) {
        _homeUIState.update {
            it.copy(
                budgeListPoint = homeUIdata.budgeListPoint,
                budgeListStar = homeUIdata.budgeListStar
            )

        }
    }

    fun productAdded()
    {
        _homeUIState.update {
            it.copy(budgeListPoint = true)
        }
    }



}
