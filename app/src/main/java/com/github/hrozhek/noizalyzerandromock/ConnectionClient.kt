package com.github.hrozhek.noizalyzerandromock

import okhttp3.*
import java.lang.Exception
import java.lang.RuntimeException
import java.util.*

class ConnectionClient(
    private val address: String,
    private val port: Int,
    private val endpoint: String
) {
    private val client = OkHttpClient()
    fun initConnection(): UUID {
        val empty: RequestBody = FormBody.Builder().build()
        val request: Request = Request.Builder()
            .url(formatRequest("controller"))
            .post(empty)
            .build()
        val response: Response
        response = try {
            client.newCall(request).execute()
        } catch (e: Exception) {
            throw RuntimeException(e) //todo
        }
        println("Controller registered! " + response + " body: " + response.body)
        var uuid: String
        try {
            uuid = response.body!!.string()
            uuid = uuid.substring(uuid.indexOf("\"") + 1, uuid.length - 1)
            println("uuid: $uuid")
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        return UUID.fromString(uuid)
    }

    private fun formatRequest(path: String): String {
        return String.format("http://%s:%d/%s/%s", address, port, endpoint, path)
    }

    fun getWsLink(id: UUID?): String {
        val path = String.format("controller/%s/file", id)
        val empty: RequestBody = FormBody.Builder().build()
        val request: Request = Request.Builder()
            .url(formatRequest(path))
            .post(empty)
            .build()
        val response: Response
        response = try {
            client.newCall(request).execute()
        } catch (e: Exception) {
            throw RuntimeException(e) //todo
        }
        println("Controller registered! " + response + " body: " + response.body)
        val ws: String
        try {
            ws = response.body!!.string()
            println("ws: $ws")
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        return ws
    }
}