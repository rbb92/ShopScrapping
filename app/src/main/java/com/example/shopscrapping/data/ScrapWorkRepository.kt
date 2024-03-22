package com.example.shopscrapping.data

interface ScrapWorkRepository {
    fun addNewWork(description: ScrapWorkDescription)
    fun deleteWork(uuidWork: String)

}