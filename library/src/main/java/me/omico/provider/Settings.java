package me.omico.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * @author Omico 2017/8/17
 */

public class Settings {
    private static final String XML_NAME = "settings";

    private static Settings settings;
    private SharedPreferences sharedPreferences;

    public static void init(@NonNull Context context) {
        if (settings == null) settings = new Settings(context, XML_NAME);
        settings();
    }

    protected Settings(@NonNull Context context, String sharedPreferences) {
        this.sharedPreferences = context.getSharedPreferences(sharedPreferences, Context.MODE_PRIVATE);
    }

    private static Settings settings() {
        if (settings != null) {
            return settings;
        }
        throw new RuntimeException("Call init in Application onCreate()");
    }

    public static String getString(@NonNull String key, String defaultValue) {
        return settings().sharedPreferences.getString(key, defaultValue);
    }

    public static Settings putString(@NonNull String key, String value) {
        settings().sharedPreferences.edit().putString(key, value).apply();
        return settings();
    }

    public static int getInt(@NonNull String key, int defaultValue) {
        return settings().sharedPreferences.getInt(key, defaultValue);
    }

    public static Settings putInt(@NonNull String key, int value) {
        settings().sharedPreferences.edit().putInt(key, value).apply();
        return settings();
    }

    public static boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return settings().sharedPreferences.getBoolean(key, defaultValue);
    }

    public static Settings putBoolean(@NonNull String key, boolean value) {
        settings().sharedPreferences.edit().putBoolean(key, value).apply();
        return settings();
    }

    public static long getLong(@NonNull String key, long defaultValue) {
        return settings().sharedPreferences.getLong(key, defaultValue);
    }

    public static Settings putLong(@NonNull String key, long value) {
        settings().sharedPreferences.edit().putLong(key, value).apply();
        return settings();
    }

    public static Settings remove(@NonNull String key) {
        settings().sharedPreferences.edit().remove(key).apply();
        return settings();
    }
}
