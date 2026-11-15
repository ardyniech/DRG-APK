package com.example

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.audio.SosHapticManager
import com.example.core.database.AppDatabase
import com.example.core.firebase.configureAppCheck
import com.example.core.repository.DRGRepository
import com.example.core.viewmodel.CashManagementViewModel
import com.example.core.viewmodel.DRGViewModel
import com.example.modules.main.DrgAppContent
import com.example.ui.theme.DRGDriverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 1: Configure Firebase App Check BEFORE Compose UI, ViewModels, or Firebase ops
        configureAppCheck(this, intent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
            }
        }
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }

        val database = AppDatabase.getInstance(applicationContext)
        val repository = DRGRepository(database)
        val sharedPrefs = applicationContext.getSharedPreferences("drg_prefs", MODE_PRIVATE)
        SosHapticManager.initialize(applicationContext)

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
    }

    fun sinkToBackground(): Boolean = moveTaskToBack(true)
}
