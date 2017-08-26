package me.omico.currentactivity.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import me.omico.currentactivity.R;
import me.omico.currentactivity.provider.Settings;
import me.omico.currentactivity.service.CurrentActivityAccessibilityService;
import me.omico.util.AccessibilityServiceUtils;
import me.omico.util.ApplicationUtil;
import me.omico.util.root.SU;

/**
 * @author Omico
 */

public class Util {

    @NonNull
    public static String getCurrentActivity(Context context) {
        String packageName = null;
        String activityName = null;
        String applicationName;

        switch (Settings.getString(Settings.Mode.SELECTION, Settings.Mode.NONE)) {
            case Settings.Mode.ROOT:
                String request = SU.getSU().runCommand("dumpsys activity | grep \"mFocusedActivity\"");

                if (!TextUtils.isEmpty(request)) {
                    String requests[] = request.split(" ")[3].split("/");

                    packageName = requests[0];
                    activityName = requests[1].substring(0, 1).equals(".") ? requests[0] + requests[1] : requests[1];
                }
                break;
            case Settings.Mode.ACCESSIBILITY_SERVICE:
                if (AccessibilityServiceUtils.isAccessibilityServiceEnabled(context, CurrentActivityAccessibilityService.class)) {
                    packageName = CurrentActivityAccessibilityService.foregroundPackageName();
                    activityName = CurrentActivityAccessibilityService.foregroundClassName();
                } else {
                    return context.getString(R.string.should_re_enable_accessibility_service);
                }
                break;
        }

        if (packageName != null && activityName != null) {
            applicationName = ApplicationUtil.getApplicationNameByPackageName(context, packageName);
            return (applicationName != null) ? (applicationName + " ( " + packageName + " )" + "\n" + activityName) : (packageName + "\n" + activityName);
        } else {
            return context.getString(R.string.failed_to_get);
        }
    }
}