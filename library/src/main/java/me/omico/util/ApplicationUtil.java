package me.omico.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * @author Omico 2017/2/8
 */

public class ApplicationUtil {

    public static String getApplicationNameByPackageName(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        String applicationName = null;
        try {
            applicationName = packageManager.getApplicationLabel(packageManager.getApplicationInfo(
                    packageName, 0)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return applicationName;
    }
}
