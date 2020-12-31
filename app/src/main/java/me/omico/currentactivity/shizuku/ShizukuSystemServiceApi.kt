package me.omico.currentactivity.shizuku

import android.app.IActivityTaskManager
import android.content.ComponentName
import me.omico.core.Singleton
import moe.shizuku.api.ShizukuBinderWrapper
import moe.shizuku.api.SystemServiceHelper

/**
 * @author Omico 2020/12/16
 */
object ShizukuSystemServiceApi {

    private val ACTIVITY_TASK_MANAGER: Singleton<IActivityTaskManager> =
            object : Singleton<IActivityTaskManager>() {
                override fun create(): IActivityTaskManager = IActivityTaskManager.Stub.asInterface(
                        ShizukuBinderWrapper(
                                SystemServiceHelper.getSystemService("activity_task")
                        )
                )
            }

    fun getTopActivity(): ComponentName =
            ACTIVITY_TASK_MANAGER.get().getTasks(Int.MAX_VALUE).first().topActivity!!
}
