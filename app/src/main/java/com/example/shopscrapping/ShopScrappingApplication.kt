package com.example.shopscrapping

import android.app.Application
import com.example.shopscrapping.data.AppContainer
import com.example.shopscrapping.data.AppDataContainer


class ShopScrappingApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
