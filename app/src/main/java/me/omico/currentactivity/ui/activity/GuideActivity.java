package me.omico.currentactivity.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.omico.currentactivity.R;
import me.omico.currentactivity.base.activity.SetupWizardBaseActivity;
import me.omico.currentactivity.provider.Settings;
import me.omico.currentactivity.service.CurrentActivityAccessibilityService;
import me.omico.util.AccessibilityServiceUtils;
import me.omico.util.ActivityCollector;
import me.omico.util.ActivityUtils;
import me.omico.util.StatusBarUtils;
import me.omico.util.device.CheckOSVariant;
import me.omico.util.root.SU;

import static me.omico.currentactivity.CurrentActivity.EXTRA_COME_FROM_MAIN;
import static me.omico.currentactivity.CurrentActivity.EXTRA_COME_FROM_TILE_SERVICE;
import static me.omico.currentactivity.CurrentActivity.EXTRA_SETUP_STEP;
import static me.omico.currentactivity.CurrentActivity.EXTRA_WORKING_MODE;
import static me.omico.currentactivity.CurrentActivity.PERMISSION_CODE_ACCESSIBILITY_SERVICE;
import static me.omico.currentactivity.CurrentActivity.PERMISSION_CODE_OVERLAY;
import static me.omico.currentactivity.provider.Settings.FIRST_OPEN;
import static me.omico.util.device.CheckOSVariant.FLYME;
import static me.omico.util.device.CheckOSVariant.ZUI;

/**
 * @author Omico 2017/8/15
 */

public class GuideActivity extends SetupWizardBaseActivity implements View.OnClickListener {

    public final static int PAGE_WELCOME = 0;
    public final static int PAGE_MODE_SELECTION = 1;
    public final static int PAGE_GETTING_MODE_PERMISSION = 2;
    public final static int PAGE_GETTING_OVERLAY_PERMISSION = 3;

    public final static int MODE_NONE = -1;
    public final static int MODE_ROOT = 0;
    public final static int MODE_ACCESSIBILITY_SERVICE = 1;

    private int setupStep;
    private int workingMode;

    private static long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            StatusBarUtils.setStatusBarColor(this, R.color.suw_status_bar);

