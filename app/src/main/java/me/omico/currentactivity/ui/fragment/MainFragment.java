package me.omico.currentactivity.ui.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;

import java.util.Collections;

import me.omico.currentactivity.R;
import me.omico.currentactivity.service.FloatViewService;
import me.omico.currentactivity.ui.activity.AboutActivity;
import me.omico.currentactivity.ui.activity.MainActivity;
import me.omico.util.ActivityUtils;
import me.omico.util.ServiceUtils;
import me.omico.util.SharedPreferencesUtils;
import me.omico.util.root.SU;

import static me.omico.currentactivity.Constants.ABOUT;
import static me.omico.currentactivity.Constants.ACTION_QUICK_START;
import static me.omico.currentactivity.Constants.BOOT_COMPLETED;
import static me.omico.currentactivity.Constants.ENABLE_FLOAT_WINDOW;
import static me.omico.currentactivity.Constants.GESTURE_CLICK;
import static me.omico.currentactivity.Constants.GESTURE_LONG_PRESS;
import static me.omico.currentactivity.Constants.IS_FIRST_OPEN;
import static me.omico.currentactivity.Constants.IS_SHORTCUT_OPEN;
import static me.omico.currentactivity.Constants.OVERLAY_PERMISSION_CODE;

/**
 * @author Omico 2017/2/13
 */

public class MainFragment extends PreferenceFragment {

    private Activity activity;

    private SwitchPreference enableFloatWindowPreference;
    private SwitchPreference bootCompletedPreference;
    private ListPreference gestureClickPreference;
    private ListPreference gestureLongPressPreference;

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
            return intent.getBooleanExtra(IS_SHORTCUT_OPEN, false);
        }
        return false;
    }

    private void initData() {
        if (SharedPreferencesUtils.getDefaultSharedPreferences(activity, IS_FIRST_OPEN, true)) {
            showNoticeDialog();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) showPermissionDialog();

        enableFloatWindowPreference.setChecked(ServiceUtils.isRunning(activity, FloatViewService.class.getName()));
    }

    private void initListener() {
        enableFloatWindowPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((boolean) newValue) {
                    ServiceUtils.startService(activity, FloatViewService.class);
                } else {
                    ServiceUtils.stopService(activity, FloatViewService.class);
                }
                return true;
            }
        });

        bootCompletedPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferencesUtils.setDefaultSharedPreferences(activity, BOOT_COMPLETED, (boolean) newValue);
                return true;
            }
        });

        gestureClickPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferencesUtils.setDefaultSharedPreferences(activity, GESTURE_CLICK, (String) newValue);
                return true;
            }
        });

        gestureLongPressPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferencesUtils.setDefaultSharedPreferences(activity, GESTURE_LONG_PRESS, (String) newValue);
                return true;
            }
        });
    }

    private void showNoticeDialog() {
        addDialog(activity, "注意", "此软件需要 ROOT 权限，6.0以上的应用需要额外权限")
                .setPositiveButton("接受，并授权", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        setPreferenceEnable(SU.isRoot());
                        SharedPreferencesUtils.setDefaultSharedPreferences(activity, IS_FIRST_OPEN, false);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) showPermissionDialog();
                    }
                })
                .setNegativeButton("放弃并退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        activity.finish();
                    }
                }).create().show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void showPermissionDialog() {
        if (!Settings.canDrawOverlays(activity)) {
            setPreferenceEnable(false);
            addDialog(activity, "注意", "需要额外授权“允许出现在其他应用上”的权限！")
                    .setPositiveButton("接受，并授权", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + activity.getPackageName()));
                            startActivityForResult(intent, OVERLAY_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("放弃并退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            activity.finish();
                        }
                    }).create().show();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_CODE) {
            if (!Settings.canDrawOverlays(activity)) {
                showSnackBarNoAction("权限授予失败，无法开启悬浮窗", Snackbar.LENGTH_SHORT);
                setPreferenceEnable(false);
            } else {
                showSnackBarNoAction("权限授予成功！", Snackbar.LENGTH_SHORT);
                setPreferenceEnable(true);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    private void createShortcut() {
        ShortcutManager shortcutManager = activity.getSystemService(ShortcutManager.class);

        Intent intent = new Intent(activity, MainActivity.class);
        intent.setAction(ACTION_QUICK_START);
        intent.putExtra(IS_SHORTCUT_OPEN, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        ShortcutInfo shortcut = new ShortcutInfo.Builder(activity, "shortcut_open")
                .setShortLabel(getString(R.string.quick_enable))
                .setLongLabel(getString(R.string.enable_float_window))
                .setIcon(Icon.createWithResource(activity, R.mipmap.ic_launcher))
                .setIntent(intent)
                .build();

        shortcutManager.setDynamicShortcuts(Collections.singletonList(shortcut));
    }

    private void setPreferenceEnable(boolean enable) {
        enableFloatWindowPreference.setEnabled(enable);
        bootCompletedPreference.setEnabled(enable);
        gestureClickPreference.setEnabled(enable);
        gestureLongPressPreference.setEnabled(enable);
    }

    private AlertDialog.Builder addDialog(Context context, String title, String message) {
        return new AlertDialog.Builder(context).setTitle(title).setMessage(message);
    }

    private void showSnackBarNoAction(String text, int time) {
        Snackbar.make(activity.findViewById(R.id.main_coordinator_layout), text, time).show();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        switch (preference.getKey()) {
            case ABOUT:
                ActivityUtils.startActivity(activity, AboutActivity.class);
                break;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
