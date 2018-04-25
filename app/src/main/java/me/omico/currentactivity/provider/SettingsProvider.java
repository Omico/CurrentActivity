package me.omico.currentactivity.provider;

import android.content.Context;
import android.support.annotation.NonNull;

import me.omico.provider.Provider;

/**
 * @author Omico 2017/8/17
 */

public class SettingsProvider extends Provider {

    public static final String ENABLE_FLOAT_WINDOW = "enable_float_window";
    public static final String BOOT_COMPLETED = "boot_completed";
    public static final String OPEN_MAIN_ACTIVITY_WHEN_QUICK_START_OR_QUICK_STOP = "open_main_activity_when_quick_start_or_quick_stop";
    public static final String GESTURE_CLICK = "gesture_click";
    public static final String GESTURE_LONG_PRESS = "gesture_long_press";
    public static final String WORKING_MODE = "working_mode";
    public static final String RESET_SETUP_WIZARD = "reset_setup_wizard";
    public static final String HISTORY = "history";
    public static final String ABOUT = "about";

    public static final String FIRST_OPEN = "first_open";

    protected SettingsProvider(@NonNull Context context, @NonNull String name, int mode) {
        super(context, name, mode);
    }

    public static void init(@NonNull Context context) {
        init(context, "settings");
    }

    public final static class Mode {
        public static final String SELECTION = "mode_selection";
        public static final String NONE = "mode_none";
        public static final String ROOT = "mode_root";
        public static final String ACCESSIBILITY_SERVICE = "mode_accessibility_service";
    }
}
