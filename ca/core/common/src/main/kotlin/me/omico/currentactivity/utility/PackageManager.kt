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
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build

val Context.installedPackages: List<PackageInfo>
    get() = when {
        Build.VERSION.SDK_INT >= 33 ->
            packageManager.getInstalledPackages(PackageManager.PackageInfoFlags.of(0L))
        else ->
            @Suppress("DEPRECATION")
            packageManager.getInstalledPackages(0)
    }

fun Context.isPackageInstalled(packageName: String): Boolean =
    installedPackages.find { it.packageName == packageName } != null
