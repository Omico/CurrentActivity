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
package me.omico.currentactivity.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import me.omico.currentactivity.core.common.resources.R
import me.omico.currentactivity.monitor.MonitorServiceStatus
import me.omico.currentactivity.monitor.isBusy
import me.omico.currentactivity.shizuku.ShizukuBinderStatus
import me.omico.currentactivity.shizuku.ShizukuPermissionStatus
import me.omico.currentactivity.shizuku.ShizukuStatus
import me.omico.currentactivity.shizuku.isGranted
import me.omico.currentactivity.ui.animatedVisibilityItem
import me.omico.currentactivity.ui.heightSpacer
import me.omico.currentactivity.ui.rememberMonitorServiceStatus
import me.omico.currentactivity.ui.rememberShizukuStatus
import me.omico.currentactivity.ui.rememberUpdatedStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeUi(
    checkCanDrawOverlays: () -> Boolean,
    onSubmitHomeAction: OnSubmitHomeAction,
) {
    val shizukuStatus by rememberShizukuStatus()
    val monitorServiceStatus: MonitorServiceStatus by rememberMonitorServiceStatus()
    val canDrawOverlays by rememberUpdatedStateWithLifecycle(
        initialValue = false,
        minActiveState = Lifecycle.State.RESUMED,
        updater = checkCanDrawOverlays,
    )
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding,
        ) {
            heightSpacer()
            item {
                ShizukuStatusCard(
                    shizukuStatus = shizukuStatus,
                    onSubmitHomeAction = onSubmitHomeAction,
                )
            }
            if (!canDrawOverlays) heightSpacer()
            animatedVisibilityItem(
                modifier = Modifier.fillMaxWidth(),
                visible = !canDrawOverlays,
                content = {
                    RequireOverlayPermissionCard(
                        onClick = { onSubmitHomeAction(HomeAction.NavigateToOverlayPermissionManager) },
                    )
                },
            )
            heightSpacer()
            item {
                MonitorSwitchCard(
                    shizukuStatus = shizukuStatus,
                    monitorServiceStatus = monitorServiceStatus,
                    canDrawOverlays = canDrawOverlays,
                    onSubmitHomeAction = onSubmitHomeAction,
                )
            }
        }
    }
}

@Composable
private fun ShizukuStatusCard(
    shizukuStatus: ShizukuStatus,
    onSubmitHomeAction: OnSubmitHomeAction,
) {
    LaunchedEffect(shizukuStatus) {
        when (shizukuStatus) {
            is ShizukuPermissionStatus.Denied -> onSubmitHomeAction(HomeAction.RequestShizukuPermission)
            else -> return@LaunchedEffect
        }
    }
    Card(modifier = Modifier.padding(horizontal = 16.dp)) {
        Box(
            modifier = run {
                Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = {
                            when (shizukuStatus) {
                                is ShizukuBinderStatus.WaitingForBinder,
                                is ShizukuBinderStatus.BinderDead,
                                -> onSubmitHomeAction(HomeAction.NavigateToShizukuManager)
                                is ShizukuPermissionStatus.Denied,
                                -> onSubmitHomeAction(HomeAction.RequestShizukuPermission)
                                else -> return@clickable
                            }
                        },
                    )
                    .padding(all = 16.dp)
            },
        ) {
            Text(
                text = when (shizukuStatus) {
                    is ShizukuStatus.Initializing -> "Initializing Shizuku..."
                    is ShizukuStatus.ShizukuManagerNotFound -> "Shizuku manager not found"
                    is ShizukuBinderStatus.BinderDead -> "Shizuku binder is dead"
                    is ShizukuBinderStatus.BinderReceived -> "Shizuku binder is received"
                    is ShizukuBinderStatus.WaitingForBinder -> "Waiting for Shizuku binder..."
                    is ShizukuPermissionStatus.Denied -> "Shizuku permission denied"
                    is ShizukuPermissionStatus.Granted -> "Shizuku is ready"
                },
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun RequireOverlayPermissionCard(
    onClick: () -> Unit = {},
) {
    Card(modifier = Modifier.padding(horizontal = 16.dp)) {
        Box(
            modifier = run {
                Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick)
                    .padding(all = 16.dp)
            },
        ) {
            Text(
                text = "Overlay permission is not granted",
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun MonitorSwitchCard(
    shizukuStatus: ShizukuStatus,
    monitorServiceStatus: MonitorServiceStatus,
    canDrawOverlays: Boolean,
    onSubmitHomeAction: OnSubmitHomeAction,
) {
    Card(modifier = Modifier.padding(horizontal = 16.dp)) {
        Box(modifier = Modifier.padding(all = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Enable Monitor",
                    style = MaterialTheme.typography.titleMedium,
                )
                Switch(
                    checked = monitorServiceStatus == MonitorServiceStatus.Started,
                    onCheckedChange = { checked ->
                        onSubmitHomeAction(HomeAction.MonitorSwitchStatusChanged(checked = checked))
                    },
                    enabled = canDrawOverlays && shizukuStatus.isGranted && !monitorServiceStatus.isBusy,
                )
            }
        }
    }
}
