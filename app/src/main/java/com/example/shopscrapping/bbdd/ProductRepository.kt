package com.example.shopscrapping.bbdd

import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<ProductEntity>>
    
    fun getProductById(id: Int): Flow<ProductEntity?>

    fun getProductByUrl(id: Int): Flow<ProductEntity?>
    fun getProductByUUID(uuid: String): Flow<ProductEntity?>
    
    suspend fun insertProduct(item: ProductEntity)
    
    suspend fun updateProduct(item: ProductEntity)
    
    suspend fun deleteProduct(item: ProductEntity)
}