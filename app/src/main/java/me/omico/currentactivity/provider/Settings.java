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
    public static final String RESET_SETUP_WIZARD = "reset_setup_wizard";
    public static final String ABOUT = "about";

    public static final String EXTRA_FIRST_OPEN = "me.omico.currentactivity.extra.FIRST_OPEN";
    public static final String EXTRA_SETUP_STEP = "me.omico.currentactivity.extra.SETUP_STEP";
    public static final String EXTRA_WORKING_MODE = "me.omico.currentactivity.extra.WORKING_MODE";
    public static final String EXTRA_SHORTCUT_OPEN = "me.omico.currentactivity.extra.SHORTCUT_OPEN";
    public static final String EXTRA_COME_FROM_MAIN = "me.omico.currentactivity.extra.COME_FROM_MAIN";

    public static final String ACTION_GESTURE_COPY = "me.omico.currentactivity.action.GESTURE_COPY";
    public static final String ACTION_GESTURE_HIDE = "me.omico.currentactivity.action.GESTURE_HIDE";
    public static final String ACTION_QUICK_START = "me.omico.currentactivity.action.QUICK_START";
    public static final String ACTION_STOP = "me.omico.currentactivity.action.STOP";

    public final static class Mode {
        public static final String SELECTION = "mode_selection";
        public static final String NONE = "mode_none";
        public static final String ROOT = "mode_root";
        public static final String ACCESSIBILITY_SERVICE = "mode_accessibility_service";
    }

    protected Settings(Context context, String sharedPreferences) {
        super(context, sharedPreferences);
    }
}
