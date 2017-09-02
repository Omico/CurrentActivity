package me.omico.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.Window;

import java.lang.reflect.Method;

/**
 * @author Omico 2017/2/16
 */

public class StatusBarUtils {

    /**
     * Need permission: <uses-permission android:name="android.permission.EXPAND_STATUS_BAR"/>
     */
    public static void collapseStatusBar(Context context) {
        try {
            @SuppressLint("WrongConstant") Object statusBarManager = context.getSystemService("statusbar");
            if (statusBarManager != null) {
                Method collapse;
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    collapse = statusBarManager.getClass().getMethod("collapse");
                } else {
                    collapse = statusBarManager.getClass().getMethod("collapsePanels");
                }
                collapse.setAccessible(true);
                collapse.invoke(statusBarManager);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor(Window window, @ColorRes int statusBarColor) {
        window.setStatusBarColor(ContextCompat.getColor(window.getContext(), statusBarColor));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor(Activity activity, @ColorRes int statusBarColor) {
        setStatusBarColor(activity.getWindow(), statusBarColor);
    }
}
