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
import android.util.TypedValue
import androidx.annotation.Dimension
import androidx.core.content.getSystemService

inline fun <reified T : Any> Context.requireSystemService(): T = requireNotNull(getSystemService())

inline fun <reified T> Context.intent(builder: Intent.() -> Unit = {}): Intent =
    Intent(this, T::class.java).apply(builder)

fun Context.dpToPx(@Dimension(unit = Dimension.DP) dp: Int): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics)
