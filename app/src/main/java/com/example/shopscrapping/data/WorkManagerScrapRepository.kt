package com.example.shopscrapping.data

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.shopscrapping.worker.UrlScrappingWorker
import java.util.concurrent.TimeUnit

class WorkManagerScrapRepository(context: Context) : ScrapWorkRepository {
    private val workManager = WorkManager.getInstance(context)

    override fun addNewWork(description: ScrapWorkDescription) {
        val data = Data.Builder()
        data.putString(UrlScrappingWorker.URL, description.url)
        data.putBoolean(UrlScrappingWorker.STOCK_ALERT, description.isStock)
        data.putFloat(UrlScrappingWorker.PRICE_ALERT, description.priceLimit)
        data.putString(UrlScrappingWorker.STORE, description.store.name)


        val workRequestBuilder = PeriodicWorkRequestBuilder<UrlScrappingWorker>(description.period.minutes,TimeUnit.MINUTES)
            .setInputData(data.build())
            .build()

        //TODO -> Generar UUID para el work  y almacenarlo en una bbdd local, de momento usando URL como id del work.
        workManager.enqueueUniquePeriodicWork(
            UrlScrappingWorker.URL,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequestBuilder)
    }

    override fun deleteWork(uuidWork: String) {
        workManager.cancelUniqueWork(uuidWork)
    }
}