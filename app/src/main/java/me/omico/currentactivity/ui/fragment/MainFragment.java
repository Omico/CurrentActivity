package me.omico.currentactivity.ui.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import java.util.Collections;

import me.omico.currentactivity.R;
import me.omico.currentactivity.provider.Settings;
import me.omico.currentactivity.service.FloatViewService;
import me.omico.currentactivity.ui.activity.AboutActivity;
import me.omico.currentactivity.ui.activity.GuideActivity;
import me.omico.currentactivity.ui.activity.MainActivity;
import me.omico.util.ActivityUtils;
import me.omico.util.ServiceUtils;

import static me.omico.currentactivity.provider.Settings.ABOUT;
import static me.omico.currentactivity.provider.Settings.ACTION_QUICK_START;
import static me.omico.currentactivity.provider.Settings.BOOT_COMPLETED;
import static me.omico.currentactivity.provider.Settings.ENABLE_FLOAT_WINDOW;
import static me.omico.currentactivity.provider.Settings.EXTRA_FIRST_OPEN;
import static me.omico.currentactivity.provider.Settings.EXTRA_SETUP_STEP;
import static me.omico.currentactivity.provider.Settings.EXTRA_SHORTCUT_OPEN;
import static me.omico.currentactivity.provider.Settings.EXTRA_WORKING_MODE;
import static me.omico.currentactivity.provider.Settings.GESTURE_CLICK;
import static me.omico.currentactivity.provider.Settings.GESTURE_LONG_PRESS;
import static me.omico.currentactivity.provider.Settings.RESET_SETUP_WIZARD;

/**
 * @author Omico 2017/2/13
 */

public class MainFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    private Activity activity;

    private SwitchPreference enableFloatWindowPreference;
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

        initQuickStartAndShortcut();
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
        initListener();
    }

    private void initQuickStartAndShortcut() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) createShortcut();

        if (isQuickStartOrShortCutEnable())
            ServiceUtils.startService(activity, FloatViewService.class);
    }

    private boolean isQuickStartOrShortCutEnable() {
        Intent intent = activity.getIntent();
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) return action.equals(ACTION_QUICK_START);
            return intent.getBooleanExtra(EXTRA_SHORTCUT_OPEN, false);
        }
        return false;
    }

    private void initData() {
        enableFloatWindowPreference.setChecked(ServiceUtils.isRunning(activity, FloatViewService.class.getName()));
        modeSelectionPreference.setValue(Settings.getString(Settings.Mode.SELECTION, Settings.Mode.NONE));
    }

    private void initListener() {
        enableFloatWindowPreference.setOnPreferenceChangeListener(this);
        bootCompletedPreference.setOnPreferenceChangeListener(this);
        gestureClickPreference.setOnPreferenceChangeListener(this);
        gestureLongPressPreference.setOnPreferenceChangeListener(this);
        modeSelectionPreference.setOnPreferenceChangeListener(this);
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    private void createShortcut() {
        ShortcutManager shortcutManager = activity.getSystemService(ShortcutManager.class);

        Intent intent = new Intent(activity, MainActivity.class);
        intent.setAction(ACTION_QUICK_START);
        intent.putExtra(EXTRA_SHORTCUT_OPEN, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        ShortcutInfo shortcut = new ShortcutInfo.Builder(activity, "shortcut_open")
                .setShortLabel(getString(R.string.quick_enable))
                .setLongLabel(getString(R.string.enable_float_window))
                .setIcon(Icon.createWithResource(activity, R.mipmap.ic_launcher))
                .setIntent(intent)
                .build();

        if (shortcutManager != null) {
            shortcutManager.setDynamicShortcuts(Collections.singletonList(shortcut));
        }
    }

    private void intentGuideActivity(int setupStep, int workingMode) {
        Settings.putBoolean(EXTRA_FIRST_OPEN, true);
        Intent intent = new Intent(activity, GuideActivity.class);
        intent.putExtra(EXTRA_SETUP_STEP, setupStep);
        intent.putExtra(EXTRA_WORKING_MODE, workingMode);
        startActivity(intent);
        activity.finish();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        switch (preference.getKey()) {
            case ENABLE_FLOAT_WINDOW:
                if ((boolean) o) {
                    ServiceUtils.startService(activity, FloatViewService.class);
                } else {
                    ServiceUtils.stopService(activity, FloatViewService.class);
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
                Settings.putString(Settings.Mode.SELECTION, (String) o);
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
                intentGuideActivity(0, -1);
                break;
            case ABOUT:
                ActivityUtils.startActivity(activity, AboutActivity.class);
                break;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
