package com.elsadig.multibankgroup.ui.navigation

import com.elsadig.multibankgroup.ui.model.PriceDirection
import kotlinx.serialization.Serializable

object Routes {
    @Serializable
    object FeedRoute

    @Serializable
    data class DetailsRoute(val stockID: Int, val stockLastPrice: Double,val priceDirection: PriceDirection)
}