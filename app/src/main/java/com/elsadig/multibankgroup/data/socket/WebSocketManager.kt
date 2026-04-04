package com.elsadig.multibankgroup.data.socket

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Singleton

@Singleton
class WebSocketManager(private val ioScope: CoroutineScope) {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    private val _incomingMessages = MutableSharedFlow<String>()
    // send back StockPriceDto as JSON
    val incomingMessages: SharedFlow<String> = _incomingMessages

    fun connect() {
        if (webSocket != null) return

        val request = Request.Builder()
            .url("wss://ws.postman-echo.com/raw")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                _connectionState.value = ConnectionState.CONNECTED
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                ioScope.launch { _incomingMessages.emit(text) }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                _connectionState.value = ConnectionState.DISCONNECTED
                webSocket.close(code, reason)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                _connectionState.value = ConnectionState.DISCONNECTED
                this@WebSocketManager.webSocket = null
            }
        })
    }

    // receive StockPriceDto as JSON
    fun send(message: String) {
        webSocket?.send(message)
    }

    fun disconnect() {
        webSocket?.close(1000, "User Manual close")
        webSocket = null
        _connectionState.value = ConnectionState.DISCONNECTED
    }
}

enum class ConnectionState {
    CONNECTED,
    DISCONNECTED
}