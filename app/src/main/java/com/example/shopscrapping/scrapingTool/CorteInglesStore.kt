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


suspend fun CorteInglesStore(url_: String): ScrapState =
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
                agent = Constants.USER_AGENT
            }

        }

        //make request
        try {
            //TODO limpiar URL
            val response = androidClient.get(url_)

            val jsonContent = extractJsonResponseCorteIngles(response.bodyAsText())?:""
            val scrapedProduct = parseJsonResponseCorteIngles(jsonContent)

            ScrapState(url = url_, url_refered = url_, store = Store.CORTEINGLES, product = scrapedProduct)

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




fun extractJsonResponseCorteIngles(body: String): String? {
    val prefijo = "ld+json"
    var startIndex = 0
    while(startIndex != 1)
    {
        Log.d("ablancom","Indice ${startIndex}")
         startIndex = body.indexOf(prefijo,startIndex)+2
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
            if(body.substring(jsonStringStartIndex, endIndex + 1).contains("Product"))
                return body.substring(jsonStringStartIndex, endIndex + 1)

        }

    }
    return null
}

fun parseJsonResponseCorteIngles(jsonResponse : String): ScrapProduct {
    val parser: Parser = Parser.default()
    val stringBuilder: StringBuilder = StringBuilder(jsonResponse)
    val json: JsonObject = parser.parse(stringBuilder) as JsonObject

    //extraemos titulo global
    val tituloGlobal = json.string("name")?:""

    //extraemos imagen global
    val imagenGlobal_base  = json.string("image")?:""

    val unicodeRegex = """\\u([0-9A-Fa-f]{4})""".toRegex()
    val regexSizeImage = """w-(\d+)-\1""".toRegex()
    // Replace each Unicode escape with the corresponding character

    val imagenGlobal = unicodeRegex.replace(imagenGlobal_base) {
        val charCode = it.groupValues[1].toInt(16)
        charCode.toChar().toString()
    }

    val offers = try {
        json.obj("offers")
    }
    catch (exc:Exception){
        json.array<JsonObject>("offers")?.get(0)
    }
    val currency = offers?.string("priceCurrency") ?:"EUR"

    var globalPrice: Number
    var globalMinPrice: Number
    try {
        globalPrice = offers?.int("price") ?: 0
    }
    catch(exc:Exception){
        globalPrice = offers?.float("price") ?: 0.0f
    }

    try {
        globalMinPrice = offers?.int("lowPrice") ?: 0
    }
    catch(exc:Exception){
        globalMinPrice = offers?.float("lowPrice") ?: 0.0f
    }
    Log.d("parsing","Titulo general: ${tituloGlobal}")
    Log.d("parsing","Imagen general: ${imagenGlobal}")

    val listOfSubProducto = mutableListOf<SubProduct>()

    //se podria ignorar esta parte, no es seguro saber el stock a partir de este atributo
    if ((offers?.string("availability")?.contains("instock",true)?:true) == false)
    {
        globalPrice = 0
        globalMinPrice = 0
    }
//    val additional_title = json.string("description")?:""


    val subProducto = SubProduct(identifier = "",
        price = globalPrice.toFloat(),
        globalMinPrice = globalMinPrice.toFloat(),
        src_image = "",
        aditional_title = ""
    )
    listOfSubProducto.add(subProducto)
    Log.d("parsing","Subproductos: ${listOfSubProducto}")

    return ScrapProduct(title = tituloGlobal, src_image = imagenGlobal,0,currency ,listOfSubProducto)

}
