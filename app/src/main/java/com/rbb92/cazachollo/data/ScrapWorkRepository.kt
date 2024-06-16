package com.rbb92.cazachollo.data

interface ScrapWorkRepository {
    fun addNewWork(description: ScrapWorkDescription)
    fun deleteWork(uuidWork: String)

}