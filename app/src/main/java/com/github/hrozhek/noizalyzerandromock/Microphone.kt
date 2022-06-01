package com.github.hrozhek.noizalyzerandromock;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class Microphone {

    private static final int SAMPLING_RATE = 8000;
    private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    private static final int CHANNEL_IN_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLING_RATE, CHANNEL_IN_CONFIG, AUDIO_FORMAT);

    private final Context context;
    private final Duration duration;

    public Microphone(Context context, Duration duration) {
        this.context = context;
        this.duration = duration;
    }

    public byte[] read() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

        byte audioData[] = new byte[BUFFER_SIZE];
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("micro not permitted!!!");
        }
        AudioRecord recorder = new AudioRecord(AUDIO_SOURCE,
                SAMPLING_RATE, CHANNEL_IN_CONFIG,
                AUDIO_FORMAT, BUFFER_SIZE);
        recorder.startRecording();

        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            System.out.println("start reading!");//todo window
            Instant end = Instant.now().plus(duration);
            while (Instant.now().isBefore(end)) {
                int numBytesRead = recorder.read(audioData, 0, audioData.length);
                out.write(audioData, 0, numBytesRead);
            }
            System.out.println("stop reading!!!");//todo window
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);//TODO
        }
        finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            recorder.stop();
            recorder.release();
        }
    }
}
