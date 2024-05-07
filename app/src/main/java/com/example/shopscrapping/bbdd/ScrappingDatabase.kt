package com.example.shopscrapping.bbdd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [ProductEntity::class,WorkEntity::class], version = 8, exportSchema = true)
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
//                    .addMigrations(MIGRATION_6_7)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

//Para migrar de version X a Y especificar los cambios de las tablas aqui para evitar que se borren. Indicar bien las
//Versiones en Migration(,)!!
val MIGRATION_6_7 = object : Migration(7, 8) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Aquí puedes escribir el SQL necesario para migrar los datos
        // Si solo estás añadiendo una nueva columna, puedes utilizar algo como:
        database.execSQL("ALTER TABLE Product ADD COLUMN region TEXT DEFAULT 'ES'")
    }
}