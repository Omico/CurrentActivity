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

import android.app.ActivityManager
import android.app.IActivityTaskManager
import android.content.ComponentName
import android.os.Build
import android.view.Display
import rikka.shizuku.ShizukuBinderWrapper
import rikka.shizuku.SystemServiceHelper

val topActivity: ComponentName?
    get() = getTasksWrapper().first().topActivity

private val activityTaskManager: IActivityTaskManager =
    SystemServiceHelper.getSystemService("activity_task")
        .let(::ShizukuBinderWrapper)
        .let(IActivityTaskManager.Stub::asInterface)

private fun getTasksWrapper(): List<ActivityManager.RunningTaskInfo> = when {
    Build.VERSION.SDK_INT < 31 -> activityTaskManager.getTasks(1)
    else -> runCatching { activityTaskManager.getTasks(1, false, false, Display.INVALID_DISPLAY) }
        .getOrElse { activityTaskManager.getTasks(1, false, false) }
}
