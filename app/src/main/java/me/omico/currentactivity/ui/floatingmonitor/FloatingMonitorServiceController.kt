package me.omico.currentactivity.ui.floatingmonitor

import android.content.Context
import me.omico.core.startForegroundService
import me.omico.core.startService
import me.omico.currentactivity.service.FloatingMonitorService

/**
 * @author Omico 2020/12/26
 */
object FloatingMonitorServiceController {

    fun startForeground(context: Context) {
        context.startForegroundService<FloatingMonitorService> {
            putExtra(FloatingMonitorService.EXTRA_ACTION, FloatingMonitorService.ACTION_START)
        }
    }

    fun stopForeground(context: Context) {
        context.startService<FloatingMonitorService> {
            putExtra(FloatingMonitorService.EXTRA_ACTION, FloatingMonitorService.ACTION_STOP)
        }
    }
}
