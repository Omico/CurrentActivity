package me.omico.currentactivity.service;


import android.accessibilityservice.AccessibilityService;
import android.support.annotation.NonNull;
import android.view.accessibility.AccessibilityEvent;

/**
 * @author Omico 2017/8/4
 */

public class CurrentActivityAccessibilityService extends AccessibilityService {

    private static volatile CharSequence foregroundPackageName;
    private static volatile CharSequence foregroundClassName;

    @NonNull
    public static String foregroundPackageName() {
        return foregroundPackageName.toString();
    }

    @NonNull
    public static String foregroundClassName() {
        return foregroundClassName.toString();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            foregroundPackageName = accessibilityEvent.getPackageName();
            foregroundClassName = accessibilityEvent.getClassName();
        }
    }

    @Override
    public void onInterrupt() {
    }
}
