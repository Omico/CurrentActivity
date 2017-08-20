package me.omico.currentactivity.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import me.omico.currentactivity.provider.Settings;

/**
 * @author Omico 2017/8/18
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Settings.getBoolean(Settings.EXTRA_FIRST_OPEN, true)) {
            startActivity(new Intent(this, GuideActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }
}
