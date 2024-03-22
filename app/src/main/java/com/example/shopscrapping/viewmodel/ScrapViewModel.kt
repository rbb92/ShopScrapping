package com.example.shopscrapping.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopscrapping.data.ScrapState
import com.example.shopscrapping.scrapingTool.AmazonFetcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScrapViewModel : ViewModel() {
    private val _scrapState = MutableStateFlow(ScrapState())
    val scrapeState: StateFlow<ScrapState> = _scrapState

    init {
        clearScrapeUIState()
    }

    fun clearScrapeUIState() {
        _scrapState.value = ScrapState()
    }

    fun updateScrapeUIState( scrapData: ScrapState) {
        _scrapState.update {
            it.copy(
                url = scrapData.url,
                price = scrapData.price,
                title = scrapData.title,
                description = scrapData.description,
                src_image = scrapData.src_image,
                isError = scrapData.isError,
                isScrapping = scrapData.isScrapping
            )

        }
    }
    //Corrutine scraping
    fun scrapeUrl( url: String){
        viewModelScope.launch {
            Log.d("ablanco","preparando scrape")
            _scrapState.update { it.copy(isScrapping = true) }
            updateScrapeUIState(AmazonFetcher(url))
//            _scrapState.update { it.copy(isScrapping = false) }
//            fetchAmazonStore(url)
//            updateScrapeUIState(fetchAmazonStore(url))
        }
    }



}