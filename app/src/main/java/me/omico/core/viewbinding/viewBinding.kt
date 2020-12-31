package me.omico.core.viewbinding

import android.view.LayoutInflater
import android.view.View
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty

/**
 * @author Omico 2020/12/29
 */
inline fun <reified T : ViewBinding> Fragment.viewBinding(): ReadOnlyProperty<Fragment, T> =
        FragmentBindingDelegate(
                bind = { T::class.java.getMethod("bind", View::class.java).invoke(null, requireView()) as T }
        )

inline fun <reified T : ViewBinding> ComponentActivity.viewBinding(): ReadOnlyProperty<ComponentActivity, T> =
        ActivityBindingDelegate(
                bind = { T::class.java.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as T }
        )
