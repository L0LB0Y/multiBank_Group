package com.elsadig.multibankgroup.ui.model

import androidx.compose.runtime.Stable
import com.elsadig.multibankgroup.domain.entity.StockSymbol

@Stable
data class StockUiModel(
    val stockSymbol: StockSymbol,
    val priceDirection: PriceDirection = PriceDirection.NONE
)