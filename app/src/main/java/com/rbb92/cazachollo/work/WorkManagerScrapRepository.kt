package com.rbb92.cazachollo.work

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.rbb92.cazachollo.data.ScrapWorkDescription
import com.rbb92.cazachollo.data.ScrapWorkRepository
import java.util.concurrent.TimeUnit

class WorkManagerScrapRepository(context: Context) : ScrapWorkRepository {
    private val workManager = WorkManager.getInstance(context)

    override fun addNewWork(description: ScrapWorkDescription) {
        val data = Data.Builder()
        data.putString(UrlScrappingWorker.URL, description.url)
        data.putString(UrlScrappingWorker.UUID, description.uUID)
        data.putBoolean(UrlScrappingWorker.STOCK_ALERT, description.isStock)
        data.putFloat(UrlScrappingWorker.PRICE_ALERT, description.priceLimit)
        data.putString(UrlScrappingWorker.STORE, description.store.name)
        data.putString(UrlScrappingWorker.REGION, description.region.name)


        val workRequestBuilder = PeriodicWorkRequestBuilder<UrlScrappingWorker>(description.period.minutes,TimeUnit.MINUTES)
            .setInputData(data.build())
            .build()

        Log.d("ablancom","Valor de UUID asociado: ${description.uUID}")
        workManager.enqueueUniquePeriodicWork(
            description.uUID,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequestBuilder)
    }

    override fun deleteWork(uuidWork: String) {
        workManager.cancelUniqueWork(uuidWork)

    }
}