        setupStep = getIntent().getIntExtra(EXTRA_SETUP_STEP, PAGE_WELCOME);
        workingMode = getIntent().getIntExtra(EXTRA_WORKING_MODE, MODE_NONE);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastClickTime < 500) return;

        lastClickTime = System.currentTimeMillis();

        if (getIntent().getBooleanExtra(EXTRA_COME_FROM_TILE_SERVICE, false) && setupStep == PAGE_WELCOME)
            ActivityCollector.getActivityCollector().removeAllActivity();

        if (getIntent().getBooleanExtra(EXTRA_COME_FROM_MAIN, false)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(EXTRA_SETUP_STEP, setupStep);
            intent.putExtra(EXTRA_WORKING_MODE, workingMode);
            intent.putExtra(EXTRA_COME_FROM_MAIN, false);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNavigateBack() {
        onBackPressed();
    }

    @Override
    protected void onNavigateNext() {
        switch (setupStep) {
            case PAGE_WELCOME:
            case PAGE_MODE_SELECTION:
            case PAGE_GETTING_MODE_PERMISSION:
                if (noNeedIntentDrawOverlayStep() && setupStep == PAGE_GETTING_MODE_PERMISSION) {
                    intentMainActivity();
                } else {
                    intentNextStep();
                }
                break;
            case PAGE_GETTING_OVERLAY_PERMISSION:
                intentMainActivity();
                break;
        }
    }

    private boolean noNeedIntentDrawOverlayStep() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && android.provider.Settings.canDrawOverlays(this));
    }

    private void intentNextStep() {
        Intent intent = new Intent(this, GuideActivity.class);
        intent.putExtra(EXTRA_SETUP_STEP, setupStep + 1);
        intent.putExtra(EXTRA_WORKING_MODE, workingMode);
        startActivity(intent);
    }

    private void intentMainActivity() {
        Settings.putBoolean(FIRST_OPEN, false);
        ActivityUtils.startActivity(this, MainActivity.class);
        ActivityCollector.getActivityCollector().removeAllActivity();
    }

    @Override
    protected void initLayout(ViewGroup viewGroup) {
        switch (setupStep) {
            case PAGE_WELCOME:
                getNavigationBar().getBackButton().setVisibility(View.GONE);
                initLayout(viewGroup, R.layout.suw_introduction, R.string.suw_welcome, true);
                break;
            case PAGE_MODE_SELECTION:
                initLayout(viewGroup, R.layout.suw_working_mode, R.string.suw_working_mode, false);
                initViewOnClickListener(R.id.suw_mode_root);
                initViewOnClickListener(R.id.suw_mode_accessibility_service);
                break;
            case PAGE_GETTING_MODE_PERMISSION:
                if (noNeedIntentDrawOverlayStep())
                    getNavigationBar().getNextButton().setText(R.string.suw_finish);
                switch (workingMode) {
                    case MODE_ROOT:
                        initLayout(viewGroup, R.layout.suw_mode_root, R.string.suw_mode_root, false);
                        initViewOnClickListener(R.id.suw_mode_root_check_button);
                        break;
                    case MODE_ACCESSIBILITY_SERVICE:
                        initLayout(viewGroup, R.layout.suw_mode_accessibility_service, R.string.suw_mode_accessibility_service, false);
                        initViewOnClickListener(R.id.suw_mode_accessibility_service_intent_setting);
                        initViewOnClickListener(R.id.suw_mode_accessibility_service_check_button);
                        break;
                }
                break;
            case PAGE_GETTING_OVERLAY_PERMISSION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getNavigationBar().getNextButton().setText(R.string.suw_finish);

                    initLayout(viewGroup, R.layout.suw_draw_overlay, R.string.suw_draw_overlay, false);
                    initViewOnClickListener(R.id.suw_draw_overlays_intent_setting);
                    initViewOnClickListener(R.id.suw_draw_overlays_check_button);

                    createOSVariantIntent();
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.suw_mode_root:
                setWorkingMode(MODE_ROOT);
                break;
            case R.id.suw_mode_accessibility_service:
                setWorkingMode(MODE_ACCESSIBILITY_SERVICE);
                break;
            case R.id.suw_mode_root_check_button:
                boolean isRooted = SU.isRooted();
                initColorTextView(R.id.suw_mode_root_desc, isRooted, R.string.suw_check_success, R.string.suw_check_root_fail);
                setNavigationBarNextButtonEnable(isRooted);
                if (isRooted) Settings.putString(Settings.Mode.SELECTION, Settings.Mode.ROOT);
                break;
            case R.id.suw_mode_accessibility_service_intent_setting:
                intentAccessibilitySetting();
                break;
            case R.id.suw_mode_accessibility_service_check_button:
                setAccessibilityServiceState();
                break;
            case R.id.suw_draw_overlays_intent_setting:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) intentDrawOverlaysSetting();
                break;
            case R.id.suw_draw_overlays_check_button:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) setDrawOverlaysState();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PERMISSION_CODE_ACCESSIBILITY_SERVICE:
                setAccessibilityServiceState();
                break;
            case PERMISSION_CODE_OVERLAY:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) setDrawOverlaysState();
                break;
        }
    }

    private void intentDrawOverlaysSetting() {
        startActivityForResult(createOSVariantIntent(), PERMISSION_CODE_OVERLAY);
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    private Intent createOSVariantIntent() {
        Intent intent;
        @StringRes int tip;

        CheckOSVariant checkOSVariant = new CheckOSVariant().init();

        switch (checkOSVariant.getOSVariant()) {
            case ZUI:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setClassName("com.zui.appsmanager", "com.zui.appsmanager.MainActivity");
                tip = R.string.suw_draw_overlay_check_os_variant_zui;
                break;
            case FLYME:
                intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
                intent.putExtra("packageName", getPackageName());
                tip = R.string.suw_draw_overlay_check_os_variant_flyme;
                break;
            default:
                intent = new Intent(
                        android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName())
                );
                tip = R.string.suw_draw_overlay_check_os_variant_common;
                break;
        }

        initColorTextView(R.id.suw_draw_overlays_desc, tip, R.color.black);

        if (android.provider.Settings.canDrawOverlays(this)) {
            initColorTextView(R.id.suw_draw_overlays_desc, R.string.suw_check_success, R.color.green);
            setNavigationBarNextButtonEnable(true);
        }

        return intent;
    }

    private void initColorTextView(@IdRes int id, boolean isSuccess, @StringRes int successStringId, @StringRes int failStringId) {
        TextView textView = findViewById(id);
        textView.setText(isSuccess ? successStringId : failStringId);
        textView.setTextColor(ContextCompat.getColor(this, isSuccess ? R.color.green : R.color.red));
    }

    private void initColorTextView(@IdRes int id, @StringRes int text, @ColorRes int color) {
        TextView textView = findViewById(id);
        textView.setText(text);
        textView.setTextColor(ContextCompat.getColor(this, color));
    }

    private void initViewOnClickListener(@IdRes int id) {
        findViewById(id).setOnClickListener(this);
    }

    private void setWorkingMode(int mode) {
        workingMode = mode;
        setNavigationBarNextButtonEnable(true);
    }

    private void setAccessibilityServiceState() {
        boolean hasPermission = AccessibilityServiceUtils.isAccessibilityServiceEnabled(this, CurrentActivityAccessibilityService.class);
        initColorTextView(R.id.suw_mode_accessibility_service_desc, hasPermission, R.string.suw_check_success, R.string.suw_check_fail);
        setNavigationBarNextButtonEnable(hasPermission);
        if (hasPermission)
            Settings.putString(Settings.Mode.SELECTION, Settings.Mode.ACCESSIBILITY_SERVICE);
    }

    public void intentAccessibilitySetting() {
        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent, PERMISSION_CODE_ACCESSIBILITY_SERVICE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setDrawOverlaysState() {
        boolean hasPermission = android.provider.Settings.canDrawOverlays(this);
        initColorTextView(R.id.suw_draw_overlays_desc, hasPermission, R.string.suw_check_success, R.string.suw_check_fail);
        setNavigationBarNextButtonEnable(hasPermission);
    }

    private void initLayout(ViewGroup viewGroup, @LayoutRes int layout, @StringRes int title, boolean nextButtonEnable) {
        View inflate = LayoutInflater.from(this).inflate(layout, viewGroup, false);
        viewGroup.addView(inflate);
        getSetupWizardLayout().setHeaderText(title);
        setNavigationBarNextButtonEnable(nextButtonEnable);
    }

    private void setNavigationBarNextButtonEnable(boolean enable) {
        getNavigationBar().getNextButton().setEnabled(enable);
    }
}

