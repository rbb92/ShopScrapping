package com.example.shopscrapping.scrapingTool

import android.util.Log
import com.example.shopscrapping.data.ScrapState
import com.example.shopscrapping.utils.priceToFloat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import it.skrape.core.htmlDocument


import io.ktor.client.*
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.UserAgent
import io.ktor.client.request.*
import io.ktor.client.statement.*
import it.skrape.selects.and
import it.skrape.selects.html5.img
import it.skrape.selects.html5.span


suspend fun AmazonFetcher(url: String): ScrapState =
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
                agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:124.0) Gecko/20100101 Firefox/124.0"
            }
        }

        //make request
        try {
            val response = androidClient.get(url)

//            response.headers.forEach { s, strings ->
//                Log.d(s,strings.toString())
//            }

            Log.d("ablancom", "URL limpia 1: ${extraerDominio(url)}/dp/${extraerPathProducto(url)}")
            Log.d("ablancom", "URL limpia 2: ${removerParametrosUrl(url)}")

            htmlDocument(response.bodyAsText())
            {

                val title = span {
                    withId = "productTitle"
                    findFirst {
                        Log.d("title", text)
                        text
                    }
                }
//                val get_all_spans = span {
//                    findAll {
//                        map{
//                            Log.d("elemento","class: ${it.className}, id: ${it.id} --> ${it.text}")
////                            Log.d("id",it.id)
////                            Log.d("texto",it.text)
//                            it.id
//                        }
//                    }
//                }

                val price = try
                {
                    //productos generales
                    span {
                        withClass = "a-price" and  "aok-align-center"
                        span {
                            withClass = "a-offscreen"
                            findFirst{
                                Log.d("precio1",text)
                                text
                            }
                        }
                    }
                } catch (e: Exception) {
//                    libros
                    try {
                        span {
                            withId = "price"
                            withClass = "a-size-medium" and "a-color-price"
                            findFirst {
                                Log.d("precio2", text)
                                text
                            }
                        }
                    }
                    catch (e: Exception) {
                        try {
                            span {
                                withClass = "a-price" and "apexPriceToPay"
                                span {
                                    withClass = "a-offscreen"
                                    findFirst {
                                        Log.d("precio3", text)
                                        text
                                    }
                                }
                            }
                        }
                        catch (e: Exception){
                            "0,0€"
                        }
                    }
                }

                val globalMinPrice = try
                {
                    //productos generales
                    span {
                        withClass = "a-price" and  "aok-align-center"
                        span {
                            withClass = "a-offscreen"
                            findAll{
                                var min_price=""
                                map{
                                    val it_price_float = priceToFloat(it.text)
                                    val min_price_float = priceToFloat(min_price)

                                    Log.d("ablancom","it_price_float ${it_price_float}, min_price_float ${min_price_float}")
                                    if(min_price_float == 0.0f)
                                        min_price = it.text
                                    else
                                    if(it_price_float != 0.0f)
                                        if(it_price_float < min_price_float && min_price_float != 0.0f)
                                            min_price = it.text

                                    Log.d("ablancom","min_price ${min_price}, it.text ${it.text}")
                                }
                                Log.d("ablancom","precio minimo ${min_price}")
                                min_price
                                
                            }
                        }
                    }
                } catch (e: Exception) {
                    // TODO Resto productos, oobtener todos los precios y sacar el minimo        
                    "0,0f"
                }

                val urlImg = img {
                    withId = "landingImage"
                    findFirst {
                        Log.d("urlImg", attribute("src"))
                        attribute("src")
                    }
                }
                ScrapState(
                    url = url,
                    price = price,
                    globalMinPrice = globalMinPrice,
                    title = title,
                    description = "",
                    src_image = urlImg)
            }
        }catch (exc:Exception){
            exc.printStackTrace()
            Log.d("ablancom",exc.toString())
            ScrapState(
                url=url,
                isError = true)
        }finally {
            androidClient.close()
        }

//        Log.d("ablancom", "after response")
//        Log.d("ablancom", response.bodyAsText().takeLast(100000))
    }


fun extraerPathProducto(url: String): String? {
    val regex = Regex("/d?p/(.*?)(?:/|\\?)")
    val matchResult = regex.find(url)
    return matchResult?.groupValues?.get(1)
}


