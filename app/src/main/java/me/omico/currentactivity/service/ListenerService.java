package me.omico.currentactivity.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import me.omico.currentactivity.R;
import me.omico.currentactivity.ui.widget.TipViewController;
import me.omico.currentactivity.util.Util;

public final class ListenerService extends Service implements TipViewController.ViewDismissHandler {

    private TipViewController mTipViewController;
    private Handler handler = new Handler();
    private CharSequence LAST_CONTENT = null;
    private String ACTION_STOP_SERVICE = "me.omico.currentactivity.stop";
    private int NOTIFICATION_ID = 1080;
    boolean isStop = false;

    @Override
    public void onCreate() {
        startForeground(NOTIFICATION_ID, notificationMethod());
        showContent(setCurrentActivity());
        mTipViewController.setOnTipViewClickListener(new TipViewController.OnTipViewClickListener() {
            @Override
            public void OnTipViewClick() {
                isStop = true;
                handler.removeCallbacks(runnable);
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (ACTION_STOP_SERVICE.equals(action)) {
            isStop = true;
            handler.removeCallbacks(runnable);
            stopSelf();
        }

        handler.postDelayed(runnable, 500);

        updateNotification();

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateNotification() {
        if (!isStop)
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notificationMethod());
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            showContent(setCurrentActivity());
            updateNotification();
            handler.postDelayed(this, 500);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        LAST_CONTENT = null;
        if (mTipViewController != null) {
            mTipViewController.setViewDismissHandler(null);
            mTipViewController = null;
        }
        stopForeground(true);
    }

    private String setCurrentActivity() {
        return Util.getCurrentActivity();
    }

    private Notification notificationMethod() {
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, new Intent(this, ListenerService.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle("当前应用包名，点击查看详细")
                .setContentText(setCurrentActivity())
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        Intent exitIntent = new Intent(this, ListenerService.class).setAction(ACTION_STOP_SERVICE);
        builder.addAction(android.R.drawable.ic_delete, "Exit", PendingIntent.getService(this, 0, exitIntent, 0));

        return builder.build();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showContent(CharSequence content) {
        if (LAST_CONTENT != null && LAST_CONTENT.equals(content) || content == null) {
            return;
        }
        LAST_CONTENT = content;

        if (mTipViewController != null) {
            mTipViewController.updateContent(content);
        } else {
            mTipViewController = new TipViewController(getApplication(), content);
            mTipViewController.setViewDismissHandler(this);
            mTipViewController.show();
        }
    }

    @Override
    public void onViewDismiss() {
        LAST_CONTENT = null;
        mTipViewController = null;
        handler.removeCallbacks(runnable);
    }
}