package me.omico.core

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService

/**
 * @author Omico 2020/12/29
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Context.createNotificationChannel(
        channelId: String,
        channelName: String = channelId,
        channelImportance: Int = NotificationManager.IMPORTANCE_DEFAULT,
        block: NotificationChannel.() -> Unit = {}
) {
    getSystemService<NotificationManager>()?.createNotificationChannel(
            NotificationChannel(channelId, channelName, channelImportance).apply(block)
    )
}
