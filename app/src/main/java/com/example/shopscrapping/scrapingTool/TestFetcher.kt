package com.example.shopscrapping.scrapingTool


import android.util.Log
import com.gargoylesoftware.htmlunit.*
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.util.Cookie
import com.gargoylesoftware.htmlunit.util.NameValuePair
import it.skrape.fetcher.BlockingFetcher
import it.skrape.fetcher.Method
import it.skrape.fetcher.Request
import it.skrape.fetcher.Result
import it.skrape.fetcher.toCookie
import it.skrape.fetcher.urlOrigin
import java.net.Proxy
import java.net.URL

public object TestFetcher : BlockingFetcher<Request> {
    override val requestBuilder: Request get() = Request()

    override fun fetch(request: Request): Result {
        val client = WebClient(BrowserVersion.BEST_SUPPORTED).withOptions(request)
        client.options.isJavaScriptEnabled = true
        client.cookieManager.isCookiesEnabled = true
        val webRequest = WebRequest(URL(request.url), request.method.toHttpMethod())


        val only_cookies: Page = client.getPage(client.currentWindow.topWindow, webRequest)



        val cookies: Set<Cookie> = client.getCookieManager().getCookies()
        val cookieHeader = StringBuilder() // Inicializa la variable como un StringBuilder

        for (cookie in cookies) {
            cookieHeader.append(cookie.name).append("=").append(cookie.value).append("; ") // Usa append para construir la cadena
            Log.d("COOKIES", cookie.name + "=" + cookie.value)
        }
        cookieHeader.delete(cookieHeader.length - 2, cookieHeader.length)

        val webRequest2 = WebRequest(URL(request.url), request.method.toHttpMethod())
        webRequest2.setAdditionalHeader("Cookie", cookieHeader.toString())
        val page: Page = client.getPage(client.currentWindow.topWindow, webRequest2)

        val cookies2: Set<Cookie> = client.getCookieManager().getCookies()
        for (cookie in cookies2) {
            Log.d("COOKIES2", cookie.name + "=" + cookie.value)
        }


        val httpResponse = page.webResponse
        val document = when {
            page.isHtmlPage -> (page as HtmlPage).asXml()
            else -> httpResponse.contentAsString
        }
        val headers = httpResponse.responseHeaders.toMap()

        val result = Result(
            responseBody = document,
            responseStatus = httpResponse.toStatus(),
            contentType = httpResponse.contentType,
            headers = headers,
            baseUri = request.url,
            cookies = httpResponse.responseHeaders
                .filter { it.name == "Set-Cookie" }
                .map { it.value.toCookie(request.url.urlOrigin) }
        )

        client.javaScriptEngine.shutdown()
        client.close()
        client.cache.clear()
        return result
    }

    public fun render(html: String): String =
        WebClient(BrowserVersion.BEST_SUPPORTED).loadHtmlCodeIntoCurrentWindow(html).asXml()

    private fun WebClient.withOptions(request: Request) = apply {
        cssErrorHandler = SilentCssErrorHandler()
        ajaxController = NicelyResynchronizingAjaxController()
        createCookies(request)
        addRequestHeader("User-Agent", request.userAgent)
        if (request.authentication != null) {
            addRequestHeader("Authorization", request.authentication!!.toHeaderValue())
        }
        request.headers.forEach {
            addRequestHeader(it.key, it.value)
        }
        @Suppress("MagicNumber") waitForBackgroundJavaScript(10_000)
        options.apply {
            timeout = request.timeout
            isRedirectEnabled = request.followRedirects
            maxInMemory = 0
            isUseInsecureSSL = request.sslRelaxed
            isCssEnabled = false
            isPopupBlockerEnabled = true
            isDownloadImages = false
            isThrowExceptionOnScriptError = false
            isThrowExceptionOnFailingStatusCode = false
            isPrintContentOnFailingStatusCode = false
            historySizeLimit = 0
            historyPageCacheLimit = 0
            withProxySettings(request)
        }
    }

    private fun WebClientOptions.withProxySettings(request: Request): WebClientOptions {
        if (request.proxy != null) {
            this.proxyConfig = ProxyConfig(
                request.proxy!!.host,
                request.proxy!!.port,
                "http",
                request.proxy!!.type == Proxy.Type.SOCKS
            )
        }
        return this
    }

    private fun WebClient.createCookies(request: Request) {
        request.cookies.forEach { cookieManager.addCookie(createCookie(request.url, it.key, it.value)) }
    }

    private fun createCookie(url: String, name: String, value: String): Cookie {
        val domain = URL(url).host
        return Cookie(domain, name, value)
    }

    private fun WebResponse.toStatus() = Result.Status(statusCode, statusMessage)

}


public fun Map<String, String>.asRawCookieSyntax(): String =
    entries.joinToString(";", postfix = ";") { "${it.key}=${it.value}" }

public fun List<NameValuePair>.toMap(): Map<String, String> =
    associateBy({ it.name }, { it.value })

internal fun Method.toHttpMethod(): HttpMethod = when (this) {
    Method.GET -> HttpMethod.GET
    Method.POST -> HttpMethod.POST
    Method.HEAD -> HttpMethod.HEAD
    Method.DELETE -> HttpMethod.DELETE
    Method.PATCH -> HttpMethod.PATCH
    Method.PUT -> HttpMethod.PUT
}
