package me.omico.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Omico on 2/16/2017.
 */

public class StatusBarUtils {
    public static void addStatusBarView(Activity activity, @ColorRes int statusBarColor) {
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
