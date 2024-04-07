package com.example.shopscrapping.bbdd

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * from Product ORDER BY precio ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>
    @Query("SELECT * from Product WHERE UUID = :id")
    fun getProductById(id: Int): Flow<ProductEntity>

    @Query("SELECT * from Product WHERE URL = :id")
    fun getProductByUrl(id: Int): Flow<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ProductEntity)

    @Update
    suspend fun update(item: ProductEntity)

    @Delete
    suspend fun delete(item: ProductEntity)
}