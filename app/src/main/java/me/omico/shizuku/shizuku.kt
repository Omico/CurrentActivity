package me.omico.shizuku

import android.content.Context
import android.content.Intent
import me.omico.core.permission.PermissionState
import me.omico.core.permission.checkPermission
import moe.shizuku.api.ShizukuApiConstants

/**
 * @author Omico 2020/12/27
 */
fun Context.checkShizukuPermission(): PermissionState = checkPermission(ShizukuApiConstants.PERMISSION)

fun Context.goToShizuku() {
    packageManager.getLaunchIntentForPackage("moe.shizuku.privileged.api")?.also {
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(it)
    }
}
