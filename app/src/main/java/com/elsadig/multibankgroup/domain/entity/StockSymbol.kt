package com.elsadig.multibankgroup.domain.entity

data class StockSymbol(
    val stockId: Int,
    val symbol: String,
    val companyName: String,
    val price: PriceUpdate,
    val description: String
)
