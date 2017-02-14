package me.omico.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

import me.omico.R;

/**
 * Created by Omico on 2017/2/7.
 */

public class ClipboardUtils {

    public static void copyToClipboard(Context context, @StringRes int resId) {
        copyToClipboard(context, context.getPackageName(), context.getString(resId));
    }

    public static void copyToClipboard(Context context, String text) {
        copyToClipboard(context, context.getPackageName(), text);
    }


    public static void copyToClipboard(Context context, @StringRes int resId, String text) {
        copyToClipboard(context, context.getString(resId), text);
    }

    public static void copyToClipboard(Context context, String name, @StringRes int resId) {
        copyToClipboard(context, name, context.getString(resId));
    }

    public static void copyToClipboard(Context context, @StringRes int resIdName, @StringRes int resIdText) {
        copyToClipboard(context, context.getString(resIdName), context.getString(resIdText));
    }

    public static void copyToClipboard(Context context, String name, String text) {
        try {
            ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE))
                    .setPrimaryClip(ClipData.newPlainText(name, text));
            Toast.makeText(context, R.string.copy_success, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, R.string.copy_failed, Toast.LENGTH_LONG).show();
        }
    }
}