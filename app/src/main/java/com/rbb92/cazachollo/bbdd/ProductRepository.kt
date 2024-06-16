package com.rbb92.cazachollo.bbdd

import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<ProductEntity>>


    fun getProductByUUID(uuid: String): Flow<ProductEntity?>
    
    suspend fun insertProduct(item: ProductEntity)
    
    suspend fun updateProduct(item: ProductEntity)
    
    suspend fun deleteProduct(item: ProductEntity)
}