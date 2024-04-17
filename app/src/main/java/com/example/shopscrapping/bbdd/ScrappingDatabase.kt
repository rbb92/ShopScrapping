package com.example.shopscrapping.bbdd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProductEntity::class,WorkEntity::class], version = 3, exportSchema = true)
abstract class ScrappingDatabase: RoomDatabase() {
    abstract fun workDao(): WorkDao
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var Instance: ScrappingDatabase? = null

        fun getDatabase(context: Context): ScrappingDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ScrappingDatabase::class.java, "scrapping_database")
                    /**
                     * Setting this option in your app's database builder means that Room
                     * permanently deletes all data from the tables in your database when it
                     * attempts to perform a migration with no defined migration path.
                     */
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}