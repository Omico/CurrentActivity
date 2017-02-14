package me.omico.currentactivity.ui.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;

import me.omico.currentactivity.R;
import me.omico.currentactivity.service.FloatViewService;
import me.omico.currentactivity.ui.activity.AboutActivity;
import me.omico.util.ActivityUtils;
import me.omico.util.ServiceUtils;
import me.omico.util.SharedPreferencesUtils;
import me.omico.util.root.SU;

import static me.omico.currentactivity.Constants.ABOUT;
import static me.omico.currentactivity.Constants.BOOT_COMPLETED;
import static me.omico.currentactivity.Constants.ENABLE_FLOAT_WINDOW;
import static me.omico.currentactivity.Constants.IS_FIRST_OPEN;
import static me.omico.currentactivity.Constants.OVERLAY_PERMISSION_CODE;

/**
 * Created by Omico on 2017/2/13.
 */

public class MainFragment extends PreferenceFragment {

    private Activity activity;

    private SwitchPreference enableFloatWindowPreference;
    private SwitchPreference bootCompletedPreference;

    public MainFragment() {
    }

    @SuppressLint("ValidFragment")
    public MainFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main);

        enableFloatWindowPreference = (SwitchPreference) findPreference(ENABLE_FLOAT_WINDOW);
        bootCompletedPreference = (SwitchPreference) findPreference(BOOT_COMPLETED);

        initData();
        initListener();
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

    private void setPreferenceEnable(boolean enable) {
        enableFloatWindowPreference.setEnabled(enable);
        bootCompletedPreference.setEnabled(enable);
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
