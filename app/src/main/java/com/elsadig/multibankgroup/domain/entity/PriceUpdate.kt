package com.elsadig.multibankgroup.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class PriceUpdate(val stockId: Int, val newPrice: Double)
