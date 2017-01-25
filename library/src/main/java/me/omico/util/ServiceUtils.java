package me.omico.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by yuwen on 17-1-24.
 */

public class ServiceUtils {

    public static void startService(Context context, Class service) {
        Intent serviceIntent = new Intent(context, service);
        context.startService(serviceIntent);
    }

    public static void stopService(Context context, Class service) {
        Intent serviceIntent = new Intent(context, service);
        context.stopService(serviceIntent);
    }

    public static boolean isRunning(Context mContext, String serviceClassName) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = manager.getRunningServices(30);

        for (ActivityManager.RunningServiceInfo info : serviceList) {
            if (TextUtils.equals(serviceClassName, info.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
