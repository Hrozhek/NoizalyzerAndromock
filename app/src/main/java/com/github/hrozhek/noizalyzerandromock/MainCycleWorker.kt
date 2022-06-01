package com.github.hrozhek.noizalyzerandromock

import android.app.AlertDialog
import android.content.Context
import com.github.hrozhek.noizalyzerandromock.AppContext.Companion.instance
import androidx.core.content.ContextCompat
import java.lang.Exception
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.ArrayList

class MainCycleWorker(private val context: Context) : Runnable {
    private val appContext = instance
    private var client: ConnectionClient? = null
    private var microphone: Microphone? = null
    private var ws: WebSocket? = null
    override fun run() {
        val dialogs: MutableList<AlertDialog> = ArrayList()
        try {
            ContextCompat.getMainExecutor(context).execute {
                dialogs.add(
                    AlertDialog.Builder(
                        context
                    )
                        .setTitle("reading mic")
                        .setMessage("reading mic").show()
                )
            }
            val read = microphone!!.read()
            ContextCompat.getMainExecutor(context).execute {
                dialogs.stream().filter { obj: AlertDialog -> obj.isShowing }
                    .forEach { obj: AlertDialog -> obj.dismiss() }
            }
            doWs(read)
        } catch (e: Exception) {
            println(e) //TODO
        } finally {
            ContextCompat.getMainExecutor(context).execute {
                for (dialog in dialogs) {
                    if (dialog.isShowing) {
                        dialog.dismiss()
                    }
                }
            }
        }
    }

    private fun doWs(read: ByteArray) {
        if (ws == null) {
            val link = client!!.getWsLink(appContext.id)
            ws = WebSocket(appContext.server!!, appContext.port, appContext.endpoint!!, link)
        }
        try {
            ws!!.sendData(read)
        } catch (e: Exception) {
            println(e) //TODO
            try {
                ws!!.sendData(read)
            } catch (ee: Exception) {
                println("tried twice, not successful$e")
                ws = null
            }
        }
    }

    fun init() {
        client = appContext.connectionClient
        if (appContext.id == null) {
            appContext.id = client!!.initConnection()
        }
        val timeout = appContext.readTimeout
        val duration = Duration.of(
            timeout!!.time, ChronoUnit.valueOf(
                timeout.timeUnit.toString()
            )
        )
        microphone = Microphone(context, duration)
    }
}