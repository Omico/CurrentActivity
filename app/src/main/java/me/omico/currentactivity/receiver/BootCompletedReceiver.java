package me.omico.currentactivity.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import me.omico.currentactivity.provider.SettingsProvider;
import me.omico.currentactivity.service.FloatViewService;
import me.omico.util.ServiceUtils;

import static me.omico.currentactivity.CurrentActivity.ACTION_FLOAT_VIEW_SERVICE_START;
import static me.omico.currentactivity.provider.SettingsProvider.BOOT_COMPLETED;

/**
 * @author Omico
 */

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_BOOT_COMPLETED) && SettingsProvider.getBoolean(BOOT_COMPLETED, false))
            ServiceUtils.startService(context, FloatViewService.class, ACTION_FLOAT_VIEW_SERVICE_START);
    }
}