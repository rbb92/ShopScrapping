package com.example.shopscrapping.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.shopscrapping.bbdd.DatabaseRepository
import com.example.shopscrapping.bbdd.ScrappingDatabase
import com.example.shopscrapping.data.ScrapState
import com.example.shopscrapping.data.Store
import com.example.shopscrapping.scrapingTool.StoreFetcher
import com.example.shopscrapping.notifications.notificationTest
import com.example.shopscrapping.utils.priceToFloat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class UrlScrappingWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    val currentContext = context

    val databaseRepository: DatabaseRepository by lazy {
        DatabaseRepository(
            ScrappingDatabase.getDatabase(context).productDao(),
            ScrappingDatabase.getDatabase(context).workDao())
    }
    override suspend fun doWork(): Result {
        val url = inputData.getString(URL)?:""
        val stockAlert = inputData.getBoolean(STOCK_ALERT,false)
        val priceAlert = inputData.getFloat(PRICE_ALERT,0f)
        val store: Store = Store.valueOf(inputData.getString(STORE)?: Store.AMAZON.name)



        //TODO antes de realizar el scrapeo, comprobar si hay restriccion de uso solo por wifi.



        var scrapState = ScrapState()
        withContext(Dispatchers.IO){
//        CoroutineScope(Dispatchers.IO).launch {
            Log.d("ablancom","antes de scrapear en work")
            scrapState = StoreFetcher(url, store)
            Log.d("ablancom","despues de scrapear en work")
        }


        val product = databaseRepository.getProductByUUID(inputData.getString(UUID)?:"").first()
        val workOfProduct = databaseRepository.getWorkByUUID(inputData.getString(UUID)?:"").first()

        if(scrapState.isError)
        {
            //hubo un error, puede ser un problema puntual de conexion (en principio  no, en este punto solo deberia ser problema
            // de scrapeo del html) o problemas al hacer el scraping en el html, CONTABILIZAR busqueda fallida
            // Result.failure() ?¿
            workOfProduct.let{
                val modifiedWork = it.copy(numeroBusquedasFallidas = it.numeroBusquedasFallidas + 1)
                databaseRepository.updateWork(modifiedWork)
            }

            return Result.success()
        }

        val dataForWorkCore = RequirementsForWork(
            currentPrice = priceToFloat(scrapState.price) ,
            currentMinPrice = priceToFloat(scrapState.globalMinPrice),
            currentDate = System.currentTimeMillis()
            )
        //finalizar? si product o workOfProduct estan vacios o son incoherentes, no deberia
        // nunca llegar a este caso!
        if((product == null) )
            return  Result.failure()

        product.let {
            dataForWorkCore.urlReferido = if (it.urlReferido.isNotBlank()) it.urlReferido else it.URL
            dataForWorkCore.name = it.nombre
            dataForWorkCore.latestPrice = it.precioActual
            dataForWorkCore.latestMinPrice = it.precioActualGobal
            dataForWorkCore.initialPrice = it.precioInicial
            workOfProduct.let {
                dataForWorkCore.isStock = it.stockAlerta
                dataForWorkCore.isAllPrice = it.todosPrecios
                dataForWorkCore.initialDate = it.fechaInicio
                dataForWorkCore.numberSearchDone = it.numeroBusquedas
                dataForWorkCore.numberSearchFail = it.numeroBusquedasFallidas
                dataForWorkCore.alertPrice = it.precioAlerta
                dataForWorkCore.numberNotifications = it.numeroNotificaciones
                dataForWorkCore.latestNotificationDate = it.fechaUltimaNotificacion

            }
        }


        //llamamos a WorkCore
        val notificationEmited = WorkCore(currentContext,dataForWorkCore).makeDecision()

        // actualizamos bbdd desde aqui, si todo ha ido bien, actualizamos precios actuales,
        // fechas, busquedas, notificaciones ...

        product.let {
            val modifiedProduct = it.copy(precioActual = dataForWorkCore.currentPrice,
                precioActualGobal = dataForWorkCore.currentMinPrice)
            databaseRepository.updateProduct(modifiedProduct)
            Log.d("ablanco","new Entity for Product updated: ${modifiedProduct}")
        }
        workOfProduct.let {
            val modifiedWork = it.copy(numeroBusquedas = it.numeroBusquedas+1,
                numeroNotificaciones = if(notificationEmited) it.numeroNotificaciones+1 else it.numeroNotificaciones,
                fechaUltimaNotificacion = if (notificationEmited) System.currentTimeMillis() else it.fechaUltimaNotificacion)
            databaseRepository.updateWork(modifiedWork)
            Log.d("ablanco","new Entity for work updated: ${modifiedWork}")
        }

        notificationTest(currentContext,"Work","Scrap with context: ${scrapState}",url)
        Log.d("ablanco","Scrapping as work done, result: \n${scrapState}")
        Log.d("ablanco","new Entity for work updated:")



        return Result.success()
    }

    companion object {
        const val URL = "URL"
        const val UUID = "UUID"
        const val STORE = "STORE"
        const val STOCK_ALERT = "STOCK_ALERT"
        const val PRICE_ALERT = "PRICE_ALERT"
    }
}

/*
Esta clase de datos contendra los atributos a tener en cuenta a la hora de realizar el scraping y leer de la bbdd
dentro del work
 */
data class RequirementsForWork(
    var currentPrice: Float = 0.0f,
    var currentMinPrice: Float = 0.0f,
    var latestPrice: Float = 0.0f,
    var latestMinPrice: Float = 0.0f,
    var initialPrice: Float = 0.0f,
    var urlReferido: String = "",
    var name: String = "",
    var isStock: Boolean = false,
    var isAllPrice: Boolean = false,
    var alertPrice: Float = 0.0f,
    var numberSearchDone: Int = 0,
    var numberSearchFail: Int = 0,
    var initialDate: Long = 0,
    var currentDate: Long = 0,
    var numberNotifications: Int = 0,
    var latestNotificationDate: Long = 0)




