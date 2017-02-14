package me.omico.currentactivity.receiver;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import me.omico.currentactivity.service.FloatViewService;
import me.omico.util.ServiceUtils;
import me.omico.util.SharedPreferencesUtils;

import static me.omico.currentactivity.Constants.BOOT_COMPLETED;

public class BootCompletedReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SharedPreferencesUtils.getDefaultSharedPreferences(context, BOOT_COMPLETED, false))
            ServiceUtils.startWakefulService(context, FloatViewService.class);
    }
}
