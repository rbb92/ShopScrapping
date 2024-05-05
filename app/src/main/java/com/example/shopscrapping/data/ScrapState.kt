package com.example.shopscrapping.data

data class CurrentProduct(
    val url: String = "",
    val url_refered: String = "",
    val store: Store = Store.AMAZON,
    val price: Float = 0.0f,
    val currency: String = "EUR",
    val globalMinPrice: Float? = null,
    val title: String = "",
    val description: String = "",
    val src_image_main: String = "",
    val src_image_sec: String? = "",
    val product_id: String = "")
{
}

data class ScrapState(
    val url: String = "",       //todos los subproductos estaran en la misma url
    val url_refered: String = "",
    val store: Store = Store.AMAZON,
    var product: ScrapProduct? = null,
    val isScrapping: Boolean = false,
    val isError: Boolean = false,
    val isScrappingProcess: Boolean = false)
{
}

data class ScrapProduct( //TODO -> incluir moneda aqui
    val title: String? = "",
    val src_image: String? = "",
    val subProductSelected: Int = 0,
    val currency: String = "EUR",
    val subProduct: List<SubProduct>)
{
}
//TODO -> diferenciar engtre reference e identifier, en principio identifier tendra el identificador para distirnguir el subproducto en cada scrap
data class SubProduct(
    var aditional_title: String? = null,
    var identifier: String? = null,
    var reference: String? = null,
    var price: Float = 0.0f,
    var globalMinPrice: Float? = null,
    var src_image: String? = null,
)
{

}