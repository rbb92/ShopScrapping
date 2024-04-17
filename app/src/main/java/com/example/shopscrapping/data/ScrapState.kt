package com.example.shopscrapping.data

data class ScrapState(
    val url: String = "",
    val store: Store = Store.AMAZON,
    val price: String = "",
    val globalMinPrice: String = "",
    val title: String = "",
    val description: String = "",
    val src_image: String = "",
    val isScrapping: Boolean = false,
    val isError: Boolean = false,
    val isScrappingProcess: Boolean = false)
{
}