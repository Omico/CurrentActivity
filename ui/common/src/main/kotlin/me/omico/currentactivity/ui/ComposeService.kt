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

import android.graphics.PixelFormat
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import me.omico.currentactivity.utility.requireSystemService

abstract class ComposeService : LifecycleService(), SavedStateRegistryOwner {

    abstract val size: Size

    open val windowLayoutParams: WindowManager.LayoutParams by lazy {
        WindowManager.LayoutParams().apply {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            width = size.width.toInt()
            height = size.height.toInt()
            gravity = Gravity.TOP or Gravity.START
            format = PixelFormat.TRANSPARENT
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        }
    }

    private val windowManager: WindowManager by lazy(::requireSystemService)

    private val savedStateRegistryController: SavedStateRegistryController by lazy {
        SavedStateRegistryController.create(this).apply {
            performRestore(null)
        }
    }

    private var view: View? = null

    override fun onDestroy() {
        if (view != null) windowManager.removeView(view)
        super.onDestroy()
    }

    override val savedStateRegistry: SavedStateRegistry =
        savedStateRegistryController.savedStateRegistry

    fun setContent(content: @Composable () -> Unit) {
        require(view == null) { "Content has already been set." }
        view = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@ComposeService)
            setViewTreeSavedStateRegistryOwner(this@ComposeService)
            setContent(content)
        }
        windowManager.addView(view, windowLayoutParams)
    }

    fun updateViewLayout(offset: Offset) {
        requireNotNull(view) { "Content has not been set." }
        windowLayoutParams.x = offset.x.toInt()
        windowLayoutParams.y = offset.y.toInt()
        windowManager.updateViewLayout(view, windowLayoutParams)
    }
}

@Composable
fun ComposeService.DraggableContainer(
    modifier: Modifier = Modifier,
    initialOffset: Offset = Offset.Zero,
    content: @Composable () -> Unit,
) {
    var offset by remember { mutableStateOf(initialOffset) }
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val maxDraggableOffset = remember(size, configuration) {
        with(density) {
            Offset(
                x = configuration.screenWidthDp.dp.toPx() - size.width,
                y = configuration.screenHeightDp.dp.toPx() - size.height,
            )
        }
    }
    Box(
        modifier = run {
            modifier
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            offset += dragAmount
                            updateViewLayout(offset.coerceIn(max = maxDraggableOffset))
                        },
                    )
                }
        },
        content = { content() },
    )
}
