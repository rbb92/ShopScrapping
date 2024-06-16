package com.rbb92.cazachollo.data

import com.rbb92.cazachollo.R


//IMPORTANTE, IMAGENES DE PAISES OBTENIDAS DE https://icons8.com/icon/set/flags/color
enum class CountriesCode{
    ES,
    DE,
    GB,
    FR,
    IT,
    CH,
    NL,
    US,
    CN,
    JP,
    KR,
    RU,
    BR,
    CA,
    AU,
    IN,
    MX,
    TR,
    ID,
    TW
}

fun getCurrencyFromCountryCode(country: CountriesCode): String
{
    return when (country){
        CountriesCode.DE,
        CountriesCode.FR,
        CountriesCode.IT,
        CountriesCode.ES,
        CountriesCode.NL -> "EUR"
        CountriesCode.CN -> "CNY"
        CountriesCode.US -> "USD"
        CountriesCode.GB -> "GBP"
        CountriesCode.JP -> "JPY"
        CountriesCode.KR -> "KRW"
        CountriesCode.RU -> "RUB"
        CountriesCode.BR -> "BRL"
        CountriesCode.CA -> "CAD"
        CountriesCode.AU -> "AUD"
        CountriesCode.IN -> "INR"
        CountriesCode.MX -> "MXN"
        CountriesCode.CH -> "CHF"
        CountriesCode.TR -> "TRY"
        CountriesCode.ID -> "IDR"
        CountriesCode.TW -> "TWD"
        else -> "EUR"
    }

}
fun getDrawableFromCountryCode(country: CountriesCode):Int{
    return when (country){
        CountriesCode.DE -> R.drawable.germany
        CountriesCode.FR -> R.drawable.france
        CountriesCode.IT -> R.drawable.italy
        CountriesCode.ES -> R.drawable.spain
        CountriesCode.NL -> R.drawable.netherlands
        CountriesCode.CN -> R.drawable.china
        CountriesCode.US -> R.drawable.usa
        CountriesCode.GB -> R.drawable.uk
        CountriesCode.JP -> R.drawable.japan
        CountriesCode.KR -> R.drawable.southkorea
        CountriesCode.RU -> R.drawable.russia
        CountriesCode.BR -> R.drawable.brazil
        CountriesCode.CA -> R.drawable.canada
        CountriesCode.AU -> R.drawable.australia
        CountriesCode.IN -> R.drawable.india
        CountriesCode.MX -> R.drawable.mexico
        CountriesCode.CH -> R.drawable.switzerland
        CountriesCode.TR -> R.drawable.turkey
        CountriesCode.ID -> R.drawable.indonesia
        CountriesCode.TW -> R.drawable.taiwan
    }

}

