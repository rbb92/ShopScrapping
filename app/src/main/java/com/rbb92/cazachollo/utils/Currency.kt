package com.rbb92.cazachollo.utils


fun currencyToString(currency: String): String{
    return when (currency.uppercase()){
        "EUR" -> "€"
        "USD" -> "$"
        "GBP" -> "£"
        else -> currency.take(4)

    }
}
