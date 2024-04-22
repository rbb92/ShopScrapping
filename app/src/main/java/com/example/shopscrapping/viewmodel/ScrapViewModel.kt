package com.example.shopscrapping.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopscrapping.bbdd.DatabaseRepository
import com.example.shopscrapping.bbdd.ProductEntity
import com.example.shopscrapping.bbdd.WorkEntity
import com.example.shopscrapping.data.ScrapState
import com.example.shopscrapping.data.ScrapWorkDescription
import com.example.shopscrapping.data.ScrapWorkRepository
import com.example.shopscrapping.scrapingTool.AmazonFetcher
import com.example.shopscrapping.utils.priceToFloat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ScrapViewModel(private val dbRepository: DatabaseRepository,
                     private val scrapWorkRepository: ScrapWorkRepository) : ViewModel() {

    private val _scrapState = MutableStateFlow(ScrapState())
    val scrapeState: StateFlow<ScrapState> = _scrapState

    init {
        clearScrapeUIState()
    }

    fun clearScrapeUIState() {
        _scrapState.value = ScrapState()
    }

    fun inScrapingState(){
        _scrapState.update { it.copy(isScrappingProcess = true) }
    }
    fun outScrapingState(){
        _scrapState.update { it.copy(isScrappingProcess = false) }
    }
    fun updateScrapeUIState( scrapData: ScrapState) {
        _scrapState.update {
            it.copy(
                url = scrapData.url,
                price = scrapData.price,
                globalMinPrice =scrapData.globalMinPrice,
                title = scrapData.title,
                description = scrapData.description,
                src_image = scrapData.src_image,
                isError = scrapData.isError,
                isScrapping = scrapData.isScrapping,
                isScrappingProcess = scrapData.isScrappingProcess
            )

        }
    }
    //Corrutine scraping
    fun scrapeUrl( url: String){
        viewModelScope.launch {
            Log.d("ablanco","preparando scrape")

            _scrapState.update { it.copy(isScrapping = true, isScrappingProcess = true) }
            updateScrapeUIState(AmazonFetcher(url)) //Se crea un nuevo ScrapeState
            _scrapState.update { it.copy(isScrappingProcess = true) }
//            fetchAmazonStore(url)
//            updateScrapeUIState(fetchAmazonStore(url))
        }
    }

    fun createNewWork(description: ScrapWorkDescription){
        viewModelScope.launch {
            val generatedUUID = UUID.randomUUID().toString()
            description.uUID = generatedUUID
            scrapWorkRepository.addNewWork(description)
            Log.d("ablancom", "Nuevo work creado ${generatedUUID}")
            Log.d("ablancom", "Valor del precio del producto ${_scrapState.value.price}")
            Log.d("ablancom", "Valor del precio del producto float ${priceToFloat(_scrapState.value.price)}")
            dbRepository.insertProduct(DescriptionToProductEntity(description))
            dbRepository.insertWork(DescriptionToWorkEntity(description))
        }
    }

    fun DescriptionToWorkEntity(description: ScrapWorkDescription): WorkEntity =
        WorkEntity(description.uUID,
                   description.isStock,
                   description.isAllPrices,
                   description.priceLimit,
                   description.period.minutes,
              null,
                   System.currentTimeMillis(),
            0,
            0,
            0,
            0)

    fun DescriptionToProductEntity(description: ScrapWorkDescription): ProductEntity =
        ProductEntity(description.uUID,
                      _scrapState.value.url,
                      _scrapState.value.title,
                      _scrapState.value.description,
                      if(description.isAllPrices) priceToFloat(_scrapState.value.globalMinPrice) else priceToFloat(_scrapState.value.price),
                      priceToFloat(_scrapState.value.globalMinPrice),
                      if(description.isAllPrices) priceToFloat(_scrapState.value.globalMinPrice) else priceToFloat(_scrapState.value.price),
                      _scrapState.value.store.name,
                      _scrapState.value.src_image,
            "")



}