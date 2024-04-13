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
import com.example.shopscrapping.data.Store
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

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
                        fullItem.initialPrice = it.precio
                        fullItem.store = it.tienda
                    }
                    fullItem.numberSearch = element.numeroBusquedas
                    fullItem.limitPrice = element.precioAlerta
                    fullItem.isStock = element.stockAlerta
                    fullItem.currentPrice = 0.0f // TODO: asigna el precio actual correctamente

                    fullList.add(fullItem)
                }

                Log.d("ablancom","Lista completa: ${fullList}")
                ScrapListState.Success(fullList)
            }
            catch (e:Exception)
            {
                Log.d("ablancom","Error obteniendo lista de scrapeos ${e.printStackTrace()}")
                ScrapListState.Empty
            }
        }
    }

    //TODO futuro, la idea es poder actualizar el tiempo y el precio-isStock de un item
    fun updateWork(){

    }

    //TODO
    fun removeWork(uuid: String){
        viewModelScope.launch {
            Log.d("ablancom", "Borrando work con uuid  ${uuid}")
            scrapWorkRepository.deleteWork(uuid)
            Log.d("ablancom", "Borrando desde la bbdd ${uuid}" )
            dbRepository.delete(uuid)
            Log.d("ablancom", "Borrado de la bbdd... ${uuid}")
        }
    }
}