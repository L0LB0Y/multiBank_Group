package com.elsadig.multibankgroup.data.repository

import com.elsadig.multibankgroup.data.socket.WebSocketManager
import com.elsadig.multibankgroup.domain.entity.PriceUpdate
import com.elsadig.multibankgroup.domain.repository.IStockRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.shareIn
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val webSocketClient: WebSocketManager,
    ioScope: CoroutineScope
) : IStockRepository {

    // Single shared flow — both screens collect this without duplicate connections
    override val priceUpdates: SharedFlow<PriceUpdate> = webSocketClient.incomingMessages
        .mapNotNull { raw -> parsePriceJson(raw) }
        .shareIn(ioScope, SharingStarted.WhileSubscribed(stopTimeoutMillis = 500), replay = 1)


    override val connectionState: StateFlow<Boolean> = webSocketClient.connectionState

    override fun connect() = webSocketClient.connect()

    override fun disconnect() = webSocketClient.disconnect()

    override fun sendPriceUpdate(priceUpdate: PriceUpdate) {
        val json = Json.encodeToString(priceUpdate)
        webSocketClient.send(json)
    }

    private fun parsePriceJson(raw: String): PriceUpdate? = try {
        Json.decodeFromString<PriceUpdate>(raw)
    } catch (e: Exception) {
        null
    }
}