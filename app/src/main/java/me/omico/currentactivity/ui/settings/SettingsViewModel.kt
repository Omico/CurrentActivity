package me.omico.currentactivity.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * @author Omico 2020/12/29
 */
class SettingsViewModel : ViewModel() {

    private val _canDrawOverlays = MutableStateFlow(false)

    private val _shizukuState = MutableStateFlow<ShizukuState>(ShizukuState.Working)
    var shizukuState: LiveData<ShizukuState> = _shizukuState.asLiveData()

    private val _isFloatingMonitorSwitchEnable = MutableStateFlow(false)
    var isFloatingMonitorSwitchEnable: LiveData<Boolean> = _isFloatingMonitorSwitchEnable.asLiveData()

    init {
        updateFloatingMonitorSwitchState()
    }

    fun updateDrawOverlaysState(canDrawOverlays: Boolean) {
        _canDrawOverlays.value = canDrawOverlays
    }

    fun updateShizukuState(state: ShizukuState) {
        _shizukuState.value = state
    }

    private fun updateFloatingMonitorSwitchState() {
        viewModelScope.launch {
            _canDrawOverlays.combine(_shizukuState) { canDrawOverlays, shizukuState ->
                canDrawOverlays && shizukuState == ShizukuState.Working
            }.collect {
                _isFloatingMonitorSwitchEnable.emit(it)
            }
        }
    }

    sealed class ShizukuState {
        object NotInstalled : ShizukuState()
        object Unauthorized : ShizukuState()
        object NotRunning : ShizukuState()
        object Working : ShizukuState()
    }
}
