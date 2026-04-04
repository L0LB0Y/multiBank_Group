package com.elsadig.multibankgroup.data.model

import kotlinx.serialization.Serializable

@Serializable
data class StockPriceDto(
    val id: Int,
    val symbol: String,
    val price: Double,
)