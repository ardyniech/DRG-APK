package com.example.modules.auth

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.core.viewmodel.DRGViewModel
import com.example.ui.theme.DrgBackgroundGradient
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgTextDark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

enum class AuthStage {
    LANDING,
    LOGIN,
    REGISTER
}

@Composable
fun AuthContainerScreen(
    viewModel: DRGViewModel,
    onLoginSuccess: (String) -> Unit
) {
    var currentStage by remember { mutableStateOf(AuthStage.LANDING) }
    var showRegisterSuccessDialog by remember { mutableStateOf(false) }
    val members by viewModel.allMembers.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(Unit) {
        val savedId = withContext(Dispatchers.IO) {
            val prefs = context.getSharedPreferences("drg_prefs", android.content.Context.MODE_PRIVATE)
            prefs.getString("logged_in_member_id", null)
        }
        if (savedId != null) {
            onLoginSuccess(savedId)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DrgBackgroundGradient)
    ) {
        Crossfade(targetState = currentStage, label = "authTransition") { stage ->
            when (stage) {
                AuthStage.LANDING -> LandingScreen(
                    onNavigateToLogin = { currentStage = AuthStage.LOGIN },
                    onNavigateToRegister = { currentStage = AuthStage.REGISTER }
                )
                AuthStage.LOGIN -> LoginScreen(
                    members = members,
                    onBack = { currentStage = AuthStage.LANDING },
                    onLogin = { id -> onLoginSuccess(id) }
                )
                AuthStage.REGISTER -> RegisterScreen(
                    onBack = { currentStage = AuthStage.LANDING },
                    onSubmit = { name, phone, plate, model, area, pin ->
                        viewModel.registerNewDriver(name, phone, plate, model, area, pin)
                        showRegisterSuccessDialog = true
                    }
                )
            }
        }
    }

    if (showRegisterSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showRegisterSuccessDialog = false
                currentStage = AuthStage.LOGIN
            },
            title = {
                Text("Pendaftaran Berhasil! 🎉", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DrgTextDark)
            },
            text = {
                Text(
                    "Data registrasi Anda telah tersimpan. Pengurus / Dewan Etika akan melakukan proses verifikasi KTA & berkas. Silakan login untuk memantau status screening Anda.",
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRegisterSuccessDialog = false
                        currentStage = AuthStage.LOGIN
                    }
                ) {
                    Text("Lanjut ke Halaman Login", fontWeight = FontWeight.Bold, color = DrgGrabGreenPrimary)
                }
            }
        )
    }
}
