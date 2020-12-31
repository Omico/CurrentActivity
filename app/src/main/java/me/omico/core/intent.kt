package me.omico.core

import android.content.Context
import android.content.Intent

/**
 * @author Omico 2020/12/30
 */
inline fun <reified T> Context.createIntent(): Intent = Intent(this, T::class.java)
