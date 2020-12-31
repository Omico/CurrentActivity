package me.omico.core.databinding

import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import me.omico.core.viewbinding.ActivityBindingDelegate
import me.omico.core.viewbinding.FragmentBindingDelegate
import kotlin.properties.ReadOnlyProperty

/**
 * @author Omico 2020/12/29
 */
fun <T : ViewDataBinding> Fragment.dataBinding(): ReadOnlyProperty<Fragment, T> =
        FragmentBindingDelegate<T>(
                bind = { DataBindingUtil.bind<T>(requireView())!! },
                also = { it.lifecycleOwner = viewLifecycleOwner }
        )

inline fun <reified T : ViewDataBinding> ComponentActivity.dataBinding(): ReadOnlyProperty<ComponentActivity, T> =
        ActivityBindingDelegate(
                bind = { T::class.java.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as T },
                also = { it.lifecycleOwner = this }
        )
