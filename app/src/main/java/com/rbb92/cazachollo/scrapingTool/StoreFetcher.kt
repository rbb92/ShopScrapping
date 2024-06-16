package com.rbb92.cazachollo.scrapingTool

import com.rbb92.cazachollo.R
import com.rbb92.cazachollo.data.CountriesCode
import com.rbb92.cazachollo.data.ScrapState
import com.rbb92.cazachollo.data.Store

//Para obtener el favicon de una web:
//http://www.google.com/s2/favicons?domain=
object Constants {
    const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:126.0) Gecko/20100101 Firefox/126.0"
}
suspend fun StoreFetcher (url: String, store:Store,country: CountriesCode): ScrapState =
    when (store)
    {
        Store.AMAZON -> AmazonFetcher(url)
        Store.ALIEXPRESS -> AliexpressFetcher(url, country)
        Store.CARREFOUR -> CarrefourStore(url)
        Store.CORTEINGLES -> CorteInglesStore(url)
        Store.MEDIAMARKT -> MediaMarktStore(url)
        Store.PCCOMPONENTES -> PcComponentesStore(url)
        else -> AmazonFetcher(url)

    }


fun extraerDominio(url: String): String? {
    val ultimoPunto = url.lastIndexOf('.')

    if (ultimoPunto == -1 || ultimoPunto == url.length - 1) {
        return null
    }

    val primeraBarraDespuesDelUltimoPunto = url.indexOf('/', ultimoPunto)

    if (primeraBarraDespuesDelUltimoPunto == -1) {
        return null
    }

    return url.substring(0, primeraBarraDespuesDelUltimoPunto)
}

//TODO IMPORTANTE, ESTA FUNCION SERIA LA IDEAL PARA DEJAR LIMPIA LA URL E INSERTAR NUESTRO CODIGO REFERIDO.
fun removerParametrosUrl(url: String): String {
    val indiceInterrogante = url.indexOf('?')
    return if (indiceInterrogante != -1) {
        url.substring(0, indiceInterrogante)
    } else {
        url
    }
}

fun imageResourceFromStore(store: Store):Int =
    when (store)
    {
        Store.AMAZON -> R.drawable.amazon
        Store.ALIEXPRESS -> R.drawable.aliexpress
        Store.CARREFOUR -> R.drawable.carrefour
        Store.CORTEINGLES -> R.drawable.corteingles
        Store.MEDIAMARKT -> R.drawable.mediamarkt
        Store.PCCOMPONENTES -> R.drawable.pccomponentes
        else -> R.drawable.amazon

    }
fun canSetRegion(store: Store):Boolean =
    when (store)
    {
        Store.AMAZON -> false
        Store.ALIEXPRESS -> true
        Store.CARREFOUR -> false
        Store.CORTEINGLES -> false
        Store.MEDIAMARKT -> false
        Store.PCCOMPONENTES -> false
        else -> false

    }
