package me.omico.core.permission

/**
 * @author Omico 2020/12/27
 */
sealed class PermissionState {

    object Granted : PermissionState()
    object ShouldShowRequestPermissionRationale : PermissionState()
    object Denied : PermissionState()
}
