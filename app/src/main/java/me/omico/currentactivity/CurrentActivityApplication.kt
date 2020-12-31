package me.omico.currentactivity

import android.app.Application
import android.content.Context
import android.util.Log
import me.weishu.reflection.Reflection
import moe.shizuku.api.ShizukuProvider

/**
 * @author Omico 2020/12/16
 */
class CurrentActivityApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        Reflection.unseal(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        ShizukuProvider.requestBinderForNonProviderProcess(this)
    }

    companion object {

        @JvmStatic
        lateinit var instance: Application
            private set
    }
}

val applicationContext: Context = CurrentActivityApplication.instance

fun Any.d(tag: String = "Debug") = Log.e(tag, toString())
