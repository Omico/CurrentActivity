package me.omico.currentactivity.provider;

import android.content.Context;

/**
 * @author Omico 2017/8/17
 */

public class Settings extends me.omico.provider.Settings {

    public static final String ENABLE_FLOAT_WINDOW = "enable_float_window";
    public static final String BOOT_COMPLETED = "boot_completed";
    public static final String OPEN_MAIN_ACTIVITY_WHEN_QUICK_START = "open_main_activity_when_quick_start";
    public static final String GESTURE_CLICK = "gesture_click";
    public static final String GESTURE_LONG_PRESS = "gesture_long_press";
    public static final String WORKING_MODE = "working_mode";
    public static final String RESET_SETUP_WIZARD = "reset_setup_wizard";
    public static final String ABOUT = "about";

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
