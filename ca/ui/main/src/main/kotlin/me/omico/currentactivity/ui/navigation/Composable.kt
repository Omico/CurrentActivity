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
package me.omico.currentactivity.ui.navigation

import android.provider.Settings
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import me.omico.currentactivity.shizuku.navigateToShizukuManager
import me.omico.currentactivity.ui.LocalMonitorServiceController
import me.omico.currentactivity.ui.home.HomeAction
import me.omico.currentactivity.ui.home.HomeUi
import me.omico.currentactivity.utility.navigateToOverlayPermissionManager
import rikka.shizuku.Shizuku

fun NavGraphBuilder.homeScreen() {
    composable(route = Route.HOME) {
        val context = LocalContext.current
        val monitorServiceController = LocalMonitorServiceController.current
        HomeUi(
            checkCanDrawOverlays = { Settings.canDrawOverlays(context) },
        ) { action ->
            when (action) {
                is HomeAction.RequestShizukuPermission ->
                    Shizuku.requestPermission(0)
                is HomeAction.MonitorSwitchStatusChanged ->
                    when {
                        action.checked -> monitorServiceController.start()
                        else -> monitorServiceController.stop()
                    }
                is HomeAction.NavigateToOverlayPermissionManager ->
                    context.navigateToOverlayPermissionManager()
                is HomeAction.NavigateToShizukuManager ->
                    context.navigateToShizukuManager()
            }
        }
    }
}
