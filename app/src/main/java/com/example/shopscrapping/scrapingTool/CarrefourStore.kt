package com.example.shopscrapping.scrapingTool


import android.util.Log
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.example.shopscrapping.data.ScrapProduct
import com.example.shopscrapping.data.ScrapState
import com.example.shopscrapping.data.Store
import com.example.shopscrapping.data.SubProduct
import io.ktor.client.*
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.UserAgent
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


suspend fun CarrefourStore(url_: String): ScrapState =
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
                agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:126.0) Gecko/20100101 Firefox/126.0"
            }

        }

        //make request
        try {
            //TODO limpiar URL
            val response = androidClient.get(url_)

            val jsonContent = extractJsonResponseCarrefour(response.bodyAsText())?:""
            val scrapedProduct = parseJsonResponseCarrefour(jsonContent)

            ScrapState(url = url_, url_refered = url_, store = Store.CARREFOUR, product = scrapedProduct)

        }catch (exc:Exception){
            exc.printStackTrace()
            Log.d("ablancom",exc.toString())
            ScrapState(
                url=url_,
                isError = true)
        }finally {
            androidClient.close()
        }

//        Log.d("ablancom", "after response")
//        Log.d("ablancom", response.bodyAsText().takeLast(100000))
    }



fun extractJsonResponseCarrefour(body: String): String? {
    val prefijo = "window.__INITIAL_STATE__="
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

fun parseJsonResponseCarrefour(jsonResponse : String): ScrapProduct {
    val parser: Parser = Parser.default()
    val stringBuilder: StringBuilder = StringBuilder(jsonResponse)
    val json: JsonObject = parser.parse(stringBuilder) as JsonObject

    //extraemos titulo global
    val tituloGlobal = json.obj("pdp")?.obj("product")?.string("name")

    //extraemos imagen global TODO
    val imagenGlobal_base  = json.obj("pdp")?.obj("product")?.array<JsonObject>("colors")?.get(0)?.array<JsonObject>("images")?.get(0)?.string("medium")?:""

    val unicodeRegex = """\\u([0-9A-Fa-f]{4})""".toRegex()

    // Replace each Unicode escape with the corresponding character

    val imagenGlobal = unicodeRegex.replace(imagenGlobal_base) {
        val charCode = it.groupValues[1].toInt(16)
        charCode.toChar().toString()
    }
    var currency = "EUR"

    Log.d("parsing","Titulo general: ${tituloGlobal}")
    Log.d("parsing","Imagen general: ${imagenGlobal}")

    val offers = json.obj("pdp")?.obj("product")?.array<JsonObject>("skus")?.get(0)?.array<JsonObject>("offers")
    val listOfSubProducto = mutableListOf<SubProduct>()
    val globalPrice = offers?.get(0)?.string("price")?.let { priceStrToFloatCarrefour(it) }
    var globalMinPrice = globalPrice
    if (offers != null) {
        offers.forEach { element ->
            val next_price = element.string("price")?.let { priceStrToFloatCarrefour(it) }
            if ((next_price != 0.0f) and (next_price!! < globalMinPrice!!))
                globalMinPrice = next_price



        }
    }
    var subProducto = SubProduct(identifier = "",
        price = globalPrice?:0.0f,
        globalMinPrice = globalMinPrice?:0.0f,
        src_image = "",
        aditional_title = ""
    )
    listOfSubProducto.add(subProducto)
    Log.d("parsing","Subproductos: ${listOfSubProducto}")

    return ScrapProduct(title = tituloGlobal, src_image = imagenGlobal,0,currency ,listOfSubProducto)

}

fun priceStrToFloatCarrefour(price_str:String):Float
{
    // Definir la expresión regular para eliminar todo lo que no sea un número, una coma o un punto
    val cleanedString = price_str.replace("[^0-9,]".toRegex(), "")

    // Reemplazar la coma con un punto
    val formattedString = cleanedString.replace(",", ".")

    // Convertir el string a float
    return formattedString.toFloat()
}