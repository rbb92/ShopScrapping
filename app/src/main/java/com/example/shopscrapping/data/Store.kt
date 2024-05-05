package com.example.shopscrapping.data

import com.example.shopscrapping.scrapingTool.extraerDominio

enum class Store {
    ALIEXPRESS,
    AMAZON,
    NULL
}

//fun storeToString(store:Store): String{
//    return when (store){
//        Store.AMAZON -> "Amazon"
//        Store.ALIEXPRESS -> "Aliexpress"
//        Store.NULL -> "Amazon"
//
//    }
//}

fun stringToStore(store:String): Store{
    return when (store.uppercase()){
        "ALIEXPRESS" -> Store.ALIEXPRESS
        "AMAZON" -> Store.AMAZON
        else -> Store.NULL
    }
}

fun getStoreFromURL(url: String): Store{
    val dominio = extraerDominio(url)?: url

    //ali para patron de aliexpress
    if (dominio.contains("ali",ignoreCase = true))
    {
        return Store.ALIEXPRESS
    }

    if (dominio.contains("amaz",ignoreCase = true) or
        dominio.contains("amzn",ignoreCase = true) )
    {
        return Store.AMAZON
    }

    return Store.NULL

}