package com.elsadig.multibankgroup.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class StockSymbol(
    val stockId: Int,
    val symbol: String,
    val companyName: String,
    val price: PriceUpdate,
    val description: String
)
