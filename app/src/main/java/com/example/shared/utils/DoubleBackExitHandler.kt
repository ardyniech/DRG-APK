package com.example.shared.utils

import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

fun Context.findActivity(): ComponentActivity? {
    var current: Context? = this
    while (current is ContextWrapper) {
        if (current is ComponentActivity) return current
        current = current.baseContext
    }
    return null
}

fun Context.moveAppToBackground(): Boolean {
    val activity = findActivity() ?: return false
    return activity.moveTaskToBack(true)
}

class DoubleBackExitHandler(
    private val context: Context,
    val timeoutMillis: Long = 2000L,
    private val onExit: () -> Unit = {
        val activity = context.findActivity()
        if (activity != null) {
            activity.finishAffinity()
        }
    },
    private val onFirstBack: () -> Unit = {
        Toast.makeText(
            context,
            "Tekan sekali lagi untuk keluar, atau tombol Home agar aplikasi tenggelam di latar belakang",
            Toast.LENGTH_SHORT
        ).show()
    }
) {
    var lastBackPressTime: Long = 0L
        private set

    fun handleBackPress(currentTime: Long = System.currentTimeMillis()): Boolean {
        return if (lastBackPressTime > 0L && (currentTime - lastBackPressTime) <= timeoutMillis) {
            onExit()
            true
        } else {
            lastBackPressTime = currentTime
            onFirstBack()
            false
        }
    }

    fun reset() {
        lastBackPressTime = 0L
    }
}

@Composable
fun rememberDoubleBackExitHandler(
    context: Context = LocalContext.current,
    timeoutMillis: Long = 2000L,
    onExit: () -> Unit = { context.findActivity()?.finishAffinity() }
): DoubleBackExitHandler {
    return remember(context, timeoutMillis) {
        DoubleBackExitHandler(context = context, timeoutMillis = timeoutMillis, onExit = onExit)
    }
}
