package me.omico.currentactivity.ui.settings.preference

import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import me.omico.core.event.EventBus
import me.omico.core.event.getLiveData
import me.omico.core.preference.PreferenceDelegate
import me.omico.core.preference.preference
import me.omico.currentactivity.event.Events
import me.omico.currentactivity.ui.floatingmonitor.FloatingMonitorServiceController
import me.omico.currentactivity.ui.settings.SettingsViewModel

/**
 * @author Omico 2020/12/27
 */
class FloatingMonitorSwitchPreferenceDelegate(
        override val host: PreferenceFragmentCompat,
        private val viewModel: SettingsViewModel
) : PreferenceDelegate {

    private val floatingMonitorSwitch: SwitchPreference by preference(PreferenceKeys.FLOATING_MONITOR)

    private var isServiceRunning: Boolean = false

    override fun onCreate() {
        floatingMonitorSwitch.run {
            isChecked = false
            setOnPreferenceChangeListener { _, newValue ->
                val isChecked = newValue as Boolean
                when {
                    isChecked -> FloatingMonitorServiceController.startForeground(context)
                    else -> stopFloatingMonitorService()
                }
                true
            }
        }
        EventBus.getLiveData(Events.IS_FLOATING_MONITOR_SERVICE_RUNNING, false).observe(host) {
            floatingMonitorSwitch.isChecked = it
            isServiceRunning = it
        }
        viewModel.isFloatingMonitorSwitchEnable.observe(host) {
            floatingMonitorSwitch.isEnabled = it
            if (!it) stopFloatingMonitorService()
        }
    }

    private fun stopFloatingMonitorService() {
        if (isServiceRunning) FloatingMonitorServiceController.stopForeground(context)
    }
}
