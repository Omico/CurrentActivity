package me.omico.currentactivity.service

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.liveData
import kotlinx.coroutines.delay
import me.omico.core.event.EventBus
import me.omico.currentactivity.BuildConfig
import me.omico.currentactivity.event.Events
import me.omico.currentactivity.shizuku.ShizukuSystemServiceApi
import me.omico.currentactivity.ui.floatingmonitor.FloatingMonitorNotificationController
import me.omico.currentactivity.ui.floatingmonitor.FloatingMonitorViewHolder

/**
 * @author Omico 2020/12/15
 */
class FloatingMonitorService : LifecycleService() {

    private val notificationController = FloatingMonitorNotificationController(this)
    private var floatingViewHolder: FloatingMonitorViewHolder? = null

    override fun onCreate() {
        super.onCreate()
        notificationController.create()
        EventBus.post(Events.IS_FLOATING_MONITOR_SERVICE_RUNNING, true)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when (intent?.getStringExtra(EXTRA_ACTION)) {
            ACTION_START -> onActionStart()
            ACTION_STOP -> onActionStop()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.post(Events.IS_FLOATING_MONITOR_SERVICE_RUNNING, false)
        removeFloatingMonitorView()
        stopForeground(true)
        notificationController.cancel()
    }

    private fun onActionStart() {
        createFloatingMonitorView()
        liveData {
            while (true) {
                emit(ShizukuSystemServiceApi.getTopActivity())
                delay(1000L)
            }
        }.observe(this) {
            floatingViewHolder?.run {
                binding.floatingText.text = StringBuilder().apply {
                    appendLine(it.packageName)
                    append(it.className)
                }.toString()
            }
        }
    }

    private fun onActionStop() {
        stopSelf()
    }

    private fun createFloatingMonitorView() {
        FloatingMonitorViewHolder.create().also {
            floatingViewHolder = it
            it.attach()
        }
    }

    private fun removeFloatingMonitorView() {
        floatingViewHolder?.detach()
        floatingViewHolder = null
    }

    companion object {

        private const val PREFIX = BuildConfig.APPLICATION_ID
        private const val PREFIX_EXTRA = "$PREFIX.extra"
        private const val PREFIX_ACTION = "$PREFIX.action"
        const val EXTRA_ACTION: String = "$PREFIX_EXTRA.ACTION"
        const val ACTION_START: String = "$PREFIX_ACTION.START"
        const val ACTION_STOP: String = "$PREFIX_ACTION.STOP"
    }
}
