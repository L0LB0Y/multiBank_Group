package com.elsadig.multibankgroup.ui.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.elsadig.multibankgroup.domain.repository.IStockRepository
import com.elsadig.multibankgroup.ui.model.PriceDirection
import com.elsadig.multibankgroup.ui.model.StockUiModel
import com.elsadig.multibankgroup.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: IStockRepository
) : ViewModel() {
    private val route = savedStateHandle.toRoute<Routes.DetailsRoute>()

    private var lastPriceCache = route.stockLastPrice
    private val stockSymbol = repository.stockList.find { it.stockId == route.stockID }?.let {
        StockUiModel(
            stockSymbol = it.copy(price = it.price.copy(newPrice = lastPriceCache)),
            priceDirection = route.priceDirection
        )
    }

    val uiState: StateFlow<StockUiModel?> =
        repository.priceUpdates.scan(initial = stockSymbol) { ui, priceUpdate ->

            if (priceUpdate.stockId == ui?.stockSymbol?.stockId) {

                val direction = when {
                    priceUpdate.newPrice > lastPriceCache -> PriceDirection.UP
                    priceUpdate.newPrice < lastPriceCache -> PriceDirection.DOWN
                    else -> PriceDirection.NONE
                }

                lastPriceCache = priceUpdate.newPrice

                ui.copy(
                    stockSymbol = ui.stockSymbol.copy(price = priceUpdate),
                    priceDirection = direction
                )

            } else ui

        }.conflate()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(500),
                initialValue = stockSymbol
            )

}