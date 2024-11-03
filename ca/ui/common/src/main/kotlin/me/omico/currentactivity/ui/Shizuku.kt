/*
 * This file is part of CurrentActivity.
 *
 * Copyright (C) 2022-2024 Omico
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
package me.omico.currentactivity.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.omico.currentactivity.shizuku.ShizukuBinderStatus
import me.omico.currentactivity.shizuku.ShizukuDelegate
import me.omico.currentactivity.shizuku.ShizukuStatus
import me.omico.currentactivity.shizuku.checkShizukuPermissionStatus
import me.omico.currentactivity.shizuku.isShizukuManagerInstalled
import me.omico.currentactivity.shizuku.topActivity

@Composable
fun rememberShizukuStatus(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    requestPermissionCode: Int = 0,
    requestPermissionAfterChecked: Boolean = false,
): MutableState<ShizukuStatus> {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val shizukuDelegate = remember(
        context,
        coroutineScope,
        requestPermissionCode,
        requestPermissionAfterChecked,
    ) {
        ShizukuDelegate(
            context = context,
            coroutineScope = coroutineScope,
            requestPermissionCode = requestPermissionCode,
            requestPermissionAfterChecked = requestPermissionAfterChecked,
        )
    }
    var isBinderReceived by remember(shizukuDelegate) { mutableStateOf(false) }
    var isShizukuManagerInstalled by remember(context) { mutableStateOf(context.isShizukuManagerInstalled) }
    val status: MutableState<ShizukuStatus> = remember(shizukuDelegate) {
        mutableStateOf(ShizukuStatus.Initializing)
    }
    DisposableEffect(shizukuDelegate) {
        shizukuDelegate.addListeners()
        coroutineScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (!isShizukuManagerInstalled) {
                    isShizukuManagerInstalled = context.isShizukuManagerInstalled
                    if (!isShizukuManagerInstalled) {
                        status.value = ShizukuStatus.ShizukuManagerNotFound
                        return@repeatOnLifecycle
                    }
                }
                if (!isBinderReceived) {
                    status.value = ShizukuBinderStatus.WaitingForBinder
                    return@repeatOnLifecycle
                }
                status.value = checkShizukuPermissionStatus(requestPermissionAfterChecked)
            }
        }
        coroutineScope.launch {
            shizukuDelegate.status.collect { newStatus ->
                if (newStatus is ShizukuBinderStatus.BinderReceived) isBinderReceived = true
                status.value = newStatus
            }
        }
        onDispose { shizukuDelegate.removeListeners() }
    }
    return status
}

@SuppressLint("ProduceStateDoesNotAssignValue")
@Composable
fun rememberCurrentActivityText(): State<String> =
    produceState(initialValue = "") {
        while (true) {
            value = topActivity
                ?.let { it.packageName + "\n" + it.className }
                ?: "Can't get current activity"
            delay(500L)
        }
    }
