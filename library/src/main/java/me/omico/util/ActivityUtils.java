package me.omico.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * @author Omico 2017/2/13
 */

public class ActivityUtils {

    public static void startActivity(Context context, Class<? extends Activity> activity) {
        Intent activityIntent = new Intent(context, activity);
        context.startActivity(activityIntent);
    }
}
