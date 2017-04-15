package me.omico.currentactivity;

import android.app.Application;

import com.google.firebase.crash.FirebaseCrash;

/**
 * Created by yuwen on 17-4-15.
 */

public class CurrentActivity extends Application {
    static {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                FirebaseCrash.report(e);
            }
        });
    }
}
