package com.elsadig.multibankgroup.domain.usecase

import com.elsadig.multibankgroup.domain.repository.IStockRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class ManagePriceUpdateUseCase @Inject constructor(
    private val repository: IStockRepository,
    private val ioScope: CoroutineScope
) {
    private var feedJob: Job? = null

    fun start() {
        if (feedJob?.isActive == true) return
        repository.connect()

        feedJob = ioScope.launch {
            while (isActive) {
                repository.stockList.forEach { stockSymbol ->
                    repository.sendPriceUpdate(stockSymbol.price.copy(newPrice = generateRandomPrice()))
                }
                delay(2_000)
            }
        }
    }

    fun stop() {
        feedJob?.cancel()
        feedJob = null
        repository.disconnect()
    }

    private fun generateRandomPrice(): Double {
        return (100..500).random() + Random.nextDouble()
    }
}