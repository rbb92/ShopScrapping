package com.rbb92.cazachollo.utils

import com.rbb92.cazachollo.data.Store

fun priceToFloat(price: String , store: Store = Store.AMAZON): Float {

    when (store){
        Store.AMAZON -> {
            val onlyNumbersprice = price.replace(Regex("[^\\d.,]"), "")
            return onlyNumbersprice.replace(Regex("[,]"), ".").replace(Regex("\\.(?=.*\\.)"), "").toFloatOrNull() ?: 0.0f
        }
//TODO !!
        Store.ALIEXPRESS -> {
            val onlyNumbersprice = price.replace(Regex("[^\\d.,]"), "")
            return onlyNumbersprice.replace(Regex("[,]"), ".").replace(Regex("\\.(?=.*\\.)"), "").toFloatOrNull() ?: 0.0f
        }
        else -> {
            val onlyNumbersprice = price.replace(Regex("[^\\d.,]"), "")
            return onlyNumbersprice.replace(Regex("[,]"), ".").replace(Regex("\\.(?=.*\\.)"), "").toFloatOrNull() ?: 0.0f
        }
    }

}
