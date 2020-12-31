package me.omico.core.fragment

import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * @author Omico 2020/12/29
 */
@MainThread
fun <T : Fragment> FragmentActivity.fragment(@IdRes id: Int): Lazy<T> = object : Lazy<T> {

    private var cached: T? = null

    @Suppress("UNCHECKED_CAST")
    override val value: T
        get() = cached ?: run {
            val fragment = supportFragmentManager.findFragmentById(id) as T
            cached = fragment
            fragment
        }

    override fun isInitialized() = cached != null
}
