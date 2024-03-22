package com.example.shopscrapping.bbdd

import kotlinx.coroutines.flow.Flow

interface WorkRepository {
    fun getAllWorks(): Flow<List<WorkEntity>>
    fun getWorkByProductId(id: Int): Flow<WorkEntity>

    suspend fun insertWork(item: WorkEntity)

    suspend fun updateWork(item: WorkEntity)

    suspend fun deleteWork(item: WorkEntity)
}