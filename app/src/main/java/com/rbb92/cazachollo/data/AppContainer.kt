package com.rbb92.cazachollo.data

import android.content.Context
import com.rbb92.cazachollo.bbdd.DatabaseRepository
import com.rbb92.cazachollo.bbdd.ScrappingDatabase
import com.rbb92.cazachollo.work.WorkManagerScrapRepository

interface AppContainer {
    val scrapWorkRepository : ScrapWorkRepository
    val databaseRepository : DatabaseRepository

}

class AppDataContainer(context: Context) : AppContainer {
    override val scrapWorkRepository = WorkManagerScrapRepository(context)

    override val databaseRepository: DatabaseRepository by lazy {
        DatabaseRepository(ScrappingDatabase.getDatabase(context).productDao(),
            ScrappingDatabase.getDatabase(context).workDao())
    }
}
