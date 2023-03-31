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
package me.omico.currentactivity.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun LazyListScope.heightSpacer(height: Dp = 16.dp) {
    item { Spacer(modifier = Modifier.height(height = height)) }
}

@OptIn(ExperimentalAnimationApi::class)
fun LazyListScope.animatedVisibilityItem(
    modifier: Modifier = Modifier,
    visible: Boolean,
    enter: EnterTransition = fadeIn() + scaleIn(),
    exit: ExitTransition = scaleOut() + fadeOut(),
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    item {
        AnimatedVisibility(
            modifier = modifier,
            visible = visible,
            enter = enter,
            exit = exit,
            content = content,
        )
    }
}
