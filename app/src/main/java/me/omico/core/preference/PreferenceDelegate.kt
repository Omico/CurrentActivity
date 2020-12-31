package me.omico.core.preference

import android.content.Context
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

/**
 * @author Omico 2020/12/27
 */
interface PreferenceDelegate {

    val host: PreferenceFragmentCompat

    val context: Context get() = host.requireContext()

    fun onCreate() {}

    fun onUpdatePreference() {}
}

fun <T : Preference> PreferenceDelegate.preference(key: String): Lazy<T> = host.preference(key)
