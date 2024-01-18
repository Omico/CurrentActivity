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
package me.omico.currentactivity.ui.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import me.omico.currentactivity.ui.LocalMonitorServiceController
import me.omico.currentactivity.ui.monitor.internal.InternalMonitorServiceController
import me.omico.currentactivity.ui.navigation.Route
import me.omico.currentactivity.ui.navigation.homeScreen
import me.omico.currentactivity.ui.theme.CurrentActivityTheme

@Composable
fun CurrentActivityApp(
    darkTheme: Boolean = isSystemInDarkTheme(),
) {
    CurrentActivityTheme(darkTheme = darkTheme) {
        CompositionLocalProvider(
            LocalMonitorServiceController provides InternalMonitorServiceController,
            content = { CurrentActivityNavHost() },
        )
    }
}

@Composable
private fun CurrentActivityNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Route.HOME,
    ) {
        homeScreen()
    }
}
