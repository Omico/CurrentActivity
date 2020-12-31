package me.omico.core.preference

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

/**
 * @author Omico 2020/12/27
 */
fun <T : Preference> PreferenceFragmentCompat.preference(key: String): Lazy<T> = object : Lazy<T> {

    private var cached: T? = null

    override val value: T
        get() = when {
            cached != null -> cached!!
            else -> {
                val preference = findPreference<T>(key)
                cached = preference
                checkNotNull(preference) { "Can't find $key in current PreferenceFragment." }
            }
        }

    override fun isInitialized() = cached != null
}

fun PreferenceFragmentCompat.addPreferenceDelegate(preferenceDelegate: PreferenceDelegate) {
    lifecycle.addObserver(PreferenceDelegateLifecycleObserver(preferenceDelegate))
}
