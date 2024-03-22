package com.example.shopscrapping.bbdd

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkDao {
    @Query("SELECT * from Work ORDER BY precioAlerta ASC")
    fun getAllWorks(): Flow<List<WorkEntity>>
    @Query("SELECT * from Work WHERE IDProducto = :id")
    fun getWorkByProductId(id: Int): Flow<WorkEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: WorkEntity)

    @Update
    suspend fun update(item: WorkEntity)

    @Delete
    suspend fun delete(item: WorkEntity)
}