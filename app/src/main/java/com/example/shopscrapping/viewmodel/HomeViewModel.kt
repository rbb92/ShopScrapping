package com.example.shopscrapping.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopscrapping.bbdd.DatabaseRepository
import com.example.shopscrapping.bbdd.PreferencesManager
import com.example.shopscrapping.bbdd.ProductEntity
import com.example.shopscrapping.bbdd.WorkEntity
import com.example.shopscrapping.data.HomeUIState
import com.example.shopscrapping.data.ScrapState
import com.example.shopscrapping.data.ScrapWorkDescription
import com.example.shopscrapping.data.ScrapWorkRepository
import com.example.shopscrapping.scrapingTool.AmazonFetcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

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
