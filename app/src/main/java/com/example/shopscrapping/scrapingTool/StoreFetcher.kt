package com.example.shopscrapping.scrapingTool

import com.example.shopscrapping.data.ScrapState
import com.example.shopscrapping.data.Store

suspend fun StoreFetcher (url: String, store:Store): ScrapState =
    when (store)
    {
        Store.AMAZON -> AmazonFetcher(url)
        Store.NULL -> AmazonFetcher(url)
    }