package com.example.shopscrapping.data

data class ScrapWorkDescription(
    val url: String,
    val store: Store,
    val isStock: Boolean,
    val priceLimit: Float,
    val period: Period

)

enum class Period(val minutes: Long)
{
    MIN_15(15),
    HOUR_1(60),
    HOUR_4(240),
    HOUR_6(360),
    HOUR_12(720),
    DAY_1(1440),
    DAY_2(1440*2),
    DAY_3(1440*3),
    DAY_4(1440*4),
    DAY_5(1440*5),
    DAY_6(1440*6),
    DAY_7(1440*7)
}