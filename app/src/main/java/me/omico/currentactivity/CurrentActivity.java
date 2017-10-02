package me.omico.currentactivity;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import me.omico.currentactivity.model.CurrentActivityData;
import me.omico.currentactivity.provider.Settings;
import me.omico.currentactivity.service.CurrentActivityAccessibilityService;
import me.omico.util.AccessibilityServiceUtils;
import me.omico.util.ApplicationUtil;
import me.omico.util.root.SU;

/**
 * @author Omico
 */

public class CurrentActivity extends Application {

    public static final String EXTRA_SETUP_STEP = "me.omico.currentactivity.extra.SETUP_STEP";
    public static final String EXTRA_WORKING_MODE = "me.omico.currentactivity.extra.WORKING_MODE";
    public static final String EXTRA_COME_FROM_MAIN = "me.omico.currentactivity.extra.COME_FROM_MAIN";
    public static final String EXTRA_COME_FROM_TILE_SERVICE = "me.omico.currentactivity.extra.COME_FROM_TILE_SERVICE";
    public static final String EXTRA_COME_FROM_SHORTCUT = "me.omico.currentactivity.extra.COME_FROM_SHORTCUT";

    public static final String ACTION_QUICK_START_OR_QUICK_STOP = "me.omico.currentactivity.action.QUICK_START_OR_QUICK_STOP";
    public static final String ACTION_GESTURE_COPY = "me.omico.currentactivity.action.GESTURE_COPY";
    public static final String ACTION_GESTURE_HIDE = "me.omico.currentactivity.action.GESTURE_HIDE";
    public static final String ACTION_FLOAT_VIEW_SERVICE_START = "me.omico.currentactivity.action.FLOAT_VIEW_SERVICE_START";
    public static final String ACTION_FLOAT_VIEW_SERVICE_STOP = "me.omico.currentactivity.action.FLOAT_VIEW_SERVICE_STOP";
    public static final String ACTION_FLOAT_VIEW_SHOW = "me.omico.currentactivity.action.FLOAT_VIEW_SHOW";
    public static final String ACTION_FLOAT_VIEW_HIDE = "me.omico.currentactivity.action.FLOAT_VIEW_HIDE";

    public static final int PERMISSION_CODE_OVERLAY = 0;
    public static final int PERMISSION_CODE_ACCESSIBILITY_SERVICE = 1;

    public static final int NOTIFICATION_ID = 1080;

    private Realm realm;
    private RealmConfiguration realmConfiguration;

    @Override
    public void onCreate() {
        super.onCreate();
        Settings.init(this);
        Realm.init(this);
        realm = Realm.getInstance(getRealmConfiguration());
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        } else {
            initStrictMode();
        }
    }

    private void initStrictMode() {
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

    public String getCurrentActivity() {
        String currentActivity;
        synchronized (this) {
            currentActivity = getCurrentActivity(this);
        }
        return currentActivity;
    }

    @NonNull
    private String getCurrentActivity(Context context) {
        String packageName = null;
        String activityName = null;
        String applicationName;

        switch (Settings.getString(Settings.Mode.SELECTION, Settings.Mode.NONE)) {
            case Settings.Mode.ROOT:
                String request = SU.getSU().runCommand("dumpsys activity | grep \"mFocusedActivity\"");

                if (!TextUtils.isEmpty(request)) {
                    String requests[] = request.split(" ")[3].split("/");

                    packageName = requests[0];
                    activityName = requests[1].substring(0, 1).equals(".") ? requests[0] + requests[1] : requests[1];
                }
                break;
            case Settings.Mode.ACCESSIBILITY_SERVICE:
                if (AccessibilityServiceUtils.isAccessibilityServiceEnabled(context, CurrentActivityAccessibilityService.class)) {
                    packageName = CurrentActivityAccessibilityService.foregroundPackageName();
                    activityName = CurrentActivityAccessibilityService.foregroundClassName();
                    if (packageName.equals("null") || activityName.equals("null")) return "";
                } else {
                    return context.getString(R.string.should_re_enable_accessibility_service);
                }
                break;
        }

        if (packageName != null && activityName != null) {
            applicationName = ApplicationUtil.getApplicationNameByPackageName(context, packageName);

            RealmResults<CurrentActivityData> results = realm.where(CurrentActivityData.class).findAll();

            if (!Objects.equals(packageName, getPackageName())) {
                if (results.isEmpty()) {
                    saveCurrentActivityData(packageName, activityName);
                } else {
                    CurrentActivityData lastData = results.get(results.size() - 1);
                    if (!Objects.equals(lastData.getPackageName(), packageName) || !Objects.equals(lastData.getActivityName(), activityName))
                        saveCurrentActivityData(packageName, activityName);
                }
            }
            return (applicationName != null) ? (applicationName + " ( " + packageName + " )" + "\n" + activityName) : (packageName + "\n" + activityName);
        } else {
            return context.getString(R.string.failed_to_get);
        }
    }

    public RealmConfiguration getRealmConfiguration() {
        if (realmConfiguration == null)
            realmConfiguration = new RealmConfiguration
                    .Builder()
                    .deleteRealmIfMigrationNeeded()
                    .name("current_activity")
                    .build();
        return realmConfiguration;
    }

    private void saveCurrentActivityData(final String packageName, final String activityName) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                CurrentActivityData currentActivityData = realm.createObject(CurrentActivityData.class);
                currentActivityData.setActivityName(activityName);
                currentActivityData.setPackageName(packageName);
            }
        });
    }

    @Nullable
    public List<CurrentActivityData> loadCurrentActivityData() {
        RealmResults<CurrentActivityData> results = realm.where(CurrentActivityData.class).findAll();
        if (!results.isEmpty()) {
            List<CurrentActivityData> data = new ArrayList<>();
            int resultsNum = results.size() - 1;
            if (resultsNum >= 10) {
                for (int i = resultsNum; i >= resultsNum - 10; i--)
                    data.add(results.get(i));
            } else {
                for (int i = resultsNum; i >= 0; i--) data.add(results.get(i));
            }
            return data;
        }
        return null;
    }

    public void clearCurrentActivityData() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                RealmResults<CurrentActivityData> results = realm.where(CurrentActivityData.class).findAll();
                results.deleteAllFromRealm();
            }
        });
    }
}
