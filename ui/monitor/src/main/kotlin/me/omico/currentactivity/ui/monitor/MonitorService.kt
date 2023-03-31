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
package me.omico.currentactivity.ui.monitor

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.compose.ui.geometry.Size
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import me.omico.currentactivity.applicationContext
import me.omico.currentactivity.core.common.resources.R
import me.omico.currentactivity.monitor.MonitorServiceStatus
import me.omico.currentactivity.ui.ComposeService
import me.omico.currentactivity.ui.DraggableContainer
import me.omico.currentactivity.ui.monitor.internal.InternalMonitorServiceController
import me.omico.currentactivity.utility.debug
import me.omico.currentactivity.utility.dpToPx
import me.omico.currentactivity.utility.intent

class MonitorService : ComposeService() {

    private val notificationManagerCompat: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(this)
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = run {
        when (intent?.action) {
            ACTION_START -> onActionStart()
            ACTION_STOP -> onActionStop()
        }
        super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        debug { "MonitorService onDestroy." }
        notificationManagerCompat.cancel(NOTIFICATION_ID)
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_DETACH)
        InternalMonitorServiceController.updateServiceStatus(MonitorServiceStatus.Stopped)
        super.onDestroy()
    }

    override val size by lazy {
        val configuration = resources.configuration
        Size(
            width = dpToPx(configuration.screenWidthDp),
            height = dpToPx(48),
        )
    }

    private fun createNotificationChannel() =
        NotificationChannelCompat
            .Builder(CHANNEL_ID, NotificationManager.IMPORTANCE_MIN)
            .setName("Monitor Service")
            .setDescription("Show monitor on background.")
            .setSound(null, null)
            .setShowBadge(true)
            .setLightsEnabled(false)
            .setVibrationEnabled(false)
            .build()
            .let(notificationManagerCompat::createNotificationChannel)

    private fun createNotification(): Notification =
        NotificationCompat
            .Builder(this, CHANNEL_ID)
            .setContentTitle("Monitoring")
            .setSmallIcon(R.drawable.notifcation_statusbar)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setOngoing(true)
            .setShowWhen(false)
            .addAction(
                NotificationCompat.Action(
                    R.drawable.notifcation_action_stop,
                    "Stop Monitoring",
                    PendingIntent.getService(
                        this,
                        REQUEST_CODE_STOP,
                        stopIntent,
                        when {
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> PendingIntent.FLAG_IMMUTABLE
                            else -> 0
                        },
                    ),
                ),
            )
            .build()

    private fun onActionStart() {
        debug { "MonitorService starting." }
        InternalMonitorServiceController.updateServiceStatus(MonitorServiceStatus.Starting)
        startForeground(NOTIFICATION_ID, createNotification())
        setContent { DraggableContainer { MonitorUi() } }
        InternalMonitorServiceController.updateServiceStatus(MonitorServiceStatus.Started)
    }

    private fun onActionStop() {
        debug { "MonitorService stopping." }
        InternalMonitorServiceController.updateServiceStatus(MonitorServiceStatus.Stopping)
        stopSelf()
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "monitor"
        private const val REQUEST_CODE_STOP = 1
        private const val ACTION_PREFIX = "${BuildConfig.LIBRARY_PACKAGE_NAME}.action"
        internal const val ACTION_START = "$ACTION_PREFIX.START"
        internal const val ACTION_STOP = "$ACTION_PREFIX.STOP"

        internal val stopIntent = applicationContext.intent<MonitorService> {
            action = ACTION_STOP
        }
    }
}
