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
package me.omico.currentactivity.ui.monitor

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.omico.currentactivity.shizuku.ShizukuBinderStatus
import me.omico.currentactivity.shizuku.ShizukuPermissionStatus
import me.omico.currentactivity.shizuku.ShizukuStatus
import me.omico.currentactivity.ui.rememberCurrentActivityText
import me.omico.currentactivity.ui.rememberShizukuStatus
import rikka.shizuku.Shizuku

@Composable
fun MonitorUi() {
    val shizukuStatus by rememberShizukuStatus(requestPermissionAfterChecked = true)
    Box(
        modifier = run {
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        },
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
            ),
        ) {
            Crossfade(
                modifier = run {
                    Modifier
                        .fillMaxSize()
                        .clickable {
                            when (shizukuStatus) {
                                is ShizukuPermissionStatus.Denied -> Shizuku.requestPermission(0)
                                else -> return@clickable
                            }
                        }
                },
                targetState = shizukuStatus,
            ) { status ->
                when (status) {
                    is ShizukuStatus.Initializing -> MonitorText(text = "Initializing...")
                    is ShizukuBinderStatus.BinderDead -> MonitorText(text = "Binder dead")
                    is ShizukuBinderStatus.BinderReceived -> MonitorText(text = "Binder received")
                    is ShizukuBinderStatus.WaitingForBinder -> MonitorText(text = "Waiting for binder...")
                    is ShizukuStatus.ShizukuManagerNotFound -> MonitorText(text = "Shizuku manager not found")
                    is ShizukuPermissionStatus.Denied -> MonitorText(text = "Permission denied")
                    is ShizukuPermissionStatus.Granted -> CurrentActivityMonitorText()
                }
            }
        }
    }
}

@Composable
private fun MonitorText(
    contentAlignment: Alignment = Alignment.Center,
    text: String,
) {
    Box(
        modifier = run {
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        },
        contentAlignment = contentAlignment,
        content = { Text(text = text) },
    )
}

@Composable
private fun CurrentActivityMonitorText() {
    val text by rememberCurrentActivityText()
    MonitorText(
        contentAlignment = Alignment.CenterStart,
        text = text,
    )
}
