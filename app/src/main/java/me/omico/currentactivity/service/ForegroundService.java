package me.omico.currentactivity.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.os.Build;

import androidx.annotation.NonNull;

/**
 * @author Omico 2018/12/02
 */
public abstract class ForegroundService extends Service {

    public abstract int getForegroundServiceNotificationId();

    public abstract Notification onStartForeground();

    @TargetApi(Build.VERSION_CODES.O)
    public abstract void onCreateNotificationChannel(@NonNull NotificationManager notificationManager);

    public void onStopForeground() {
        stopForeground(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) onCreateNotificationChannel(notificationManager);
        }

        startForeground(getForegroundServiceNotificationId(), onStartForeground());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onStopForeground();
    }
}
