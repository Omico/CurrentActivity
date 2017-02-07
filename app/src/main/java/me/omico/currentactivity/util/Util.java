package me.omico.currentactivity.util;

import android.content.Context;
import android.text.TextUtils;

import me.omico.currentactivity.R;
import me.omico.util.root.SU;

public class Util {

    public static String getCurrentActivity(Context context) {
        String request = SU.getSU().runCommand("dumpsys activity | grep \"mFocusedActivity\"");

        if (!TextUtils.isEmpty(request)) {
            String requests[] = request.split(" ")[3].split("/");

            String packageName = requests[0];
            String activityName = requests[1].substring(0, 1).equals(".") ? requests[0] + requests[1] : requests[1];

            return packageName + "\n" + activityName;
        }
        return context.getString(R.string.failed_to_get);
    }
}