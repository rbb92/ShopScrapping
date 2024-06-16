package com.rbb92.cazachollo.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbb92.cazachollo.R
import com.rbb92.cazachollo.bbdd.DatabaseRepository
import com.rbb92.cazachollo.bbdd.ProductEntity
import com.rbb92.cazachollo.bbdd.WorkEntity
import com.rbb92.cazachollo.data.CountriesCode
import com.rbb92.cazachollo.data.CurrentProduct
import com.rbb92.cazachollo.data.ScrapState
import com.rbb92.cazachollo.data.ScrapWorkDescription
import com.rbb92.cazachollo.data.ScrapWorkRepository
import com.rbb92.cazachollo.data.Store
import com.rbb92.cazachollo.data.getStoreFromURL
import com.rbb92.cazachollo.scrapingTool.StoreFetcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ScrapViewModel(private val dbRepository: DatabaseRepository,
                     private val scrapWorkRepository: ScrapWorkRepository) : ViewModel() {

    private val _scrapState = MutableStateFlow(ScrapState())
    val scrapeState: StateFlow<ScrapState> = _scrapState

    private val _currentProduct = MutableStateFlow(CurrentProduct())
    val currentProduct: StateFlow<CurrentProduct> = _currentProduct

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
                url_refered = scrapData.url_refered,
                store = scrapData.store,
                product = scrapData.product,
                isError = scrapData.isError,
                isScrapping = scrapData.isScrapping,
                isScrappingProcess = scrapData.isScrappingProcess
            )

        }
    }
    fun updateCurrentProductUI()
    {
        val indexProduct = _scrapState.value.product?.subProductSelected ?: 0
        val price = _scrapState.value.product?.subProduct?.get(indexProduct)?.price ?: 0.0f
        val minPrice = _scrapState.value.product?.subProduct?.get(indexProduct)?.globalMinPrice
        val currency = _scrapState.value.product?.currency ?: "EUR"
        val title = _scrapState.value.product?.title ?: ""
        val subTitle = _scrapState.value.product?.subProduct?.get(indexProduct)?.aditional_title ?: ""
        val srcImageMain = _scrapState.value.product?.src_image ?: ""
        val srcImageSec = _scrapState.value.product?.subProduct?.get(indexProduct)?.src_image ?: ""
        val productId = _scrapState.value.product?.subProduct?.get(indexProduct)?.identifier ?: ""

        _currentProduct.update {
            it.copy(
                url = _scrapState.value.url,
                url_refered = _scrapState.value.url_refered,
                store = _scrapState.value.store,
                price = price,
                currency = currency,
                region = _scrapState.value.region,
                globalMinPrice = minPrice,
                title = title,
                description = subTitle,
                src_image_main = srcImageMain,
                src_image_sec = srcImageSec,
                product_id = productId
            )
        }
    }
    fun selectSubProduct(index:Int) = _scrapState.update {
            val updatedProduct = it.product?.copy(
                subProductSelected = index
            )
            it.copy(
                product = updatedProduct
            )
    }
    fun numberSubProducts(): Int = _scrapState.value.product?.subProduct?.size ?: 1
    fun haveSubProducts(): Boolean = _scrapState.value.product?.subProduct?.size!! > 1

    fun subProductTitle(index: Int) = _scrapState.value.product?.subProduct?.get(index)?.aditional_title ?: ""
    fun subProductImage(index: Int) = _scrapState.value.product?.subProduct?.get(index)?.src_image ?: ""

    fun currentSubProduct() = _scrapState.value.product?.subProductSelected ?: 0
    //Corrutine scraping
    fun scrapeUrl( url: String, store: Store, country: CountriesCode){
        viewModelScope.launch {
            Log.d("ablanco","preparando scrape")

            _scrapState.update { it.copy(isScrapping = true, isScrappingProcess = true) }
            updateScrapeUIState(StoreFetcher(url,store,country))
            _scrapState.update { it.copy(isScrappingProcess = true, region = country) }
            updateCurrentProductUI()
//            fetchAmazonStore(url)
//            updateScrapeUIState(fetchAmazonStore(url))
        }
    }

    fun detectStoreFromURL(url: String): Store = getStoreFromURL(url)

    fun createNewWork(description: ScrapWorkDescription){
        viewModelScope.launch {
            val generatedUUID = UUID.randomUUID().toString()
            description.uUID = generatedUUID
            scrapWorkRepository.addNewWork(description)
            Log.d("ablancom", "Nuevo work creado ${generatedUUID}")
//            Log.d("ablancom", "Valor del precio del producto ${_scrapState.value.price}")
//            Log.d("ablancom", "Valor del precio del producto float ${priceToFloat(_scrapState.value.price)}")
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
                   System.currentTimeMillis(),
            0,
            0,
            0,
            0,
            0)

    fun DescriptionToProductEntity(description: ScrapWorkDescription): ProductEntity =
        ProductEntity(description.uUID,
                      _currentProduct.value.url,
                      _currentProduct.value.title,
                      _currentProduct.value.description,
                      if(description.isAllPrices) _currentProduct.value.globalMinPrice ?: _currentProduct.value.price else _currentProduct.value.price,
            _currentProduct.value.globalMinPrice ?: _currentProduct.value.price,
                      if(description.isAllPrices) _currentProduct.value.globalMinPrice ?: _currentProduct.value.price else _currentProduct.value.price,
                      _scrapState.value.store.name,
                      _currentProduct.value.currency,
                      _currentProduct.value.region.name,
                      _currentProduct.value.product_id,
                      _currentProduct.value.src_image_main,
                      _currentProduct.value.url_refered)

    fun storeMessageAdvertise(store: Store, currentContext: Context): String =
        when (store){
            Store.AMAZON -> currentContext.getString(R.string.message_advertise_amazon)
            Store.ALIEXPRESS -> currentContext.getString(R.string.message_advertise_aliexpress)
            else -> currentContext.getString(R.string.message_advertise_amazon)
        }

}