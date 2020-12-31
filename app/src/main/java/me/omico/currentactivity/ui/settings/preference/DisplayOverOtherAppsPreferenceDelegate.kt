package me.omico.currentactivity.ui.settings.preference

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import me.omico.core.preference.PreferenceDelegate
import me.omico.core.preference.preference
import me.omico.currentactivity.ui.settings.SettingsViewModel

/**
 * @author Omico 2020/12/30
 */
class DisplayOverOtherAppsPreferenceDelegate(
        override val host: PreferenceFragmentCompat,
        private val viewModel: SettingsViewModel
) : PreferenceDelegate {

    private val displayOverOtherApps: Preference by preference(PreferenceKeys.DISPLAY_OVER_OTHER_APPS)
    private val requestDisplayOverOtherAppsPermission = host.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        displayOverOtherApps.isVisible = when (it.resultCode) {
            Activity.RESULT_OK -> false
            else -> true
        }
    }

    override fun onCreate() {
        viewModel.updateDrawOverlaysState(Settings.canDrawOverlays(context))
        displayOverOtherApps.setOnPreferenceClickListener {
            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).also {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) it.data = Uri.parse("package:" + context.packageName)
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                requestDisplayOverOtherAppsPermission.launch(it)
            }
            true
        }
    }

    override fun onUpdatePreference() {
        val canDrawOverlays = Settings.canDrawOverlays(context)
        viewModel.updateDrawOverlaysState(canDrawOverlays)
        displayOverOtherApps.isVisible = !canDrawOverlays
    }
}
