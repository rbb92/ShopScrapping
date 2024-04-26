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
        Store.NULL -> {
            val onlyNumbersprice = price.replace(Regex("[^\\d.,]"), "")
            return onlyNumbersprice.replace(Regex("[,]"), ".").replace(Regex("\\.(?=.*\\.)"), "").toFloatOrNull() ?: 0.0f
        }
    }

}

fun epochToString(epoch: Long): String {
    val date = Date(epoch)
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
}

fun convertirMinutosADiasYMinutos(valorEnMinutos: Long): String {
    val dias = valorEnMinutos / (24 * 60) // 24 horas en un día y 60 minutos en una hora
    val horas = (valorEnMinutos % (24 * 60)) / 60
    val minutos = valorEnMinutos % 60

    // Construir la cadena resultante
    val sb = StringBuilder()
    if (dias != 0L) {
        sb.append("$dias día(s) ")
    }
    if (horas != 0L) {
        sb.append("$horas hora(s) ")
    }
    if (minutos != 0L) {
        sb.append("$minutos minuto(s)")
    }

    return sb.toString()
}