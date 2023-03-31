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
package me.omico.currentactivity.ui.monitor.internal

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.omico.currentactivity.applicationContext
import me.omico.currentactivity.monitor.MonitorServiceController
import me.omico.currentactivity.monitor.MonitorServiceStatus
import me.omico.currentactivity.ui.monitor.MonitorService
import me.omico.currentactivity.utility.startForegroundService

val InternalMonitorServiceController: MonitorServiceController by lazy(::MonitorServiceControllerImpl)

private class MonitorServiceControllerImpl : MonitorServiceController {

    private val _status: MutableStateFlow<MonitorServiceStatus> =
        MutableStateFlow(MonitorServiceStatus.Stopped)
    override val status: StateFlow<MonitorServiceStatus> = _status.asStateFlow()

    override fun start() = applicationContext.startForegroundService<MonitorService> {
        action = MonitorService.ACTION_START
    }

    override fun stop() {
        applicationContext.startService(MonitorService.stopIntent)
    }

    override fun toggle() {
        when (status.value) {
            MonitorServiceStatus.Started -> stop()
            MonitorServiceStatus.Stopped -> start()
            else -> return
        }
    }

    override fun updateServiceStatus(status: MonitorServiceStatus) {
        _status.value = status
    }
}
