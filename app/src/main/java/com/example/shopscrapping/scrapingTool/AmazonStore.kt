package com.example.shopscrapping.scrapingTool

import android.util.Log
import com.example.shopscrapping.viewmodel.ScrapUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import it.skrape.core.htmlDocument
import it.skrape.fetcher.AsyncFetcher
import it.skrape.fetcher.BlockingFetcher
import it.skrape.fetcher.BrowserFetcher
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.extractBlocking
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.fetcher.Result
import it.skrape.fetcher.Cookie


import io.ktor.client.*
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.BrowserUserAgent
import io.ktor.client.plugins.UserAgent
import io.ktor.http.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.flattenEntries
import io.ktor.util.toMap
import io.ktor.utils.io.core.*
import it.skrape.fetcher.NonBlockingFetcher
import it.skrape.fetcher.Request
import it.skrape.fetcher.*
import it.skrape.selects.and
import it.skrape.selects.attribute
import it.skrape.selects.html5.div
import it.skrape.selects.html5.img
import it.skrape.selects.html5.span
import it.skrape.selects.text
import kotlinx.coroutines.runBlocking




suspend fun AmazonFetcher(url: String): ScrapUiState =
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
                agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:121.0) Gecko/20100101 Firefox/121.0"
            }
        }

        //make request
        try {
            val response = androidClient.get(url)

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
                            "No disponible"
                        }
                    }
                }

                val url_img = img {
                    withId = "landingImage"
                    findFirst {
                        Log.d("url_img", attribute("src"))
                        attribute("src")
                    }
                }
                ScrapUiState(
                    url = url,
                    price = price,
                    title = title,
                    description = "",
                    src_image = url_img)
            }
        }catch (exc:Exception){
            exc.printStackTrace()
            Log.d("ablancom",exc.toString())
            ScrapUiState(
                url=url,
                isError = true)
        }finally {
            androidClient.close()
        }

//        Log.d("ablancom", "after response")
//        Log.d("ablancom", response.bodyAsText().takeLast(100000))
    }





