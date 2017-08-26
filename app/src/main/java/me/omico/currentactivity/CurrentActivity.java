package me.omico.currentactivity;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import me.omico.currentactivity.provider.Settings;

/**
 * @author Omico
 */

public class CurrentActivity extends Application {

    public static final int PERMISSION_CODE_OVERLAY = 0;
    public static final int PERMISSION_CODE_ACCESSIBILITY_SERVICE = 1;
    public static final int NOTIFICATION_ID = 1080;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Settings.init(this);
        if (BuildConfig.DEBUG) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .detectCustomSlowCalls()
                    .penaltyDeath();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                threadPolicyBuilder.detectResourceMismatches();
            }

            StrictMode.setThreadPolicy(threadPolicyBuilder.build());

            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder()
                            .detectLeakedSqlLiteObjects()
                            .detectLeakedClosableObjects()
                            .detectLeakedRegistrationObjects()
                            .detectActivityLeaks()
                            .penaltyDeath()
                            .build()
            );
        }
    }
}
