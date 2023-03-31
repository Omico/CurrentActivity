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

import androidx.core.content.PermissionChecker
import rikka.shizuku.Shizuku

sealed interface ShizukuStatus {
    object Initializing : ShizukuStatus
    object ShizukuManagerNotFound : ShizukuStatus
}

sealed interface ShizukuBinderStatus : ShizukuStatus {

    object WaitingForBinder : ShizukuBinderStatus

    data class BinderReceived(
        val requireUpdateShizukuManager: Boolean = false,
    ) : ShizukuBinderStatus

    object BinderDead : ShizukuBinderStatus
}

sealed interface ShizukuPermissionStatus : ShizukuStatus {

    object Granted : ShizukuPermissionStatus

    data class Denied(
        val requireUpdateShizukuManager: Boolean = false,
        val shouldShowRequestPermissionRationale: Boolean = false,
    ) : ShizukuPermissionStatus
}

fun checkShizukuPermissionStatus(
    requestPermissionAfterChecked: Boolean = false,
    requestPermissionCode: Int = 0,
): ShizukuPermissionStatus = when {
    Shizuku.isPreV11() -> ShizukuPermissionStatus.Denied(requireUpdateShizukuManager = true)
    Shizuku.checkSelfPermission() == PermissionChecker.PERMISSION_GRANTED -> ShizukuPermissionStatus.Granted
    Shizuku.shouldShowRequestPermissionRationale() ->
        ShizukuPermissionStatus.Denied(shouldShowRequestPermissionRationale = true)
    else -> ShizukuPermissionStatus.Denied()
        .also { if (requestPermissionAfterChecked) Shizuku.requestPermission(requestPermissionCode) }
}

inline val ShizukuStatus.isGranted: Boolean
    get() = this == ShizukuPermissionStatus.Granted
