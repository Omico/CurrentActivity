package android.app;

import android.annotation.TargetApi;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;

import java.util.List;

public interface IActivityTaskManager extends IInterface {
    List<ActivityManager.RunningTaskInfo> getTasks(int maxNum);

    // Android 12 or an earlier version of Android 13, android-12.0.0_r1 to android-13.0.0_r15.
    // https://github.com/aosp-mirror/platform_frameworks_base/commit/4091a47400e9112726f081d3748dd53c03f3a31c
    @TargetApi(31)
    List<ActivityManager.RunningTaskInfo> getTasks(int maxNum, boolean filterOnlyVisibleRecents,
                                                   boolean keepIntentExtra);

    // Since android-13.0.0_r16, the parameter "displayId" is added.
    // https://github.com/aosp-mirror/platform_frameworks_base/commit/5c8fd0ddbf5ca7eb4a49bbd8125ec7d246036751
    @TargetApi(33)
    List<ActivityManager.RunningTaskInfo> getTasks(int maxNum, boolean filterOnlyVisibleRecents,
                                                   boolean keepIntentExtra, int displayId);

    abstract class Stub extends Binder implements IActivityTaskManager {
        public static IActivityTaskManager asInterface(IBinder obj) {
            throw new RuntimeException("Stub!");
        }
    }
}
