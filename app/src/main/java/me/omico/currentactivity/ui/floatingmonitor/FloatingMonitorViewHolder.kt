package me.omico.currentactivity.ui.floatingmonitor

import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import me.omico.core.view.FloatingViewHolder
import me.omico.currentactivity.applicationContext
import me.omico.currentactivity.databinding.FloatingMonitorViewBinding

/**
 * @author Omico 2020/12/18
 */
class FloatingMonitorViewHolder private constructor(
        override var windowLayoutParams: WindowManager.LayoutParams,
        val binding: FloatingMonitorViewBinding,
) : FloatingViewHolder(binding.root, windowLayoutParams) {

    companion object {

        fun create(): FloatingMonitorViewHolder {
            val binding = FloatingMonitorViewBinding.inflate(LayoutInflater.from(applicationContext))
            val windowLayoutParams = WindowManager.LayoutParams().apply {
                type = when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                    else -> @Suppress("DEPRECATION") WindowManager.LayoutParams.TYPE_PHONE
                }
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
                gravity = Gravity.TOP or Gravity.START
                format = PixelFormat.TRANSPARENT
                flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            }
            return FloatingMonitorViewHolder(windowLayoutParams, binding)
        }
    }
}
