package me.omico.currentactivity.service;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;

import java.util.Objects;

import me.omico.currentactivity.R;
import me.omico.currentactivity.provider.Settings;
import me.omico.currentactivity.ui.activity.GuideActivity;
import me.omico.util.ServiceUtils;
import me.omico.util.StatusBarUtils;

import static me.omico.currentactivity.CurrentActivity.ACTION_FLOAT_VIEW_SERVICE_START;
import static me.omico.currentactivity.CurrentActivity.ACTION_FLOAT_VIEW_SERVICE_STOP;
import static me.omico.currentactivity.CurrentActivity.EXTRA_COME_FROM_TILE_SERVICE;
import static me.omico.currentactivity.CurrentActivity.EXTRA_SETUP_STEP;
import static me.omico.currentactivity.CurrentActivity.EXTRA_WORKING_MODE;
import static me.omico.currentactivity.ui.activity.GuideActivity.MODE_NONE;
import static me.omico.currentactivity.ui.activity.GuideActivity.PAGE_WELCOME;

/**
 * @author Omico 2017/6/9
 */

@TargetApi(Build.VERSION_CODES.N)
public class CurrentActivityTileService extends TileService {

    private Tile tile;
    private Icon icon;

    @Override
    public void onStartListening() {
        super.onStartListening();
        tile = getQsTile();
        icon = Icon.createWithResource(getApplicationContext(), R.mipmap.ic_launcher);
        if (ServiceUtils.isRunning(getApplicationContext(), FloatViewService.class.getName())) {
            setEnableTile();
        } else {
            setDisableTile();
        }
        tile.updateTile();
    }

    @Override
    public void onClick() {
        super.onClick();
        if (Objects.equals(Settings.getString(Settings.Mode.SELECTION, Settings.Mode.NONE), Settings.Mode.NONE)) {
            intentGuideActivity();
        } else {
            switch (tile.getState()) {
                case Tile.STATE_ACTIVE:
                    setDisableTile();
                    setAction(ACTION_FLOAT_VIEW_SERVICE_STOP);
                    break;
                case Tile.STATE_INACTIVE:
                    setEnableTile();
                    setAction(ACTION_FLOAT_VIEW_SERVICE_START);
                    break;
            }
            StatusBarUtils.collapseStatusBar(this);
        }
        tile.updateTile();
    }

    private void intentGuideActivity() {
        Intent intent = new Intent(this, GuideActivity.class);
        intent.putExtra(EXTRA_SETUP_STEP, PAGE_WELCOME);
        intent.putExtra(EXTRA_WORKING_MODE, MODE_NONE);
        intent.putExtra(EXTRA_COME_FROM_TILE_SERVICE, true);
        startActivity(intent);
        StatusBarUtils.collapseStatusBar(this);
        Toast.makeText(getApplicationContext(), getString(R.string.quick_settings_tile_is_unavailable), Toast.LENGTH_LONG).show();
    }

    private void setEnableTile() {
        setTile(icon, getString(R.string.enable), Tile.STATE_ACTIVE);
    }

    private void setDisableTile() {
        setTile(icon.setTint(0x80ffffff), getString(R.string.disable), Tile.STATE_INACTIVE);
    }

    private void setAction(String action) {
        ServiceUtils.startService(this, FloatViewService.class, action);
    }

    private void setTile(Icon icon, String label, int state) {
        tile.setIcon(icon);
        tile.setLabel(label);
        tile.setContentDescription(label);
        tile.setState(state);
    }
}
