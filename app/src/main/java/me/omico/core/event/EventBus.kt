package me.omico.core.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @author Omico 2020/12/22
 */
object EventBus {

    private val map = HashMap<String, MutableStateFlow<Any>>()

    fun <T : Any> post(key: String, value: T) {
        map[key]?.also { it.value = value } ?: map.put(key, MutableStateFlow(value))
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(key: String, defaultValue: T): StateFlow<T> =
            provideMutableStateFlow(key, defaultValue).asStateFlow() as StateFlow<T>

    private fun provideMutableStateFlow(key: String, defaultValue: Any): MutableStateFlow<Any> =
            map[key] ?: MutableStateFlow(defaultValue).also { map[key] = it }
}

fun <T : Any> EventBus.getLiveData(key: String, defaultValue: T): LiveData<T> =
        get(key, defaultValue).asLiveData()
