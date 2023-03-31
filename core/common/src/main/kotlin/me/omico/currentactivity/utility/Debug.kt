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
import android.util.Log
import java.io.File

object AppDebug {
    var enabled: Boolean = true
}

inline fun <reified T : Any> debug(
    tag: String? = null,
    message: () -> T?,
) {
    if (AppDebug.enabled) {
        val appDebugTag = tag?.let { "AppDebug: $it" } ?: "AppDebug"
        Log.e(appDebugTag, message().toString())
    }
}

inline fun <reified T : Any> T?.d(tag: String? = null) = debug(tag) { this }

inline val <reified T : Any> T?.d
    get() = d()

fun Context.writeDebugText(name: String, content: String) {
    File(filesDir, "debug")
        .also { if (!it.exists()) it.mkdirs() }
        .let { File(it, name) }
        .writeText(content)
}
