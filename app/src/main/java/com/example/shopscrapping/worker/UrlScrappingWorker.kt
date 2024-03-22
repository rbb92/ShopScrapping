package com.example.shopscrapping.worker

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.shopscrapping.data.ScrapState
import com.example.shopscrapping.data.Store
import com.example.shopscrapping.scrapingTool.StoreFetcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UrlScrappingWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val url = inputData.getString(URL)?:""
        val stockAlert = inputData.getBoolean(STOCK_ALERT,false)
        val priceAlert = inputData.getFloat(PRICE_ALERT,0f)
        val store: Store = Store.valueOf(inputData.getString(STORE)?: Store.AMAZON.name)

        var scrapState = ScrapState()
        CoroutineScope(Dispatchers.IO).launch {
            scrapState = StoreFetcher(url, store)
        }

        if(scrapState.isError){
            //TODO hubo un error, puede ser un problema puntual de conexion
            //return Result.failure()
        }

        if(scrapState.price.toFloatOrNull() != null) {
            if (stockAlert) {
                //TODO, articulo en stock, notificar usuario.
            } else {
                if(scrapState.price.toFloatOrNull()!! <= priceAlert){
                    //TODO, alerta de bajada de precio.
                }
            }
        }

        return Result.success()
    }

    companion object {
        const val URL = "URL"
        const val STORE = "STORE"
        const val STOCK_ALERT = "STOCK_ALERT"
        const val PRICE_ALERT = "PRICE_ALERT"
    }
}


