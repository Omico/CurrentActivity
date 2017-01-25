package me.omico.currentactivity.receiver;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import me.omico.currentactivity.service.ListenerService;
import me.omico.util.ServiceUtils;

public class BootCompletedReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ServiceUtils.startService(context, ListenerService.class);

        intent.putExtra("weak-lock", true);
        Intent mIntent = new Intent(context, ListenerService.class);

        WakefulBroadcastReceiver.startWakefulService(context, mIntent);
    }
}
