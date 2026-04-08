package com.elsadig.multibankgroup.ui.screens.feed

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elsadig.multibankgroup.domain.repository.IStockRepository
import com.elsadig.multibankgroup.ui.model.PriceDirection
import com.elsadig.multibankgroup.ui.model.StockUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(repository: IStockRepository) : ViewModel() {

    // Cache of last known price per symbol — used to determine direction
    private val lastPriceCache = mutableMapOf<Int, Double>()

    private val mappedStockUiModel = repository.stockList.map { StockUiModel(stockSymbol = it) }
    val uiState: StateFlow<FeedUiState> =
        repository.priceUpdates.scan(FeedUiState(stocks = mappedStockUiModel)) { ui, priceUpdate ->

            val previous = lastPriceCache[priceUpdate.stockId]

            val direction = when {
                previous == null -> PriceDirection.NONE
                priceUpdate.newPrice > previous -> PriceDirection.UP
                priceUpdate.newPrice < previous -> PriceDirection.DOWN
                else -> PriceDirection.NONE
            }

            // Update cache with the new price
            lastPriceCache[priceUpdate.stockId] = priceUpdate.newPrice
            val updatedStockList = ui.stocks.map { stock ->
                if (stock.stockSymbol.stockId == priceUpdate.stockId)
                    stock.copy(
                        stockSymbol = stock.stockSymbol.copy(price = priceUpdate),
                        priceDirection = direction
                    )
                else stock
            }.sortedByDescending { it.stockSymbol.price.newPrice }
            Log.d("lol", ": $priceUpdate")
            ui.copy(stocks = updatedStockList)
        }.flowOn(Dispatchers.IO)
            .conflate()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 60000),
                initialValue = FeedUiState(stocks = mappedStockUiModel)
            )

}

@Stable
data class FeedUiState(val stocks: List<StockUiModel> = emptyList())


