package me.omico.currentactivity.ui.activity;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import me.omico.currentactivity.R;
import me.omico.currentactivity.service.FloatViewService;
import me.omico.util.ServiceUtils;
import me.omico.util.SharedPreferencesUtils;
import me.omico.util.root.SU;

import static me.omico.currentactivity.Constants.IS_FIRST_OPEN;
import static me.omico.currentactivity.Constants.OVERLAY_PERMISSION_CODE;

public final class MainActivity extends AppCompatActivity {

    private SwitchCompat swOpen;
    private TextView tvAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swOpen = (SwitchCompat) findViewById(R.id.switch_button);
        tvAbout = (TextView) findViewById(R.id.about);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        initData();
        initListener();
    }

    private void initData() {
        if (SharedPreferencesUtils.getDefaultSharedPreferences(this, IS_FIRST_OPEN, true)) {
            showNoticeDialog();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) showPermissionDialog();

        swOpen.setChecked(ServiceUtils.isRunning(this, FloatViewService.class.getName()));
    }

    private void initListener() {
        tvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE))
                        .setPrimaryClip(
                                ClipData.newPlainText(getString(R.string.app_name),
                                        getString(R.string.open_source_address)));

                showSnackBarNoAction("已复制到剪贴板", Snackbar.LENGTH_SHORT);
            }
        });

        swOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ServiceUtils.startService(MainActivity.this, FloatViewService.class);
                } else {
                    ServiceUtils.stopService(MainActivity.this, FloatViewService.class);
                }
            }
        });
    }

    private void showNoticeDialog() {
        addDialog(this, "注意", "此软件需要 ROOT 权限，6.0以上的应用需要额外权限")
                .setPositiveButton("接受，并授权", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        swOpen.setEnabled(SU.isRoot());
                        SharedPreferencesUtils.setDefaultSharedPreferences(MainActivity.this, IS_FIRST_OPEN, false);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) showPermissionDialog();
                    }
                })
                .setNegativeButton("放弃并退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        MainActivity.this.finish();
                    }
                }).create().show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void showPermissionDialog() {
        if (!Settings.canDrawOverlays(this)) {
            swOpen.setEnabled(false);
            addDialog(this, "注意", "需要额外授权“允许出现在其他应用上”的权限！")
                    .setPositiveButton("接受，并授权", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, OVERLAY_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("放弃并退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            MainActivity.this.finish();
                        }
                    }).create().show();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                showSnackBarNoAction("权限授予失败，无法开启悬浮窗", Snackbar.LENGTH_SHORT);
                swOpen.setEnabled(false);
            } else {
                showSnackBarNoAction("权限授予成功！", Snackbar.LENGTH_SHORT);
                swOpen.setEnabled(true);
            }
        }
    }

    private AlertDialog.Builder addDialog(Context context, String title, String message) {
        return new AlertDialog.Builder(context).setTitle(title).setMessage(message);
    }

    private void showSnackBarNoAction(String text, int time) {
        Snackbar.make(findViewById(R.id.main_coordinator_layout), text, time).show();
    }
}