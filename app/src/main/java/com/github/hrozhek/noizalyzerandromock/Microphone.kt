package com.github.hrozhek.noizalyzerandromock

import android.Manifest
import android.content.Context
import com.github.hrozhek.noizalyzerandromock.Microphone
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Process
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.Exception
import java.lang.RuntimeException
import java.time.Duration
import java.time.Instant

class Microphone(private val context: Context, private val duration: Duration) {
    fun read(): ByteArray {
        Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
        val audioData = ByteArray(BUFFER_SIZE)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("micro not permitted!!!")
        }
        val recorder = AudioRecord(
            AUDIO_SOURCE,
            SAMPLING_RATE, CHANNEL_IN_CONFIG,
            AUDIO_FORMAT, BUFFER_SIZE
        )
        recorder.startRecording()
        var out: ByteArrayOutputStream? = null
        return try {
            out = ByteArrayOutputStream()
            println("start reading!") //todo window
            val end = Instant.now().plus(duration)
            while (Instant.now().isBefore(end)) {
                val numBytesRead = recorder.read(audioData, 0, audioData.size)
                out.write(audioData, 0, numBytesRead)
            }
            println("stop reading!!!") //todo window
            out.toByteArray()
        } catch (e: Exception) {
            throw RuntimeException(e) //TODO
        } finally {
            if (out != null) {
                try {
                    out.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            recorder.stop()
            recorder.release()
        }
    }

    companion object {
        private const val SAMPLING_RATE = 8000
        private const val AUDIO_SOURCE = MediaRecorder.AudioSource.MIC
        private const val CHANNEL_IN_CONFIG = AudioFormat.CHANNEL_IN_MONO
        private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
        private val BUFFER_SIZE =
            AudioRecord.getMinBufferSize(SAMPLING_RATE, CHANNEL_IN_CONFIG, AUDIO_FORMAT)
    }
}