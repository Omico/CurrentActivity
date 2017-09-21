package me.omico.currentactivity.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import static me.omico.currentactivity.CurrentActivity.ACTION_QUICK_START;
import static me.omico.currentactivity.CurrentActivity.EXTRA_COME_FROM_ASSISTANT;

/**
 * @author Omico 2017/8/18
 */

public class SplashActivityFromAssistant extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setAction(ACTION_QUICK_START);
        intent.putExtra(EXTRA_COME_FROM_ASSISTANT, true);
        startActivity(intent);
        SplashActivityFromAssistant.this.finish();
    }
}
