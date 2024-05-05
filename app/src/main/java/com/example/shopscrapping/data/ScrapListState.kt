package com.example.shopscrapping.data

sealed interface ScrapListState {
    data class Success(val items: List<ScrapedItem>):ScrapListState
    object Loading: ScrapListState
    object Empty: ScrapListState
}

data class ScrapedItem(
    var src_image: String = "",
    var title: String = "",
    var initialPrice: Float = 0.0f,
    var currentPrice: Float = 0.0f,
    var limitPrice: Float = 0.0f,
    var priceDifference: Int = 0, //percentage
    var isStock: Boolean = false,
    var store: String = "",
    var description: String = "",
    var numberSearch: Int = 0,
    var url: String = "",
    var uuid: String = "",
    var initialDate: Long = 0,
    var latestSearch: Long = 0,
    var latestNotification: Long = 0,
    var periodAlert: Long = 0,
    var currency: String = "€"
){

}