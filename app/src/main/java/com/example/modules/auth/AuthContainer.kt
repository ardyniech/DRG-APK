package com.example.modules.auth

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.core.viewmodel.DRGViewModel
import com.example.core.viewmodel.registerNewDriver
import com.example.modules.about.AboutScreen
import com.example.shared.atoms.DrgAtmosphericGlow
import com.example.shared.models.MemberRole
import com.example.shared.utils.rememberDoubleBackExitHandler
import com.example.ui.theme.DrgBackgroundGradient
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
    val doubleBackHandler = rememberDoubleBackExitHandler()

    BackHandler {
        when {
            showRegisterSuccessDialog -> {
                showRegisterSuccessDialog = false
                currentStage = AuthStage.LOGIN
            }
            currentStage != AuthStage.LANDING -> {
                doubleBackHandler.reset()
                currentStage = AuthStage.LANDING
            }
            else -> {
                doubleBackHandler.handleBackPress()
            }
        }
    }

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
        RegisterSuccessDialog(
            onDismiss = {
                showRegisterSuccessDialog = false
                currentStage = AuthStage.LOGIN
            }
        )
    }
}
