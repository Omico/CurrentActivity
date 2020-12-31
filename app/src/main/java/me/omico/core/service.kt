package me.omico.core

import android.app.Service
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

/**
 * @author Omico 2020/12/29
 */
inline fun <reified T : Service> Context.startService(block: Intent.() -> Unit = {}) {
    Intent(this, T::class.java).apply(block).also { startService(it) }
}

inline fun <reified T : Service> Context.startForegroundService(block: Intent.() -> Unit = {}) {
    Intent(this, T::class.java).apply(block).also { ContextCompat.startForegroundService(this, it) }
}
