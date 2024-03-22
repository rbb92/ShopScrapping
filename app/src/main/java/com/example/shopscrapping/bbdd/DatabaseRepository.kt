package com.example.shopscrapping.bbdd

import kotlinx.coroutines.flow.Flow

class DatabaseRepository(private val productDao: ProductDao,
                         private  val workDao: WorkDao) :
                            WorkRepository, ProductRepository{
    override fun getAllProducts(): Flow<List<ProductEntity>> = productDao.getAllProducts()

    override fun getProductById(id: Int): Flow<ProductEntity?> = productDao.getProductById(id)

    override fun getProductByUrl(id: Int): Flow<ProductEntity?> = productDao.getProductByUrl(id)

    override suspend fun insertProduct(item: ProductEntity) = productDao.insert(item)

    override suspend fun updateProduct(item: ProductEntity) = productDao.update(item)

    override suspend fun deleteProduct(item: ProductEntity) = productDao.delete(item)

    override fun getAllWorks(): Flow<List<WorkEntity>> = workDao.getAllWorks()

    override fun getWorkByProductId(id: Int): Flow<WorkEntity> = workDao.getWorkByProductId(id)

    override suspend fun insertWork(item: WorkEntity) = workDao.insert(item)

    override suspend fun updateWork(item: WorkEntity) = workDao.update(item)

    override suspend fun deleteWork(item: WorkEntity) = workDao.delete(item)
}