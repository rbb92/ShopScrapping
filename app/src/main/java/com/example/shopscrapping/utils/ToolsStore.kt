package com.example.shopscrapping.utils

import com.example.shopscrapping.data.Store
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

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
