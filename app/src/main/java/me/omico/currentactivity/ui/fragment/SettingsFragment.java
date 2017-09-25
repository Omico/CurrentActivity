package me.omico.currentactivity.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;

import java.util.Objects;

import me.omico.currentactivity.R;
import me.omico.currentactivity.provider.Settings;
import me.omico.currentactivity.service.FloatViewService;
import me.omico.currentactivity.ui.activity.AboutActivity;
import me.omico.currentactivity.ui.activity.GuideActivity;
import me.omico.currentactivity.util.FloatViewBroadcastReceiverHelper;
import me.omico.util.ActivityCollector;
import me.omico.util.ActivityUtils;
import me.omico.util.LocalBroadcastUtils;
import me.omico.util.ServiceUtils;

import static me.omico.currentactivity.CurrentActivity.ACTION_FLOAT_VIEW_SERVICE_START;
import static me.omico.currentactivity.CurrentActivity.ACTION_FLOAT_VIEW_SERVICE_STOP;
import static me.omico.currentactivity.CurrentActivity.ACTION_GESTURE_COPY;
import static me.omico.currentactivity.CurrentActivity.ACTION_GESTURE_HIDE;
import static me.omico.currentactivity.CurrentActivity.EXTRA_COME_FROM_MAIN;
import static me.omico.currentactivity.CurrentActivity.EXTRA_SETUP_STEP;
import static me.omico.currentactivity.CurrentActivity.EXTRA_WORKING_MODE;
import static me.omico.currentactivity.provider.Settings.ABOUT;
import static me.omico.currentactivity.provider.Settings.BOOT_COMPLETED;
import static me.omico.currentactivity.provider.Settings.ENABLE_FLOAT_WINDOW;
import static me.omico.currentactivity.provider.Settings.FIRST_OPEN;
import static me.omico.currentactivity.provider.Settings.GESTURE_CLICK;
import static me.omico.currentactivity.provider.Settings.GESTURE_LONG_PRESS;
import static me.omico.currentactivity.provider.Settings.OPEN_MAIN_ACTIVITY_WHEN_QUICK_START_OR_QUICK_STOP;
import static me.omico.currentactivity.provider.Settings.RESET_SETUP_WIZARD;
import static me.omico.currentactivity.provider.Settings.WORKING_MODE;
import static me.omico.currentactivity.ui.activity.GuideActivity.MODE_ACCESSIBILITY_SERVICE;
import static me.omico.currentactivity.ui.activity.GuideActivity.MODE_NONE;
import static me.omico.currentactivity.ui.activity.GuideActivity.MODE_ROOT;
import static me.omico.currentactivity.ui.activity.GuideActivity.PAGE_GETTING_MODE_PERMISSION;
import static me.omico.currentactivity.ui.activity.GuideActivity.PAGE_WELCOME;

