package me.omico.currentactivity.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import me.omico.currentactivity.R;
import me.omico.currentactivity.provider.Settings;
import me.omico.currentactivity.ui.fragment.MainFragment;
import me.omico.currentactivity.util.Util;
import me.omico.support.widget.floatwindow.FloatWindow;
import me.omico.util.ClipboardUtils;

import static me.omico.currentactivity.CurrentActivity.NOTIFICATION_ID;
import static me.omico.currentactivity.provider.Settings.ACTION_GESTURE_COPY;
import static me.omico.currentactivity.provider.Settings.ACTION_GESTURE_HIDE;
import static me.omico.currentactivity.provider.Settings.ACTION_STOP;
import static me.omico.currentactivity.provider.Settings.ENABLE_FLOAT_WINDOW;
import static me.omico.currentactivity.provider.Settings.GESTURE_CLICK;
import static me.omico.currentactivity.provider.Settings.GESTURE_LONG_PRESS;

/**
 * @author Omico
 */

public final class FloatViewService extends Service {

    private NotificationManager notificationManager;
    private FloatWindow mFloatWindow;
    private TextView tipTextView;
    private TextView mTextView;
    private View view;
    private Handler handler = new Handler();
    private boolean isStop = false;

    @Override
    public void onCreate() {
        notificationManager = ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationChannel();
        startForeground(NOTIFICATION_ID, notificationMethod());
        initFloatViewContent();
        initFloatView();
        setCurrentActivity();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (ACTION_STOP.equals(action)) {
            isStop = true;
            handler.removeCallbacks(runnable);
            Settings.putBoolean(ENABLE_FLOAT_WINDOW, false);
            stopSelf();
        }

        if (!isStop) mFloatWindow.show();

        handler.postDelayed(runnable, 500);

        updateNotification();
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateNotification() {
        if (!isStop) notificationManager.notify(NOTIFICATION_ID, notificationMethod());
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            setCurrentActivity();
            updateNotification();
            handler.postDelayed(this, 500);
        }
    };

    private void setCurrentActivity() {
        mTextView.setText(Util.getCurrentActivity(this));
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel mChannel = new NotificationChannel(getClass().getSimpleName(), getString(R.string.app_name), NotificationManager.IMPORTANCE_LOW);
        notificationManager.createNotificationChannel(mChannel);
    }

    private Notification notificationMethod() {
        Notification notification;

        PendingIntent pendingIntent = PendingIntent.getService(this, 0, new Intent(this, FloatViewService.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getClass().getSimpleName())
                .setContentTitle("当前应用包名，点击查看详细")
                .setContentText(Util.getCurrentActivity(this))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher);

        Intent exitIntent = new Intent(this, FloatViewService.class).setAction(ACTION_STOP);
        builder.addAction(R.drawable.ic_action_exit, getString(R.string.notification_action_exit), PendingIntent.getService(this, 0, exitIntent, 0));

        notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;

        return notification;
    }

    /*Example:
    private WindowManager.LayoutParams setLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                getType(),
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);

        layoutParams.gravity = Gravity.TOP;
        return layoutParams;
    }
    */

    private void initFloatView() {
        mFloatWindow = new FloatWindow(this);

        /*Example:
        mFloatWindow.init(view, setLayoutParams()).attach();

        mFloatWindow.init(view, setLayoutParams(), Gravity.TOP).attach();

        mFloatWindow.init(view).setLayoutParams(setLayoutParams()).attach();

        mFloatWindow.init(view).setLayoutParams(setLayoutParams(), Gravity.TOP).attach();

        mFloatWindow.init(view, setLayoutParams()).setLayoutParamsGravity(Gravity.BOTTOM).attach();

        mFloatWindow
                .init(view)
                .setLayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        getType(),
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                        PixelFormat.TRANSLUCENT)
                .setLayoutParamsGravity(Gravity.TOP)
                .attach();
        */

        mFloatWindow
                .init(view)
                .setLayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        getType(),
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                        PixelFormat.TRANSLUCENT,
                        Gravity.TOP)
                .attach();

        mFloatWindow.setOnFloatWindowClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGestureAction(GESTURE_CLICK);
            }
        });

        mFloatWindow.setOnFloatWindowLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                loadGestureAction(GESTURE_LONG_PRESS);
                return true;
            }
        });
    }

    private void loadGestureAction(String key) {
        switch (Settings.getString(key, "")) {
            case ACTION_GESTURE_HIDE:
                mFloatWindow.hide();
                break;
            case ACTION_GESTURE_COPY:
                ClipboardUtils.copyToClipboard(getApplicationContext(), mTextView.getText().toString());
                break;
        }
    }

    private int getType() {
        int type = WindowManager.LayoutParams.TYPE_PHONE;

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            type = WindowManager.LayoutParams.TYPE_TOAST;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }

        return type;
    }

    private void initFloatViewContent() {
        view = View.inflate(this, R.layout.float_view, null);
        tipTextView = view.findViewById(R.id.float_view_tip);
        mTextView = view.findViewById(R.id.float_view_text);
        view.setSystemUiVisibility(
                view.getSystemUiVisibility()
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );

        handler.postDelayed(
                new Runnable() {
                    public void run() {
                        tipTextView.setVisibility(View.GONE);
                    }
                },
                5000
        );
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isStop = true;
        stopForeground(true);
        notificationManager.cancel(NOTIFICATION_ID);
        mFloatWindow.detach();
        MainFragment.enableFloatWindowPreference.setChecked(false);
    }
}