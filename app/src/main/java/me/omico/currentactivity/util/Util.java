package me.omico.currentactivity.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import me.omico.currentactivity.R;
import me.omico.currentactivity.provider.Settings;
import me.omico.util.ApplicationUtil;
import me.omico.util.root.SU;

/**
 * @author Omico
 */

public class Util {

    @NonNull
    public static String getCurrentActivity(Context context) {
        switch (Settings.getString(Settings.Mode.SELECTION, Settings.Mode.NONE)) {
            case Settings.Mode.ROOT:
                String request = SU.getSU().runCommand("dumpsys activity | grep \"mFocusedActivity\"");

                if (!TextUtils.isEmpty(request)) {
                    String requests[] = request.split(" ")[3].split("/");

                    String packageName = requests[0];
                    String activityName = requests[1].substring(0, 1).equals(".") ? requests[0] + requests[1] : requests[1];
                    String applicationName = ApplicationUtil.getApplicationNameByPackageName(context, requests[0]);

                    if (applicationName != null) {
                        return applicationName + " ( " + packageName + " )" + "\n" + activityName;
                    } else {
                        return packageName + "\n" + activityName;
                    }
                }
                break;
        }
        return context.getString(R.string.failed_to_get);
    }
}