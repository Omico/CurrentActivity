package me.omico.currentactivity.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import me.omico.currentactivity.provider.Settings;
import me.omico.currentactivity.service.FloatViewService;
import me.omico.util.ServiceUtils;

import static me.omico.currentactivity.provider.Settings.BOOT_COMPLETED;

/**
 * @author Omico
 */

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Settings.getBoolean(BOOT_COMPLETED, false))
            ServiceUtils.startService(context, FloatViewService.class);
    }
}