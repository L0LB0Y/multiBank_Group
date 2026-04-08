package com.elsadig.multibankgroup.data.socket

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketManager @Inject constructor(
    private val ioScope: CoroutineScope
) {

    private val TAG = this::class.java.simpleName

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    private val _connectionState = MutableStateFlow(false)
    val connectionState: StateFlow<Boolean> = _connectionState

    private val _incomingMessages = MutableSharedFlow<String>()
    val incomingMessages: SharedFlow<String> = _incomingMessages

    fun connect() {
        if (webSocket != null) {
            Log.d(TAG, "connect() ignored: already connected")
            return
        }

        Log.d(TAG, "Connecting to WebSocket...")

        val request = Request.Builder()
            .url("wss://echo.websocket.org") // replaced the socket url cus postman one keep drooping down
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(TAG, "WebSocket connected")
                _connectionState.value = true
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d(TAG, "Message received: $text")
                ioScope.launch {
                    _incomingMessages.emit(text)
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "WebSocket closing: code=$code, reason=$reason")
                _connectionState.value = false
                webSocket.close(code, reason)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "WebSocket error: ${t.message}", t)
                _connectionState.value = false
                this@WebSocketManager.webSocket = null
                reconnect()
            }
        })
    }

    private fun reconnect() {
        ioScope.launch {
            delay(2000)
            Log.d(TAG, "Reconnecting WebSocket...")
            connect()
        }
    }

    fun send(message: String) {
        if (_connectionState.value) {
            Log.d(TAG, "Sending message: $message")
            webSocket?.send(message)
        } else {
            Log.d(TAG, "Send skipped: not connected")
        }
    }

    fun disconnect() {
        Log.d(TAG, "Disconnecting WebSocket")
        webSocket?.close(1000, "User Manual close")
        webSocket = null
        _connectionState.value = false
    }
}