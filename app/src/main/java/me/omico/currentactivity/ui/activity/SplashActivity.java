package me.omico.currentactivity.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.Collections;

import me.omico.currentactivity.R;
import me.omico.currentactivity.provider.Settings;
import me.omico.currentactivity.service.FloatViewService;

import static me.omico.currentactivity.provider.Settings.ACTION_FLOAT_VIEW_SERVICE_START;
import static me.omico.currentactivity.provider.Settings.ACTION_QUICK_START;
import static me.omico.currentactivity.provider.Settings.EXTRA_SHORTCUT_OPEN;

/**
 * @author Omico 2017/8/18
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initQuickStartAndShortcut();

        if (Settings.getBoolean(Settings.EXTRA_FIRST_OPEN, true)) {
            startActivity(new Intent(this, GuideActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }

    private void initQuickStartAndShortcut() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) createShortcut();

        if (isQuickStartOrShortCutEnable())
            startService(new Intent(this, FloatViewService.class).setAction(ACTION_FLOAT_VIEW_SERVICE_START));
    }

    private boolean isQuickStartOrShortCutEnable() {
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) return action.equals(ACTION_QUICK_START);
            return intent.getBooleanExtra(EXTRA_SHORTCUT_OPEN, false);
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    private void createShortcut() {
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(ACTION_QUICK_START);
        intent.putExtra(EXTRA_SHORTCUT_OPEN, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "shortcut_open")
                .setShortLabel(getString(R.string.quick_enable))
                .setLongLabel(getString(R.string.enable_float_window))
                .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher))
                .setIntent(intent)
                .build();

        if (shortcutManager != null) {
            shortcutManager.setDynamicShortcuts(Collections.singletonList(shortcut));
        }
    }
}
