package me.omico.currentactivity.ui.activity;

import android.os.Bundle;

import me.omico.base.activity.AppCompatBaseActivity;
import me.omico.currentactivity.R;
import me.omico.currentactivity.ui.fragment.SettingsFragment;

/**
 * @author Omico
 */

public class MainActivity extends AppCompatBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.colorPrimary);

        replaceSupportFragment(R.id.main_fragment, MainActivity.this,
                SettingsFragment.class.getCanonicalName());
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected int setSupportActionBar() {
        return R.id.toolbar;
    }

    @Override
    protected boolean setDisplayHomeAsUpEnabled() {
        return false;
    }

    @Override
    protected CharSequence setTitle() {
        return getString(R.string.app_name);
    }
}