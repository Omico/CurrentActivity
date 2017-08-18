package me.omico.currentactivity.provider;

import android.content.Context;

/**
 * @author Omico 2017/8/17
 */

public class Settings extends me.omico.provider.Settings {

    public static final String ENABLE_FLOAT_WINDOW = "enable_float_window";
    public static final String BOOT_COMPLETED = "boot_completed";
    public static final String GESTURE_CLICK = "gesture_click";
    public static final String GESTURE_LONG_PRESS = "gesture_long_press";
    public static final String ABOUT = "about";

    public static final String IS_FIRST_OPEN = "is_first_open";
    public static final String IS_SHORTCUT_OPEN = "is_shortcut_open";

    public static final String ACTION_GESTURE_COPY = "me.omico.currentactivity.action.gesture_copy";
    public static final String ACTION_GESTURE_HIDE = "me.omico.currentactivity.action.gesture_hide";
    public static final String ACTION_QUICK_START = "me.omico.currentactivity.action.quick_start";
    public static final String ACTION_STOP = "me.omico.currentactivity.action.stop";

    protected Settings(Context context, String sharedPreferences) {
        super(context, sharedPreferences);
    }
}
