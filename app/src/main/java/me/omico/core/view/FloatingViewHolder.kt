package me.omico.core.view

import android.view.View
import android.view.WindowManager
import androidx.core.content.getSystemService

/**
 * @author Omico 2020/12/18
 */
open class FloatingViewHolder(
        private val view: View,
        open var windowLayoutParams: WindowManager.LayoutParams
) {

    private val windowManager: WindowManager by lazy { view.context.getSystemService()!! }

    open fun attach() {
        windowManager.addView(view, windowLayoutParams)
    }

    open fun detach() {
        windowManager.removeView(view)
    }
}
