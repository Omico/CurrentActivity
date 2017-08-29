package me.omico.currentactivity.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import me.omico.currentactivity.R;
import me.omico.currentactivity.provider.Settings;
import me.omico.currentactivity.service.FloatViewService;
import me.omico.currentactivity.ui.activity.AboutActivity;
import me.omico.currentactivity.ui.activity.GuideActivity;
import me.omico.util.ActivityUtils;
import me.omico.util.ServiceUtils;

import static me.omico.currentactivity.provider.Settings.ABOUT;
import static me.omico.currentactivity.provider.Settings.ACTION_FLOAT_VIEW_SERVICE_START;
import static me.omico.currentactivity.provider.Settings.ACTION_FLOAT_VIEW_SERVICE_STOP;
import static me.omico.currentactivity.provider.Settings.ACTION_GESTURE_COPY;
import static me.omico.currentactivity.provider.Settings.ACTION_GESTURE_HIDE;
import static me.omico.currentactivity.provider.Settings.BOOT_COMPLETED;
import static me.omico.currentactivity.provider.Settings.ENABLE_FLOAT_WINDOW;
import static me.omico.currentactivity.provider.Settings.EXTRA_COME_FROM_MAIN;
import static me.omico.currentactivity.provider.Settings.EXTRA_FIRST_OPEN;
import static me.omico.currentactivity.provider.Settings.EXTRA_SETUP_STEP;
import static me.omico.currentactivity.provider.Settings.EXTRA_WORKING_MODE;
import static me.omico.currentactivity.provider.Settings.GESTURE_CLICK;
import static me.omico.currentactivity.provider.Settings.GESTURE_LONG_PRESS;
import static me.omico.currentactivity.provider.Settings.RESET_SETUP_WIZARD;

/**
 * @author Omico 2017/2/13
 */

public class MainFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    private Activity activity;

    public static SwitchPreference enableFloatWindowPreference;
    private SwitchPreference bootCompletedPreference;
    private ListPreference gestureClickPreference;
    private ListPreference gestureLongPressPreference;
    private ListPreference modeSelectionPreference;

    public MainFragment() {
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main);

        this.activity = MainFragment.this.getActivity();

        enableFloatWindowPreference = (SwitchPreference) findPreference(ENABLE_FLOAT_WINDOW);
        bootCompletedPreference = (SwitchPreference) findPreference(BOOT_COMPLETED);
        gestureClickPreference = (ListPreference) findPreference(GESTURE_CLICK);
        gestureLongPressPreference = (ListPreference) findPreference(GESTURE_LONG_PRESS);
        modeSelectionPreference = (ListPreference) findPreference(Settings.Mode.SELECTION);
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
        initListener();
    }

    private void initData() {
        enableFloatWindowPreference.setChecked(ServiceUtils.isRunning(activity, FloatViewService.class.getName()));

        Settings.putString(GESTURE_CLICK, ACTION_GESTURE_HIDE);
        Settings.putString(GESTURE_LONG_PRESS, ACTION_GESTURE_COPY);

        switch (Settings.getString(Settings.Mode.SELECTION, Settings.Mode.NONE)) {
            case Settings.Mode.ROOT:
            case Settings.Mode.ACCESSIBILITY_SERVICE:
                modeSelectionPreference.setValue(Settings.getString(Settings.Mode.SELECTION, Settings.Mode.NONE));
                break;
            case Settings.Mode.NONE:
                modeSelectionPreference.setValue(Settings.Mode.NONE);
                modeSelectionPreference.setSummary(String.format(getString(R.string.working_mode_label), getString(R.string.mode_none)));
                break;
        }
    }

    private void initListener() {
        enableFloatWindowPreference.setOnPreferenceChangeListener(this);
        bootCompletedPreference.setOnPreferenceChangeListener(this);
        gestureClickPreference.setOnPreferenceChangeListener(this);
        gestureLongPressPreference.setOnPreferenceChangeListener(this);
        modeSelectionPreference.setOnPreferenceChangeListener(this);
    }

    private void intentGuideActivity(int setupStep, int workingMode) {
        Settings.putBoolean(EXTRA_FIRST_OPEN, true);
        Intent intent = new Intent(activity, GuideActivity.class);
        intent.putExtra(EXTRA_SETUP_STEP, setupStep);
        intent.putExtra(EXTRA_WORKING_MODE, workingMode);
        if (workingMode != -1) intent.putExtra(EXTRA_COME_FROM_MAIN, true);
        startActivity(intent);
        activity.finish();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        switch (preference.getKey()) {
            case ENABLE_FLOAT_WINDOW:
                if (Settings.getString(Settings.Mode.SELECTION, Settings.Mode.NONE).equals(Settings.Mode.NONE)) {
                    intentGuideActivity(0, -1);
                } else if ((boolean) o) {
                    activity.startService(new Intent(activity, FloatViewService.class).setAction(ACTION_FLOAT_VIEW_SERVICE_START));
                } else {
                    activity.startService(new Intent(activity, FloatViewService.class).setAction(ACTION_FLOAT_VIEW_SERVICE_STOP));
                }
                break;
            case BOOT_COMPLETED:
                Settings.putBoolean(BOOT_COMPLETED, (boolean) o);
                break;
            case GESTURE_CLICK:
                Settings.putString(GESTURE_CLICK, (String) o);
                break;
            case GESTURE_LONG_PRESS:
                Settings.putString(GESTURE_LONG_PRESS, (String) o);
                break;
            case Settings.Mode.SELECTION:
                switch ((String) o) {
                    case Settings.Mode.ROOT:
                        intentGuideActivity(2, 0);
                        break;
                    case Settings.Mode.ACCESSIBILITY_SERVICE:
                        intentGuideActivity(2, 1);
                        break;
                    case Settings.Mode.NONE:
                        intentGuideActivity(0, -1);
                        break;
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        switch (preference.getKey()) {
            case RESET_SETUP_WIZARD:
                activity.startService(new Intent(activity, FloatViewService.class).setAction(ACTION_FLOAT_VIEW_SERVICE_STOP));
                intentGuideActivity(0, -1);
                break;
            case ABOUT:
                ActivityUtils.startActivity(activity, AboutActivity.class);
                break;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
