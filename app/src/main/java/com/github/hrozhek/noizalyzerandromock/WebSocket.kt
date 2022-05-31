package com.github.hrozhek.noizalyzerandromock

import tech.gusavila92.websocketclient.WebSocketClient
import java.lang.Exception
import java.lang.RuntimeException
import java.net.URI
import java.net.URISyntaxException

class WebSocket(address: String, port: Int, endpoint: String, ws: String) {

    private val webSocketClient: WebSocketClient

    fun sendData(data: ByteArray?) {
        webSocketClient.send(data)
    }

    private fun createClient(uri: URI): WebSocketClient {
        return object : WebSocketClient(uri) {
            override fun onOpen() {
                println("onOpen")
                webSocketClient.send("Hello, World!")
            }

            override fun onTextReceived(message: String) {
                println("onTextReceived")
            }

            override fun onBinaryReceived(data: ByteArray) {
                println("onBinaryReceived")
            }

            override fun onPingReceived(data: ByteArray) {
                println("onPingReceived")
            }

            override fun onPongReceived(data: ByteArray) {
                println("onPongReceived")
            }

            override fun onException(e: Exception) {
                println(e.message)
            }

            override fun onCloseReceived() {
                println("onCloseReceived")
            }
        }
    }

    init {
        webSocketClient = createClient(formatAddress(address, port, endpoint, ws))
//        webSocketClient.enableAutomaticReconnection(5000)
        webSocketClient.connect()
    }

    private fun formatAddress(address: String, port: Int, endpoint: String, ws: String): URI {
        val uri = String.format("ws://%s:%d/%s/%s/0", address, port, endpoint, ws)
        return try {
            URI(uri)
        } catch (e: URISyntaxException) {
            throw RuntimeException(e) //TODO
        }
    }
}