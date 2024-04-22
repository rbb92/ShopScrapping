package com.example.shopscrapping.utils

import com.example.shopscrapping.data.Store

fun priceToFloat(price: String , store: Store = Store.AMAZON): Float {

    when (store){
        Store.AMAZON -> {
            val onlyNumbersprice = price.replace(Regex("[^\\d.,]"), "")
            return onlyNumbersprice.replace(Regex("[,]"), ".").replace(Regex("\\.(?=.*\\.)"), "").toFloatOrNull() ?: 0.0f
        }
        Store.NULL -> {
            val onlyNumbersprice = price.replace(Regex("[^\\d.,]"), "")
            return onlyNumbersprice.replace(Regex("[,]"), ".").replace(Regex("\\.(?=.*\\.)"), "").toFloatOrNull() ?: 0.0f
        }
    }

}
