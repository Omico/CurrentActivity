package me.omico.currentactivity.ui.floatingmonitor

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import me.omico.core.createIntent
import me.omico.core.createNotificationChannel
import me.omico.currentactivity.R
import me.omico.currentactivity.applicationContext
import me.omico.currentactivity.service.FloatingMonitorService
import me.omico.currentactivity.service.FloatingMonitorService.Companion.ACTION_STOP
import me.omico.currentactivity.service.FloatingMonitorService.Companion.EXTRA_ACTION
import me.omico.currentactivity.ui.MainActivity

/**
 * @author Omico 2020/12/16
 */
class FloatingMonitorNotificationController(
        private val service: FloatingMonitorService
) {

    private lateinit var notification: Notification
    private val notificationManager by lazy { service.getSystemService<NotificationManager>()!! }

    fun create() {
        service.run {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                createNotificationChannel(CHANNEL_ID, getString(R.string.notification_channel_floating_monitor_service)) {
                    setSound(null, null)
                }
            }
            notification = createNotification(getString(R.string.notification_content_title_monitoring)) {
                setContentIntent()
                addStopAction()
            }
            startForeground(NOTIFICATION_ID, notification)
            updateNotification()
        }
    }

    fun cancel() {
        notificationManager.notify(NOTIFICATION_ID, createNotification(service.getString(R.string.notification_content_title_closing)))
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun updateNotification() {
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotification(contentTitle: String, block: NotificationCompat.Builder.() -> Unit = {}): Notification =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
                setSmallIcon(R.drawable.ic_launcher_foreground)
                setContentTitle(contentTitle)
                setShowWhen(false)
                setOnlyAlertOnce(true)
                block()
            }.build()

    private fun NotificationCompat.Builder.setContentIntent() {
        setContentIntent(PendingIntent.getActivity(applicationContext, 0, applicationContext.createIntent<MainActivity>(), 0))
    }

    private fun NotificationCompat.Builder.addStopAction() {
        val intent = Intent(service, FloatingMonitorService::class.java).putExtra(EXTRA_ACTION, ACTION_STOP)
        val stopPendingIntent = PendingIntent.getService(service, PENDING_INTENT_REQUEST_CODE_STOP, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        addAction(R.drawable.ic_notification_action_stop, service.getString(R.string.notification_action_stop), stopPendingIntent)
    }

    companion object {

        private const val CHANNEL_ID: String = "floating_monitor_service"
        private const val NOTIFICATION_ID: Int = 2333
        private const val PENDING_INTENT_REQUEST_CODE_STOP = 0
    }
}
