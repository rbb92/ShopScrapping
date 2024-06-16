package com.rbb92.cazachollo.bbdd

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * from Product ORDER BY precioInicial ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>
    @Query("SELECT * from Product WHERE UUID = :id")
    fun getProductById(id: Int): Flow<ProductEntity>

    @Query("SELECT * from Product WHERE URL = :id")
    fun getProductByUrl(id: Int): Flow<ProductEntity>
    @Query("SELECT * from Product WHERE UUID = :uuid")
    fun getProductByUUID(uuid: String): Flow<ProductEntity>

    @Query("DELETE from Product WHERE UUID = :uuid")
    suspend fun deleteFromUUID(uuid: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ProductEntity)

    @Update
    suspend fun update(item: ProductEntity)

    @Delete
    suspend fun delete(item: ProductEntity)
}