package com.example.shopscrapping.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopscrapping.bbdd.DatabaseRepository
import com.example.shopscrapping.data.ScrapListState
import com.example.shopscrapping.data.ScrapWorkRepository
import com.example.shopscrapping.data.ScrapedItem
import com.example.shopscrapping.utils.currencyToString
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

//TODO ScrapListViewModel
class ScrapListViewModel(private val dbRepository: DatabaseRepository,
                         private val scrapWorkRepository: ScrapWorkRepository) : ViewModel() {

    var scrapListState: ScrapListState by mutableStateOf(ScrapListState.Loading)
        private set

    init {
        getScrapList()
    }

    fun getScrapList(){
        viewModelScope.launch {
            if(scrapListState !is ScrapListState.Success)
                scrapListState = ScrapListState.Loading
            scrapListState = try {
                val fullList = mutableListOf<ScrapedItem>()
                val works = dbRepository.getAllWorks().first()
                for (element in works) {
                    val fullItem = ScrapedItem()
                    val product = dbRepository.getProductByUUID(element.UUID).first() // Obtener el primer elemento del flujo
                    product?.let {
                        fullItem.url = it.URL
                        fullItem.description = it.descripcion
                        fullItem.title = it.nombre
                        fullItem.uuid = it.UUID
                        fullItem.src_image = it.urlImagen
                        fullItem.initialPrice = it.precioInicial
                        fullItem.currentPrice = if (element.todosPrecios) it.precioActualGobal else it.precioActual
                        fullItem.store = it.tienda
                        fullItem.currency = currencyToString(it.moneda)
                    }
                    fullItem.numberSearch = element.numeroBusquedas
                    fullItem.limitPrice = element.precioAlerta
                    fullItem.isStock = element.stockAlerta
                    fullItem.initialDate = element.fechaInicio
                    fullItem.latestNotification = element.fechaUltimaNotificacion
                    fullItem.latestSearch = element.fechaUltimaBusqueda
                    fullItem.periodAlert = element.periodo

                    if((fullItem.initialPrice == 0.0f) or (fullItem.currentPrice == 0.0f))
                        fullItem.priceDifference = 0
                    else
                        fullItem.priceDifference = ((fullItem.initialPrice - fullItem.currentPrice)*100/(fullItem.initialPrice)).toInt()

                    fullItem.initialDate = element.fechaInicio

                    fullList.add(fullItem)
                }

                Log.d("ablancom","Lista completa: ${fullList}")
                val sortedListDescending = fullList.sortedByDescending { it.initialDate }
                ScrapListState.Success(sortedListDescending)
            }
            catch (e:Exception)
            {
                Log.d("ablancom","Error obteniendo lista de scrapeos ${e.printStackTrace()}")
                ScrapListState.Empty
            }
        }
    }

    //TODO futuro, la idea es poder actualizar el tiempo y el precio-isStock de un item



    fun removeWork(uuid: String){
        viewModelScope.launch {
            Log.d("ablancom", "Borrando work con uuid  ${uuid}")
            scrapWorkRepository.deleteWork(uuid)
            Log.d("ablancom", "Borrando desde la bbdd ${uuid}" )
            dbRepository.delete(uuid)
            Log.d("ablancom", "Borrado de la bbdd... ${uuid}")
        }
    }
    fun secundaryGoal(scrapItem: ScrapedItem ):Boolean  {
        if(scrapItem.initialPrice > scrapItem.currentPrice)
            return true
        return false
    }
    fun mainGoal(scrapItem: ScrapedItem ):Boolean  {
        if(scrapItem.isStock and (scrapItem.currentPrice > 0.0f))
            return true
        if((scrapItem.currentPrice <= scrapItem.limitPrice) and (scrapItem.currentPrice > 0.0f))
            return true
        return false
    }
}