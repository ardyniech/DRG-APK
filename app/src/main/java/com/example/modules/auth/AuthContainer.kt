package com.example.modules.auth

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.core.viewmodel.DRGViewModel
import com.example.ui.theme.DrgBackgroundGradient
import com.example.shared.models.DriverMember

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
    val members by viewModel.allMembers.collectAsState()

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
                    onLogin = { id ->
                        onLoginSuccess(id)
                    }
                )
                AuthStage.REGISTER -> RegisterScreen(
                    onBack = { currentStage = AuthStage.LANDING },
                    onSubmit = { name, phone, plate, model, area ->
                        viewModel.registerNewDriver(name, phone, plate, model, area)
                        currentStage = AuthStage.LOGIN
                    }
                )
            }
        }
    }
}
