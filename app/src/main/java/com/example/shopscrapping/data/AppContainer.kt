package com.example.shopscrapping.data

import android.content.Context
import com.example.shopscrapping.bbdd.DatabaseRepository
import com.example.shopscrapping.bbdd.ScrappingDatabase
import com.example.shopscrapping.work.WorkManagerScrapRepository

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
