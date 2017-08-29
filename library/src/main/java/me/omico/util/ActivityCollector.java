package me.omico.util;

import android.app.Activity;
import android.support.annotation.NonNull;

import java.util.Stack;

/**
 * @author Omico 2017/8/18
 */

public class ActivityCollector {

    private static ActivityCollector activityCollector;
    private static Stack<Activity> activityStack;

    private ActivityCollector() {
        if (activityStack == null) activityStack = new Stack<>();
    }

    public static ActivityCollector getActivityCollector() {
        if (activityCollector == null) activityCollector = new ActivityCollector();
        return activityCollector;
    }

    public void addActivity(@NonNull Activity activity) {
        activityStack.add(activity);
    }

    public void removeActivity(@NonNull Activity activity) {
        activityStack.remove(activity);
        activity.finish();
    }

    public void removeAllActivity() {
        for (Activity activity : activityStack) if (!activity.isFinishing()) activity.finish();
        activityStack.clear();
    }
}
