package me.omico.currentactivity.service;


import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

/**
 * @author Omico 2017/8/4
 */

public class CurrentActivityAccessibilityService extends AccessibilityService {

    private static volatile String foregroundPackageName;
    private static volatile String foregroundClassName;

    public static String foregroundPackageName() {
        return foregroundPackageName;
    }

    public static String foregroundClassName() {
        return foregroundClassName;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            foregroundPackageName = accessibilityEvent.getPackageName().toString();
            foregroundClassName = accessibilityEvent.getClassName().toString();
        }
    }

    @Override
    public void onInterrupt() {
    }
}
