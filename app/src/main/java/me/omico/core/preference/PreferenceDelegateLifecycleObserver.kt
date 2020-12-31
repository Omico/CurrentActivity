package me.omico.core.preference

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * @author Omico 2020/12/27
 */
class PreferenceDelegateLifecycleObserver(
        private val preferenceDelegate: PreferenceDelegate
) : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        preferenceDelegate.onCreate()
    }

    override fun onResume(owner: LifecycleOwner) {
        preferenceDelegate.onUpdatePreference()
    }
}
