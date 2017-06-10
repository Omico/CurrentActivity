package me.omico.currentactivity.service;

import android.annotation.TargetApi;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;

import me.omico.currentactivity.R;
import me.omico.util.ServiceUtils;
import me.omico.util.root.SU;

/**
 * Created by yuwen on 17-6-9.
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

        if (SU.isRoot()) {
            if (ServiceUtils.isRunning(getApplicationContext(), FloatViewService.class.getName())) {
                setTile(icon, getString(R.string.enable), Tile.STATE_ACTIVE);
            } else {
                setTile(icon.setTint(0x80ffffff), getString(R.string.disable), Tile.STATE_INACTIVE);
            }
        } else {
            setTile(icon.setTint(0x80ffffff), getString(R.string.unavailable), Tile.STATE_UNAVAILABLE);
            Toast.makeText(getApplicationContext(), getString(R.string.quick_settings_tile_is_unavailable), Toast.LENGTH_LONG).show();
        }
        tile.updateTile();
    }

    @Override
    public void onClick() {
        super.onClick();
        switch (tile.getState()) {
            case Tile.STATE_ACTIVE:
                ServiceUtils.stopService(getApplicationContext(), FloatViewService.class);
                setTile(icon.setTint(0x80ffffff), getString(R.string.disable), Tile.STATE_INACTIVE);
                break;
            case Tile.STATE_INACTIVE:
                ServiceUtils.startService(getApplicationContext(), FloatViewService.class);
                setTile(icon, getString(R.string.enable), Tile.STATE_ACTIVE);
                break;
        }
        tile.updateTile();
    }

    private void setTile(Icon icon, String label, int state) {
        tile.setIcon(icon);
        tile.setLabel(label);
        tile.setContentDescription(label);
        tile.setState(state);
    }
}
