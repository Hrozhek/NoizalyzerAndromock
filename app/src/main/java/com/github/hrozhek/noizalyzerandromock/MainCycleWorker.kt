package com.github.hrozhek.noizalyzerandromock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import androidx.core.content.ContextCompat;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class MainCycleWorker implements Runnable {

    private final AppContext appContext = AppContext.getAppCon();
    private final Context context;
    private ConnectionClient client;
    private Microphone microphone;
    private WebSocket ws;

    public MainCycleWorker(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        List<AlertDialog> dialogs = new ArrayList<>();
        try {
            ContextCompat.getMainExecutor(context).execute(()  -> {
                dialogs.add(new AlertDialog.Builder(context)
                        .setTitle("reading mic")
                        .setMessage("reading mic").show());
                    });

            byte read[] = microphone.read();
            ContextCompat.getMainExecutor(context).execute(()  -> {
                dialogs.stream().filter(AlertDialog::isShowing).forEach(AlertDialog::dismiss);
            });
            ContextCompat.getMainExecutor(context).execute(()  -> {
                dialogs.add(new AlertDialog.Builder(context)
                        .setTitle("writing through ws")
                        .setMessage("writing through ws").show());
            });
//            new AlertDialog.Builder(context)
//                    .setTitle("writing through ws")
//                    .setMessage("writing through ws").show();
            doWs(read);
        } catch (Exception e) {
            System.out.println(e);//TODO
        } finally {
            ContextCompat.getMainExecutor(context).execute(()  -> {
                for (AlertDialog dialog: dialogs) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    private void doWs(byte[] read) {
        if (ws == null) {
            String link = client.getWsLink(appContext.getId());
            ws = new WebSocket(appContext.getServer(), appContext.getPort(), appContext.getEndpoint(), link);
        }
        try {
            ws.sendData(read);
        } catch (Exception e) {
            System.out.println(e);//TODO
            try {
                ws.sendData(read);
            } catch (Exception ee) {
                System.out.println("tried twice, not successful" + e);
                ws = null;
            }
        }
    }

    public void init() {
        client = appContext.getConnectionClient();
        if (appContext.getId() == null) {
            appContext.setId(client.initConnection());
        }
        Timeout timeout = appContext.getReadTimeout();
        Duration duration = Duration.of(timeout.getTime(), ChronoUnit.valueOf(timeout.getTimeUnit().toString()));
        microphone = new Microphone(context, duration);
    }
}
