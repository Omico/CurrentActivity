package me.omico.currentactivity.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import java.util.Collections;

import me.omico.currentactivity.R;
import me.omico.currentactivity.provider.Settings;
import me.omico.currentactivity.service.FloatViewService;
import me.omico.util.ActivityCollector;
import me.omico.util.ActivityUtils;
import me.omico.util.ServiceUtils;

import static me.omico.currentactivity.CurrentActivity.ACTION_FLOAT_VIEW_SERVICE_START;
import static me.omico.currentactivity.CurrentActivity.ACTION_FLOAT_VIEW_SERVICE_STOP;
import static me.omico.currentactivity.CurrentActivity.ACTION_QUICK_START_OR_QUICK_STOP;
import static me.omico.currentactivity.CurrentActivity.EXTRA_COME_FROM_SHORTCUT;
import static me.omico.currentactivity.provider.Settings.FIRST_OPEN;
import static me.omico.currentactivity.provider.Settings.OPEN_MAIN_ACTIVITY_WHEN_QUICK_START_OR_QUICK_STOP;

/**
 * @author Omico 2017/8/18
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparentBackground();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) createShortcut();

        if (Settings.getBoolean(FIRST_OPEN, true)) {
            ActivityUtils.startActivity(this, GuideActivity.class);
        } else if (isQuickStartOrQuickStop()) {
            if (Settings.getBoolean(OPEN_MAIN_ACTIVITY_WHEN_QUICK_START_OR_QUICK_STOP, false)) {
                ActivityUtils.startActivity(this, MainActivity.class);
            } else {
                ActivityCollector.getActivityCollector().removeAllActivity();
            }
            boolean isRunning = ServiceUtils.isRunning(getApplicationContext(), FloatViewService.class.getName());
            ServiceUtils.startService(this, FloatViewService.class, isRunning ? ACTION_FLOAT_VIEW_SERVICE_STOP : ACTION_FLOAT_VIEW_SERVICE_START);
        } else {
            ActivityUtils.startActivity(this, MainActivity.class);
        }
        SplashActivity.this.finish();
    }

    private boolean isQuickStartOrQuickStop() {
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            if (action != null)
                return action.equals(ACTION_QUICK_START_OR_QUICK_STOP) || action.equals(Intent.ACTION_ASSIST);
            return intent.getBooleanExtra(EXTRA_COME_FROM_SHORTCUT, false);
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    private void createShortcut() {
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

        Intent intent = new Intent(this, SplashActivity.class);
        intent.setAction(ACTION_QUICK_START_OR_QUICK_STOP);
        intent.putExtra(EXTRA_COME_FROM_SHORTCUT, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "shortcut_quick_switch")
                .setShortLabel(getString(R.string.shortcut_quick_switch))
                .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher))
                .setIntent(intent)
                .build();

        if (shortcutManager != null) {
            shortcutManager.setDynamicShortcuts(Collections.singletonList(shortcut));
        }
    }

    private void setTransparentBackground() {
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.getDecorView().setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
    }
}
