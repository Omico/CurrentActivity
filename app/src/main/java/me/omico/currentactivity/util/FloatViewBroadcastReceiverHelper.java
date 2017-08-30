package me.omico.currentactivity.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import me.omico.util.LocalBroadcastUtils;

import static me.omico.currentactivity.provider.Settings.ACTION_FLOAT_VIEW_HIDE;
import static me.omico.currentactivity.provider.Settings.ACTION_FLOAT_VIEW_SERVICE_START;
import static me.omico.currentactivity.provider.Settings.ACTION_FLOAT_VIEW_SERVICE_STOP;
import static me.omico.currentactivity.provider.Settings.ACTION_FLOAT_VIEW_SHOW;

/**
 * @author Omico 2017/8/29
 */

public class FloatViewBroadcastReceiverHelper {

    private Context context;
    private BroadcastReceiver broadcastReceiver;
    private OnFloatViewStateChangedListener onFloatViewStateChangedListener;

    public FloatViewBroadcastReceiverHelper(Context context) {
        this.context = context;
    }

    public void register() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action != null && onFloatViewStateChangedListener != null) {
                    switch (action) {
                        case ACTION_FLOAT_VIEW_SERVICE_START:
                            onFloatViewStateChangedListener.onServiceStart();
                            break;
                        case ACTION_FLOAT_VIEW_SERVICE_STOP:
                            onFloatViewStateChangedListener.onServiceStop();
                            break;
                        case ACTION_FLOAT_VIEW_SHOW:
                            onFloatViewStateChangedListener.onShown();
                            break;
                        case ACTION_FLOAT_VIEW_HIDE:
                            onFloatViewStateChangedListener.onHidden();
                            break;
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_FLOAT_VIEW_SERVICE_START);
        intentFilter.addAction(ACTION_FLOAT_VIEW_SERVICE_STOP);
        intentFilter.addAction(ACTION_FLOAT_VIEW_SHOW);
        intentFilter.addAction(ACTION_FLOAT_VIEW_HIDE);
        LocalBroadcastUtils.register(context, broadcastReceiver, intentFilter);
    }

    public void unregister() {
        LocalBroadcastUtils.unregister(context, broadcastReceiver);
    }

    public FloatViewBroadcastReceiverHelper setOnFloatViewStateChangedListener(OnFloatViewStateChangedListener onFloatViewStateChangedListener) {
        this.onFloatViewStateChangedListener = onFloatViewStateChangedListener;
        return this;
    }

    public interface OnFloatViewStateChangedListener {
        void onServiceStart();

        void onServiceStop();

        void onShown();

        void onHidden();
    }
}
