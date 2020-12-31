package me.omico.core.viewbinding

import androidx.activity.ComponentActivity
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author Omico 2020/12/29
 */
class ActivityBindingDelegate<T : ViewBinding>(
        private val bind: () -> T,
        private val also: (T) -> Unit = {}
) : ReadOnlyProperty<ComponentActivity, T> {

    private var binding: T? = null

    override fun getValue(thisRef: ComponentActivity, property: KProperty<*>): T =
            binding ?: bind().also {
                binding = it
                also(it)
            }
}
