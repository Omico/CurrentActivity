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
package me.omico.currentactivity.utility

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

fun ActivityResultCaller.activityResultLauncher(callback: ActivityResultCallback<ActivityResult>): ActivityResultLauncher<Intent> =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult(), callback)

fun ActivityResultCaller.requestDisplayOverlayPermission(
    context: Context,
    callback: ActivityResultCallback<ActivityResult>,
) = activityResultLauncher(callback).launch(context.manageOverlayPermissionIntent)

val Context.manageOverlayPermissionIntent: Intent
    get() = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        .apply {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                data = Uri.parse("package:$packageName")
            }
        }

fun Context.navigateToOverlayPermissionManager() = startActivity(manageOverlayPermissionIntent)
