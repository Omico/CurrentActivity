/*
 * This file is part of CurrentActivity.
 *
 * Copyright (C) 2022-2023 Omico
 *
 * CurrentActivity is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * CurrentActivity is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CurrentActivity. If not, see <https://www.gnu.org/licenses/>.
 */
package me.omico.currentactivity.shizuku

import android.content.Context
import androidx.core.content.PermissionChecker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import rikka.shizuku.Shizuku

class ShizukuDelegate(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
    private val requestPermissionCode: Int = 0,
    private val requestPermissionAfterChecked: Boolean = false,
) : CoroutineScope by coroutineScope {

    private val _binderStatus: MutableStateFlow<ShizukuBinderStatus> =
        MutableStateFlow(ShizukuBinderStatus.WaitingForBinder)
    val binderStatus: StateFlow<ShizukuBinderStatus> = _binderStatus

    private val _permissionStatus: MutableStateFlow<ShizukuPermissionStatus> =
        MutableStateFlow(ShizukuPermissionStatus.Denied())
    val permissionStatus: StateFlow<ShizukuPermissionStatus> = _permissionStatus

    private val _status: MutableSharedFlow<ShizukuStatus> = MutableSharedFlow(replay = 0)
    val status: SharedFlow<ShizukuStatus> = _status.asSharedFlow()

    private val binderReceivedListener = Shizuku.OnBinderReceivedListener {
        launch {
            _binderStatus.emit(ShizukuBinderStatus.BinderReceived(requireUpdateShizukuManager = Shizuku.isPreV11()))
            if (requestPermissionAfterChecked) _permissionStatus.emit(checkShizukuPermissionStatus())
        }
    }

    private val binderDeadListener = Shizuku.OnBinderDeadListener {
        launch { _binderStatus.emit(ShizukuBinderStatus.BinderDead) }
    }

    private val requestPermissionResultListener =
        Shizuku.OnRequestPermissionResultListener { code, grantResult ->
            if (code != requestPermissionCode) return@OnRequestPermissionResultListener
            launch {
                val status = when (grantResult) {
                    PermissionChecker.PERMISSION_GRANTED -> ShizukuPermissionStatus.Granted
                    else -> ShizukuPermissionStatus.Denied(
                        requireUpdateShizukuManager = Shizuku.isPreV11(),
                        shouldShowRequestPermissionRationale = Shizuku.shouldShowRequestPermissionRationale(),
                    )
                }
                _permissionStatus.emit(status)
            }
        }

    init {
        collectStatus()
    }

    fun addListeners() {
        checkShizukuManagerInstalled()
        Shizuku.addBinderReceivedListenerSticky(binderReceivedListener)
        Shizuku.addBinderDeadListener(binderDeadListener)
        Shizuku.addRequestPermissionResultListener(requestPermissionResultListener)
    }

    fun removeListeners() {
        Shizuku.removeBinderReceivedListener(binderReceivedListener)
        Shizuku.removeBinderDeadListener(binderDeadListener)
        Shizuku.removeRequestPermissionResultListener(requestPermissionResultListener)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun collectStatus() {
        launch {
            flowOf(_binderStatus, _permissionStatus).flattenMerge(2).collect {
                if (_binderStatus.value is ShizukuBinderStatus.WaitingForBinder) return@collect
                _status.emit(it)
            }
        }
    }

    private fun checkShizukuManagerInstalled() {
        launch {
            if (context.isShizukuManagerInstalled) return@launch
            _status.emit(ShizukuStatus.ShizukuManagerNotFound)
        }
    }
}
