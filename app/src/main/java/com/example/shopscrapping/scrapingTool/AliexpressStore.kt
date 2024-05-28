package com.example.shopscrapping.scrapingTool

import android.util.Log
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.example.shopscrapping.data.CountriesCode
import com.example.shopscrapping.data.ScrapProduct
import com.example.shopscrapping.data.ScrapState
import com.example.shopscrapping.data.Store
import com.example.shopscrapping.data.SubProduct
import com.example.shopscrapping.data.getCurrencyFromCountryCode
import com.example.shopscrapping.utils.priceToFloat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import it.skrape.core.htmlDocument


import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.Cookie
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import it.skrape.fetcher.BrowserFetcher
import it.skrape.fetcher.Request
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.and
import it.skrape.selects.html5.img
import it.skrape.selects.html5.span


suspend fun AliexpressFetcher(urlToScrape: String, region: CountriesCode): ScrapState =
    withContext(Dispatchers.IO) {
        //configure client
        val androidClient = HttpClient(OkHttp){
            engine {
                config {
                    followRedirects(true)
                }
                pipelining = true
            }
            install(UserAgent) {
                agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:125.0) Gecko/20100101 Firefox/125.0"
//                agent = "AliApp(AliExpress/8.51.0) "

            }
        }

        //make request
        try {

            val urlClean = removerParametrosUrl(urlToScrape)
            val first_response = androidClient.get(urlClean)

            val allCookies = first_response.headers.getAll(HttpHeaders.SetCookie)
            val cookieClean = allCookies?.joinToString(separator = "; ")
                ?.replace(Regex("(c_tp=)[^&]+"), "\$1${getCurrencyFromCountryCode(region)}")
                ?.replace(Regex("(region=)[^&]+"), "\$1$region")


            val defaultCookie = "ali_apache_id=33.27.96.10.1714762640932.106340.2; path=/; domain=.aliexpress.com; expires=Wed, 30-Nov-2084 01:01:01 GMT; xman_us_f=x_locale=es_ES&x_l=0&x_c_chg=1&acs_rt=6e1de1ca41b94743b4c04dce969bc676; Domain=.aliexpress.com; Expires=Wed, 21-May-2092 22:11:27 GMT; Path=/; XSRF-TOKEN=2c2b5245-354a-49d7-a275-3fbd59ee18e4; Path=/; HttpOnly; JSESSIONID=F403CB5A5957FB0A5999FFFC24CA615E; Path=/; HttpOnly; intl_common_forever=BRsONuT6tl/Lk8F3iRn2h1tWiCLI0Ci4Nxr3WBaaLQXFw6JDCYrnRA==; Domain=.aliexpress.com; Expires=Wed, 21-May-2092 22:11:27 GMT; Path=/; HttpOnly; intl_locale=es_ES; Domain=.aliexpress.com; Path=/; xman_f=8dCYM3L9O1fSxUk0AIde/TD0xmWxGXp9fNfIibVCwCVMmG1fTiqTqFsG4n17av3jBSoM42v0uBbAu1U0mRcksUvWbZdpIDbpwtt4EsL8oWbzuP7rUpVK/Q==; Domain=.aliexpress.com; Expires=Wed, 21-May-2092 22:11:27 GMT; Path=/; HttpOnly; acs_usuc_t=x_csrf=1anay9y3m_gqi&acs_rt=6e1de1ca41b94743b4c04dce969bc676; Domain=.aliexpress.com; Path=/; xman_t=GM9uOXkeIcnqx+bbYjsoJAscq0nr8b+wihffmtKQYILcmSWpyhIkdXZdfLFQxrQd; Domain=.aliexpress.com; Expires=Thu, 01-Aug-2024 18:57:20 GMT; Path=/; HttpOnly; aep_usuc_f=site=esp&c_tp=${getCurrencyFromCountryCode(region)}&region=${region}&b_locale=es_ES; Domain=.aliexpress.com; Expires=Wed, 21-May-2092 22:11:27 GMT; Path=/"

            val response = androidClient.get(urlClean){
                headers{
                    append(HttpHeaders.Cookie,cookieClean?:defaultCookie)

                }
            }

            val jsonContent = extractJsonResponseAliexpress(response.bodyAsText())?:""

            //for debugging purpouse
            val MAX_LOG_LENGTH = 2000
            for (i in 0..jsonContent.length step MAX_LOG_LENGTH) {
                val chunk = jsonContent.substring(i, minOf(i + MAX_LOG_LENGTH, jsonContent.length))
                Log.d("JSON", chunk)
            }
            val scrapedProduct = parseJsonResponseAliexpress(jsonContent)

            //TODO insertar referido Aliexpress aqui
            ScrapState(url = urlClean, url_refered = urlClean, store = Store.ALIEXPRESS, product = scrapedProduct)

        }
        catch (exc:Exception)
        {
            exc.printStackTrace()
            Log.d("ablancom",exc.toString())
            ScrapState(
                url=urlToScrape,
                store = Store.ALIEXPRESS,
                isError = true)
        }finally {
            androidClient.close()
        }

