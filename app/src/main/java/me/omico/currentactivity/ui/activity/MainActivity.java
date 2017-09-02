package me.omico.currentactivity.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import me.omico.currentactivity.R;
import me.omico.currentactivity.ui.fragment.MainFragment;
import me.omico.util.StatusBarUtils;

/**
 * @author Omico
 */

public final class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getFragmentManager().beginTransaction().replace(R.id.main_fragment, new MainFragment()).commit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            StatusBarUtils.setStatusBarColor(this, R.color.colorPrimary);
    }
}