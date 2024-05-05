package com.example.shopscrapping.scrapingTool

import com.example.shopscrapping.data.ScrapState
import com.example.shopscrapping.data.Store

suspend fun StoreFetcher (url: String, store:Store): ScrapState =
    when (store)
    {
        Store.AMAZON -> AmazonFetcher(url)
        Store.ALIEXPRESS -> AliexpressFetcher(url) //TODO aliexpress store
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
