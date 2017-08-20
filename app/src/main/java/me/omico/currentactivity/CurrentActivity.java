package me.omico.currentactivity;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import me.omico.currentactivity.provider.Settings;

/**
 * @author Omico
 */

public class CurrentActivity extends Application {

    public static final int PERMISSION_CODE_OVERLAY = 0;
    public static final int NOTIFICATION_ID = 1080;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Settings.init(this);
    }
}
