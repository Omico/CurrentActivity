package me.omico.core

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * @author Omico 2020/12/29
 */
fun Context.createDialog(block: MaterialAlertDialogBuilder.() -> Unit): AlertDialog =
        MaterialAlertDialogBuilder(this).apply(block).create()

fun Fragment.createDialog(block: MaterialAlertDialogBuilder.() -> Unit): AlertDialog =
        requireContext().createDialog(block)

fun Context.showDialog(block: MaterialAlertDialogBuilder.() -> Unit): AlertDialog =
        MaterialAlertDialogBuilder(this).apply(block).show()

fun Fragment.showDialog(block: MaterialAlertDialogBuilder.() -> Unit): AlertDialog =
        requireContext().showDialog(block)
