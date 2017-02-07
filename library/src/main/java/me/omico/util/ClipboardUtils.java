package me.omico.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import me.omico.R;

/**
 * Created by Omico on 2017/2/7.
 */

public class ClipboardUtils {
    public static void copyToClipboard(Context context, String text) {
        copyToClipboard(context, context.getPackageName(), text);
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