package me.omico.currentactivity.base.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.android.setupwizardlib.SetupWizardLayout;
import com.android.setupwizardlib.view.NavigationBar;

import me.omico.currentactivity.R;
import me.omico.util.ActivityCollector;

/**
 * @author Omico 2017/8/15
 */

public abstract class SetupWizardBaseActivity extends Activity {

    private SetupWizardLayout setupWizardLayout;
    private NavigationBar navigationBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.getActivityCollector().addActivity(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupWizardLayout = findViewById(R.id.setup_wizard_layout);
        navigationBar = setupWizardLayout.getNavigationBar();

        ViewGroup viewGroup = findViewById(R.id.fragment_container);

        navigationBar.setNavigationBarListener(new NavigationBar.NavigationBarListener() {
            @Override
            public void onNavigateBack() {
                SetupWizardBaseActivity.this.onNavigateBack();
            }

            @Override
            public void onNavigateNext() {
                SetupWizardBaseActivity.this.onNavigateNext();
            }
        });

        initLayout(viewGroup);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.getActivityCollector().removeActivity(this);
    }

    public SetupWizardLayout getSetupWizardLayout() {
        return setupWizardLayout;
    }

    public NavigationBar getNavigationBar() {
        return navigationBar;
    }

    protected abstract void onNavigateBack();

    protected abstract void onNavigateNext();

    protected abstract void initLayout(ViewGroup viewGroup);
}
