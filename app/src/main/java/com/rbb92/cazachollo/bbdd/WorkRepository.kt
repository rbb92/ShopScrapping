package com.rbb92.cazachollo.bbdd

import kotlinx.coroutines.flow.Flow

interface WorkRepository {
    fun getAllWorks(): Flow<List<WorkEntity>>
    fun getWorkByUUID(uuid: String): Flow<WorkEntity>

    suspend fun insertWork(item: WorkEntity)

    suspend fun updateWork(item: WorkEntity)

    suspend fun deleteWork(item: WorkEntity)
}