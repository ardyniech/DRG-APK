package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val repository = DRGRepository(database)
        val sharedPrefs = applicationContext.getSharedPreferences("drg_prefs", android.content.Context.MODE_PRIVATE)

        com.example.core.audio.SosHapticManager.initialize(applicationContext)

        setContent {
            val viewModel: DRGViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return DRGViewModel(repository, sharedPrefs, applicationContext) as T
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
}

