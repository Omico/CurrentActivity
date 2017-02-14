package me.omico.currentactivity.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import me.omico.currentactivity.BuildConfig;
import me.omico.currentactivity.R;
import me.omico.util.ClipboardUtils;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_about);

        TextView appVersion = (TextView) findViewById(R.id.app_version);
        TextView about = (TextView) findViewById(R.id.about);

        appVersion.setText(String.format(getString(R.string.version),
                BuildConfig.VERSION_NAME, BuildConfig.BUILD_TYPE, BuildConfig.VERSION_CODE));

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtils.copyToClipboard(AboutActivity.this, R.string.open_source_address);
            }
        });
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
