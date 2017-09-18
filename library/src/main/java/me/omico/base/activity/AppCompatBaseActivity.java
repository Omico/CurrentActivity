package me.omico.base.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import me.omico.util.StatusBarUtils;

/**
 * @author Omico 2017/9/15
 */

public abstract class AppCompatBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.setContentView());

        setSupportActionBar((Toolbar) findViewById(this.setSupportActionBar()));
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(this.setDisplayHomeAsUpEnabled());
            actionBar.setTitle(this.setTitle());
        }
    }

    protected void setStatusBarColor(@ColorRes int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            StatusBarUtils.setStatusBarColor(this, statusBarColor);
    }

    protected void replaceSupportFragment(@IdRes int fragmentContainer, Context context, @NonNull String fragmentName) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(fragmentContainer, Fragment.instantiate(context, fragmentName))
                .commit();
    }

    @LayoutRes
    protected abstract int setContentView();

    @IdRes
    protected abstract int setSupportActionBar();

    protected abstract boolean setDisplayHomeAsUpEnabled();

    protected abstract CharSequence setTitle();
}
