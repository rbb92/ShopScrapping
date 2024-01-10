package com.example.shopscrapping.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopscrapping.scrapingTool.AmazonFetcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScrapViewModel : ViewModel() {
    private val _scrapState = MutableStateFlow(ScrapUiState())
    val scrapeState: StateFlow<ScrapUiState> = _scrapState

    init {
        clearScrapeUIState()
    }

    fun clearScrapeUIState() {
        _scrapState.value = ScrapUiState()
    }

    fun updateScrapeUIState( scrapData: ScrapUiState) {
        _scrapState.update {
            it.copy(
                url = scrapData.url,
                price = scrapData.price,
                title = scrapData.title,
                description = scrapData.description,
                src_image = scrapData.src_image
            )

        }
    }
    //Corrutine scraping
    fun scrapeUrl( url: String){
        viewModelScope.launch {
            Log.d("ablanco","preparando scrape")
            updateScrapeUIState(AmazonFetcher(url))
//            fetchAmazonStore(url)
//            updateScrapeUIState(fetchAmazonStore(url))
        }
    }



}