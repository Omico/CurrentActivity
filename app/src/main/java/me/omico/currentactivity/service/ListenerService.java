package me.omico.currentactivity.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import me.omico.currentactivity.R;
import me.omico.currentactivity.ui.widget.TipViewController;
import me.omico.currentactivity.util.Util;
import me.omico.util.ServiceUtils;

public final class ListenerService extends Service implements TipViewController.ViewDismissHandler {

    private TipViewController mTipViewController;
    private Handler handler = new Handler();
    private CharSequence LAST_CONTENT = null;

    @Override
    public void onCreate() {
        showContent(Util.getCurrentActivity());
        mTipViewController.setOnTipViewClickListener(new TipViewController.OnTipViewClickListener() {
            @Override
            public void OnTipViewClick() {
                notificationMethod();
                handler.removeCallbacks(runnable);
                ServiceUtils.stopService(ListenerService.this, ListenerService.this.getClass());
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.postDelayed(runnable, 500);
        return super.onStartCommand(intent, flags, startId);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            showContent(Util.getCurrentActivity());
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
    }

    public void notificationMethod() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, new Intent(this, ListenerService.class), 0);

        Notification notification = new Notification.Builder(this)
                .setAutoCancel(true)
                .setOngoing(false)
                .setOnlyAlertOnce(true)
                .setShowWhen(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("当前应用包名，点击查看详细")
                .setContentText(Util.getCurrentActivity())
                .setContentIntent(pendingIntent)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, notification);
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
    }
}