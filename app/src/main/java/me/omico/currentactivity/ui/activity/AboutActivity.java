package me.omico.currentactivity.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import me.omico.currentactivity.BuildConfig;
import me.omico.currentactivity.R;
import me.omico.currentactivity.base.activity.AppCompatBaseActivity;
import me.omico.util.ClipboardUtils;

/**
 * @author Omico
 */

public class AboutActivity extends AppCompatBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.colorPrimaryDark);
        setStatusBarDarkMode(true);

        TextView appVersion = findViewById(R.id.app_version);
        TextView about = findViewById(R.id.about);

        appVersion.setText(
                String.format(
                        getString(R.string.version),
                        BuildConfig.VERSION_NAME, BuildConfig.BUILD_TYPE, BuildConfig.VERSION_CODE
                )
        );

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtils.copyToClipboard(AboutActivity.this, R.string.open_source_address);
            }
        });
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_about;
    }

    @Override
    protected int setSupportActionBar() {
        return R.id.toolbar;
    }

    @Override
    protected boolean setDisplayHomeAsUpEnabled() {
        return true;
    }

    @Override
    protected CharSequence setTitle() {
        return getString(R.string.about);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
