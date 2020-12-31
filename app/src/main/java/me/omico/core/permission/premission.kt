package me.omico.core.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * @author Omico 2020/12/27
 */
fun Context.checkPermission(permission: String): PermissionState = when {
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> PermissionState.Granted
    this is Activity && ActivityCompat.shouldShowRequestPermissionRationale(this, permission) -> PermissionState.ShouldShowRequestPermissionRationale
    else -> PermissionState.Denied
}

fun ActivityResultCaller.permissionLauncher(block: (Boolean) -> Unit = {}): ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { block(it) }
