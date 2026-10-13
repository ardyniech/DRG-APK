package com.example.modules.auth

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.modules.about.AboutScreen
import com.example.core.viewmodel.*
import com.example.shared.atoms.DrgAtmosphericGlow
import com.example.shared.models.MemberRole
import com.example.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

enum class AuthStage {
    LANDING, LOGIN, REGISTER, ABOUT
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
            .windowInsetsPadding(WindowInsets.systemBars)
            .imePadding()
    ) {
        DrgAtmosphericGlow()

        Crossfade(targetState = currentStage, label = "authTransition") { stage ->
            when (stage) {
                AuthStage.LANDING -> LandingScreen(
                    onNavigateToLogin = { currentStage = AuthStage.LOGIN },
                    onNavigateToRegister = { currentStage = AuthStage.REGISTER },
                    onNavigateToAbout = { currentStage = AuthStage.ABOUT },
                    onQuickDemoLogin = {
                        val superAdmin = members.find { it.role == MemberRole.SUPER_ADMIN } ?: members.firstOrNull()
                        if (superAdmin != null) onLoginSuccess(superAdmin.id) else currentStage = AuthStage.LOGIN
                    }
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
                AuthStage.ABOUT -> AboutScreen(
                    onBack = { currentStage = AuthStage.LANDING }
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
                    "Data registrasi Anda telah tersimpan. Pengurus / Satgas akan memverifikasi berkas KTA Anda.",
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
