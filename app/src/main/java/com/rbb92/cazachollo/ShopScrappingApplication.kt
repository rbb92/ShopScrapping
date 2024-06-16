package com.rbb92.cazachollo

import android.app.Application
import com.rbb92.cazachollo.data.AppContainer
import com.rbb92.cazachollo.data.AppDataContainer


class ShopScrappingApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
