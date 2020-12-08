package com.rmh.nuk.internet.login;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.rmh.nuk.internet.R;

public class KeepAliveService extends Service {
    wifiChanged event;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("RMH", "Service started");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(event);
        stopForeground(true);
        stopSelf();
        System.exit(0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context context = this;
        Log.d("RMH", "Create Notification");
        Intent ri = new Intent(context, LoginActivity.class);
        Intent ri2 = new Intent(context, StopProcess.class);
        ri2.putExtra("service", intent);
        TaskStackBuilder tsb = TaskStackBuilder.create(context);
        tsb.addNextIntent(ri);
        PendingIntent pi = tsb.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pi2 = PendingIntent.getBroadcast(getApplication(), 2, ri2, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action nca = new NotificationCompat.Action.Builder(0, "Close", pi2).build();
        Notification notification = new NotificationCompat.Builder(context, "KeepAlive")
                .setContentTitle("NUK WIFI AUTO LOGIN")
                .setContentText("Keep Working.")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pi)
                .addAction(nca)
                .build();
        startForeground(1, notification);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        event = new wifiChanged();
        registerReceiver(event, intentFilter);
        return super.onStartCommand(intent, flags, startId);
    }
}