/**
 * @author Omico 2017/9/5
 */

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    private Activity activity;

    private SwitchPreferenceCompat enableFloatWindowPreference;
    private SwitchPreferenceCompat bootCompletedPreference;
    private SwitchPreferenceCompat openMainActivityWhenQuickStartOrQuickStopPreference;
    private ListPreference gestureClickPreference;
    private ListPreference gestureLongPressPreference;
    private ListPreference workingModePreference;

    private FloatViewBroadcastReceiverHelper floatViewBroadcastReceiverHelper;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);

        activity = this.getActivity();

        enableFloatWindowPreference = (SwitchPreferenceCompat) findPreference(ENABLE_FLOAT_WINDOW);
        bootCompletedPreference = (SwitchPreferenceCompat) findPreference(BOOT_COMPLETED);
        openMainActivityWhenQuickStartOrQuickStopPreference = (SwitchPreferenceCompat) findPreference(OPEN_MAIN_ACTIVITY_WHEN_QUICK_START_OR_QUICK_STOP);
        gestureClickPreference = (ListPreference) findPreference(GESTURE_CLICK);
        gestureLongPressPreference = (ListPreference) findPreference(GESTURE_LONG_PRESS);
        workingModePreference = (ListPreference) findPreference(WORKING_MODE);

        initFloatViewBroadcastReceiverHelper();
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
        initListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        enableFloatWindowPreference.setChecked(ServiceUtils.isRunning(activity, FloatViewService.class.getName()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        floatViewBroadcastReceiverHelper.unregister();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case ENABLE_FLOAT_WINDOW:
                if (Settings.getString(Settings.Mode.SELECTION, Settings.Mode.NONE).equals(Settings.Mode.NONE)) {
                    ActivityCollector.getActivityCollector().removeAllActivity();
                    intentGuideActivity(PAGE_WELCOME, MODE_NONE);
                } else if ((boolean) newValue) {
                    activity.startService(new Intent(activity, FloatViewService.class).setAction(ACTION_FLOAT_VIEW_SERVICE_START));
                } else {
                    LocalBroadcastUtils.send(activity, new Intent(ACTION_FLOAT_VIEW_SERVICE_STOP));
                }
                break;
            case BOOT_COMPLETED:
                Settings.putBoolean(BOOT_COMPLETED, (boolean) newValue);
                break;
            case OPEN_MAIN_ACTIVITY_WHEN_QUICK_START_OR_QUICK_STOP:
                Settings.putBoolean(OPEN_MAIN_ACTIVITY_WHEN_QUICK_START_OR_QUICK_STOP, (boolean) newValue);
                break;
            case GESTURE_CLICK:
                Settings.putString(GESTURE_CLICK, (String) newValue);
                break;
            case GESTURE_LONG_PRESS:
                Settings.putString(GESTURE_LONG_PRESS, (String) newValue);
                break;
            case WORKING_MODE:
                switch ((String) newValue) {
                    case Settings.Mode.ROOT:
                        intentGuideActivity(PAGE_GETTING_MODE_PERMISSION, MODE_ROOT);
                        break;
                    case Settings.Mode.ACCESSIBILITY_SERVICE:
                        intentGuideActivity(PAGE_GETTING_MODE_PERMISSION, MODE_ACCESSIBILITY_SERVICE);
                        break;
                    case Settings.Mode.NONE:
                        intentGuideActivity(PAGE_WELCOME, MODE_NONE);
                        break;
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        switch (preference.getKey()) {
            case RESET_SETUP_WIZARD:
                LocalBroadcastUtils.send(activity, new Intent(ACTION_FLOAT_VIEW_SERVICE_STOP));
                intentGuideActivity(PAGE_WELCOME, MODE_NONE);
                break;
            case ABOUT:
                ActivityUtils.startActivity(activity, AboutActivity.class);
                break;
        }
        return super.onPreferenceTreeClick(preference);
    }

    private void initFloatViewBroadcastReceiverHelper() {
        floatViewBroadcastReceiverHelper = new FloatViewBroadcastReceiverHelper(activity);

        floatViewBroadcastReceiverHelper
                .setOnFloatViewStateChangedListener(
                        new FloatViewBroadcastReceiverHelper.OnFloatViewStateChangedListener() {
                            @Override
                            public void onServiceStart() {
                            }

                            @Override
                            public void onServiceStop() {
                                enableFloatWindowPreference.setChecked(false);
                            }

                            @Override
                            public void onShown() {
                            }

                            @Override
                            public void onHidden() {
                            }
                        }
                )
                .register();
    }

    private void initData() {
        Settings.putString(GESTURE_CLICK, ACTION_GESTURE_HIDE);
        Settings.putString(GESTURE_LONG_PRESS, ACTION_GESTURE_COPY);

        String mode = Settings.getString(Settings.Mode.SELECTION, Settings.Mode.NONE);

        workingModePreference.setValue(mode);

        if (Objects.equals(mode, Settings.Mode.NONE))
            workingModePreference.setSummary(String.format(getString(R.string.working_mode_label), getString(R.string.mode_none)));
    }

    private void initListener() {
        enableFloatWindowPreference.setOnPreferenceChangeListener(this);
        bootCompletedPreference.setOnPreferenceChangeListener(this);
        openMainActivityWhenQuickStartOrQuickStopPreference.setOnPreferenceChangeListener(this);
        gestureClickPreference.setOnPreferenceChangeListener(this);
        gestureLongPressPreference.setOnPreferenceChangeListener(this);
        workingModePreference.setOnPreferenceChangeListener(this);
    }

    private void intentGuideActivity(int setupStep, int workingMode) {
        Settings.putBoolean(FIRST_OPEN, true);
        if (workingMode == MODE_NONE)
            Settings.putString(Settings.Mode.SELECTION, Settings.Mode.NONE);
        Intent intent = new Intent(activity, GuideActivity.class);
        intent.putExtra(EXTRA_SETUP_STEP, setupStep);
        intent.putExtra(EXTRA_WORKING_MODE, workingMode);
        if (workingMode != MODE_NONE) intent.putExtra(EXTRA_COME_FROM_MAIN, true);
        startActivity(intent);
        activity.finish();
    }
}
