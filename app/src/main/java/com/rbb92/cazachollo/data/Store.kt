package com.rbb92.cazachollo.data

import com.rbb92.cazachollo.scrapingTool.extraerDominio

enum class Store {
    ALIEXPRESS,
    AMAZON,
    CARREFOUR,
    CORTEINGLES,
    MEDIAMARKT,
    PCCOMPONENTES,
    NULL,
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
        "CARREFOUR" -> Store.CARREFOUR
        "CORTEINGLES" -> Store.CORTEINGLES
        "MEDIAMARKT" -> Store.MEDIAMARKT
        "PCCOMPONENTES" -> Store.PCCOMPONENTES
        else -> Store.NULL
    }
}

fun getStoreFromURL(url: String): Store{
    val dominio = extraerDominio(url)?: url
    if (dominio.contains("pccomp",ignoreCase = true))
    {
        return Store.PCCOMPONENTES
    }
    if (dominio.contains("corteing",ignoreCase = true))
    {
        return Store.CORTEINGLES
    }
    if (dominio.contains("carrefour",ignoreCase = true))
    {
        return Store.CARREFOUR
    }
    if (dominio.contains("mediam",ignoreCase = true))
    {
        return Store.MEDIAMARKT
    }
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