package me.omico.currentactivity.base.activity;

import android.os.Bundle;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import me.omico.util.StatusBarUtils;

/**
 * @author Omico 2017/9/15
 */

public abstract class AppCompatBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.setContentView());

        setSupportActionBar(findViewById(this.setSupportActionBar()));
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(this.setDisplayHomeAsUpEnabled());
            actionBar.setTitle(this.setTitle());
        }
    }

    protected void setStatusBarColor(@ColorRes int statusBarColor) {
        StatusBarUtils.setStatusBarColor(this, statusBarColor);
    }

    protected boolean setStatusBarDarkMode(boolean darkMode) {
        return StatusBarUtils.setStatusBarDarkMode(this, darkMode);
    }

    protected void replaceSupportFragment(@IdRes int fragmentContainer, @NonNull Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(fragmentContainer, fragment)
                .commit();
    }

    @LayoutRes
    protected abstract int setContentView();

    @IdRes
    protected abstract int setSupportActionBar();

    protected abstract boolean setDisplayHomeAsUpEnabled();

    protected abstract CharSequence setTitle();
}
