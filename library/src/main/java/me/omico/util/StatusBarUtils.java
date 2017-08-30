package me.omico.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
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

    public static void addStatusBarView(Activity activity, @ColorRes int statusBarColor) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) return;
        View statusBarView = new View(activity);
        statusBarView.setBackgroundColor(ContextCompat.getColor(activity, statusBarColor));
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity));
        ((ViewGroup) activity.findViewById(android.R.id.content)).addView(statusBarView, layoutParams);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
