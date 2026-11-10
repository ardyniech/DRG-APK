package com.example

import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.database.AppDatabase
import com.example.core.repository.DRGRepository
import com.example.core.viewmodel.CashManagementViewModel
import com.example.core.viewmodel.DRGViewModel
import com.example.modules.main.DrgAppContent
import com.example.ui.theme.DRGDriverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Minta izin notifikasi (Android 13+) agar worker bisa beri tahu update.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) !=
                android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                androidx.core.app.ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }

        val database = AppDatabase.getInstance(applicationContext)
        val repository = DRGRepository(database)
        val sharedPrefs = applicationContext.getSharedPreferences("drg_prefs", android.content.Context.MODE_PRIVATE)

        com.example.core.audio.SosHapticManager.initialize(applicationContext)

        setContent {
            val viewModel: DRGViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return DRGViewModel(repository, sharedPrefs, application) as T
                    }
                }
            )

            val cashViewModel: CashManagementViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return CashManagementViewModel(repository) as T
                    }
                }
            )

            DRGDriverTheme {
                DrgAppContent(viewModel = viewModel, cashViewModel = cashViewModel)
            }
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        // App transitions seamlessly to background state ("tenggelam") on Home / Recents
    }

    fun sinkToBackground(): Boolean {
        return moveTaskToBack(true)
    }
}

