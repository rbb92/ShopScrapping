package com.rbb92.cazachollo.scrapingTool

import android.util.Log
import com.rbb92.cazachollo.data.ScrapProduct
import com.rbb92.cazachollo.data.ScrapState
import com.rbb92.cazachollo.data.Store
import com.rbb92.cazachollo.data.SubProduct
import com.rbb92.cazachollo.utils.priceToFloat
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
                agent = Constants.USER_AGENT
            }
        }

        //make request
        try {
            Log.d("ablancom", "URL limpia 2: ${removerParametrosUrl(url)}")
            val urlClean = removerParametrosUrl(url)
            val response = androidClient.get(urlClean)

//            response.headers.forEach { s, strings ->
//                Log.d(s,strings.toString())
//            }


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
                val product = SubProduct(price = priceToFloat(price),
                                         globalMinPrice = priceToFloat(globalMinPrice))
                val subProduct = mutableListOf<SubProduct>()
                subProduct.add(product)

                //TODO insertar referido Aliexpress aqui
                ScrapState(
                    url = urlClean,
                    url_refered = urlClean,
                    store = Store.AMAZON,
                    product = ScrapProduct(
                        title = title,
                        src_image = urlImg,
                        subProduct = subProduct)
                    )

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


