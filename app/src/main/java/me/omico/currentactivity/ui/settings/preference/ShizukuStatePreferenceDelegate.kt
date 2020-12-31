package me.omico.currentactivity.ui.settings.preference

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import me.omico.core.permission.PermissionState
import me.omico.core.permission.permissionLauncher
import me.omico.core.preference.PreferenceDelegate
import me.omico.core.preference.preference
import me.omico.currentactivity.R
import me.omico.currentactivity.ui.settings.SettingsViewModel
import me.omico.currentactivity.ui.settings.SettingsViewModel.ShizukuState.Unauthorized
import me.omico.currentactivity.ui.settings.SettingsViewModel.ShizukuState.NotInstalled
import me.omico.currentactivity.ui.settings.SettingsViewModel.ShizukuState.NotRunning
import me.omico.currentactivity.ui.settings.SettingsViewModel.ShizukuState.Working
import me.omico.shizuku.checkShizukuPermission
import me.omico.shizuku.goToShizuku
import moe.shizuku.api.ShizukuApiConstants
import moe.shizuku.api.ShizukuProvider
import moe.shizuku.api.ShizukuService

/**
 * @author Omico 2020/12/27
 */
class ShizukuStatePreferenceDelegate(
        override val host: PreferenceFragmentCompat,
        private val viewModel: SettingsViewModel
) : PreferenceDelegate {

    private val shizukuState: Preference by preference(PreferenceKeys.SHIZUKU_STATE)
    private val permissionLauncher = host.permissionLauncher()

    override fun onCreate() {
        viewModel.shizukuState.observe(host) { state ->
            when (state) {
                NotInstalled -> {
                    shizukuState.isVisible = true
                    shizukuState.setTitle(R.string.preference_shizuku_state_title_shizuku_is_not_installed)
                    shizukuState.summary = null
                    shizukuState.onPreferenceClickListener = null
                }
                Unauthorized -> {
                    shizukuState.isVisible = true
                    shizukuState.setTitle(R.string.preference_shizuku_state_title_shizuku_unauthorized)
                    shizukuState.setSummary(R.string.preference_shizuku_state_summary_click_here_to_authorize)
                    when (context.checkShizukuPermission()) {
                        PermissionState.ShouldShowRequestPermissionRationale -> {
                            shizukuState.setOnPreferenceClickListener {
                                requestPermission()
                                true
                            }
                        }
                        PermissionState.Denied -> {
                            shizukuState.setOnPreferenceClickListener {
                                context.goToShizuku()
                                true
                            }
                        }
                        else -> Unit
                    }
                }
                NotRunning -> {
                    shizukuState.isVisible = true
                    shizukuState.setTitle(R.string.preference_shizuku_state_title_shizuku_is_not_running)
                    shizukuState.setSummary(R.string.preference_shizuku_state_summary_click_here_go_to_shizuku)
                    shizukuState.setOnPreferenceClickListener {
                        context.goToShizuku()
                        true
                    }
                }
                Working -> {
                    shizukuState.isVisible = false
                }
            }
        }
    }

    override fun onUpdatePreference() {
        val isShizukuInstalled = ShizukuProvider.isShizukuInstalled(context)
        val isShizukuServiceRunning = ShizukuService.pingBinder()
        val isShizukuAuthorized = context.checkShizukuPermission() == PermissionState.Granted
        val shizukuState = when {
            !isShizukuInstalled -> NotInstalled
            !isShizukuAuthorized -> Unauthorized
            !isShizukuServiceRunning -> NotRunning
            else -> Working
        }
        viewModel.updateShizukuState(shizukuState)
    }

    private fun requestPermission() {
        permissionLauncher.launch(ShizukuApiConstants.PERMISSION)
    }
}
