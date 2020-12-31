package me.omico.core.viewbinding

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author Omico 2020/12/29
 */
class FragmentBindingDelegate<T : ViewBinding>(
        private val bind: () -> T,
        private val also: (T) -> Unit = {}
) : ReadOnlyProperty<Fragment, T> {

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return thisRef.findBinding(property) ?: bind().also {
            it.root.setTag(property.name.hashCode(), it)
            also(it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : ViewDataBinding> Fragment.findBinding(property: KProperty<*>): T? =
            requireView().getTag(property.name.hashCode()) as? T
}