//        Log.d("ablancom", "after response")
//        Log.d("ablancom", response.bodyAsText().takeLast(100000))
    }


//fun extraerPathProducto(url: String): String? {
//    val regex = Regex("/d?p/(.*?)(?:/|\\?)")
//    val matchResult = regex.find(url)
//    return matchResult?.groupValues?.get(1)
//}


fun extractJsonResponseAliexpress(body: String): String? {
    val prefijo = "window.runParams = "
    val startIndex = body.indexOf(prefijo)

    if (startIndex != -1) {
        val jsonStringStartIndex = startIndex + prefijo.length
        var braceCount = 0 // contador para rastrear el equilibrio de llaves
        var endIndex = -1 // índice de la llave de cierre "}" correspondiente

        // Busca el índice de la llave de cierre "}" correspondiente al índice de apertura "{"
        for (i in jsonStringStartIndex until body.length) {
            when (body[i]) {
                '{' -> braceCount++
                '}' -> {
                    braceCount--
                    if (braceCount == 0) {
                        endIndex = i
                        break
                    }
                }
            }
        }

        // Si se encuentra el índice de cierre "}", extraer el JSON
        if (endIndex != -1) {
            return body.substring(jsonStringStartIndex, endIndex + 1)
        }
    }
    return null
}

fun parseJsonResponseAliexpress(jsonResponse : String): ScrapProduct{
    val parser: Parser = Parser.default()
    val stringBuilder: StringBuilder = StringBuilder(jsonResponse.replace("data","\"data\""))
    val json: JsonObject = parser.parse(stringBuilder) as JsonObject

    //extraemos titulo global
    val tituloGlobal = json.obj("data")?.obj("metaDataComponent")?.string("title")

    //extraemos imagen global
    val imagenGlobal = json.obj("data")?.obj("imageComponent")?.array<String>("image640PathList")?.get(0)

    var currency = "EUR"

    Log.d("parsing","Titulo general: ${tituloGlobal}")
    Log.d("parsing","Imagen general: ${imagenGlobal}")

    val subProductos = json.obj("data")?.obj("priceComponent")?.array<JsonObject>("skuPriceList")
    val listOfSubProducto = mutableListOf<SubProduct>()
    if (subProductos != null) {
        subProductos.forEach { element ->
            var subProducto = SubProduct(identifier = element.string("skuIdStr")?:"",
                price = element.obj("skuVal")?.obj("skuActivityAmount")?.float("value")?:
                        element.obj("skuVal")?.obj("skuAmount")?.float("value")?:0.0f,
                src_image = "",
                aditional_title = ""
            )
            currency = element.obj("skuVal")?.obj("skuActivityAmount")?.string("currency") ?:
                       element.obj("skuVal")?.obj("skuAmount")?.string("currency") ?: currency
            if ((element.obj("skuVal")?.int("availQuantity") ?: 1) == 0)
            {
                subProducto.price = 0.0f //NO estaria en stockk el producto
            }

            val referencia = (element.string("skuPropIds")?:"").split(",")
//            subProducto.reference = referencia TODO

            Log.d("parsing","referencia: ${referencia}")
            json.obj("data")?.obj("skuComponent")?.
            array<JsonObject>("productSKUPropertyList")?.forEachIndexed  { index, elementoRef ->
                elementoRef.array<JsonObject>("skuPropertyValues")?.forEach { element1 ->
                    if (element1.long("propertyValueId") == referencia.get(index).toLong())
                    {
                        if(element1.string("skuPropertyImagePath")!=null)
                            subProducto.src_image = element1.string("skuPropertyImagePath")!! //TODO se podria guardar todas las imagenes?
                        subProducto.aditional_title = (subProducto.aditional_title + " - " + (element1.string("propertyValueDisplayName")?: ""))
                    }
                }
            }
            listOfSubProducto.add(subProducto)
        }
    }

    Log.d("parsing","Subproductos: ${listOfSubProducto}")

    return ScrapProduct(title = tituloGlobal, src_image = imagenGlobal,0,currency ,listOfSubProducto)

}

