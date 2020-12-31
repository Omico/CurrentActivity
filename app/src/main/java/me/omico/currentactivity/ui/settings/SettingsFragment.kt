package me.omico.currentactivity.ui.settings

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceFragmentCompat
import me.omico.core.preference.addPreferenceDelegate
import me.omico.currentactivity.R
import me.omico.currentactivity.ui.settings.preference.DisplayOverOtherAppsPreferenceDelegate
import me.omico.currentactivity.ui.settings.preference.FloatingMonitorSwitchPreferenceDelegate
import me.omico.currentactivity.ui.settings.preference.ShizukuStatePreferenceDelegate

/**
 * @author Omico 2020/12/15
 */
class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
        addPreferenceDelegate(DisplayOverOtherAppsPreferenceDelegate(this@SettingsFragment, viewModel))
        addPreferenceDelegate(ShizukuStatePreferenceDelegate(this@SettingsFragment, viewModel))
        addPreferenceDelegate(FloatingMonitorSwitchPreferenceDelegate(this@SettingsFragment, viewModel))
    }
}
