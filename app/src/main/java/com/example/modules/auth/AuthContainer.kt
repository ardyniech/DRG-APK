package com.example.modules.auth

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.core.firebase.GoogleAuthManager
import com.example.core.firebase.GoogleAuthOutcome
import com.example.core.viewmodel.DRGViewModel
import com.example.core.viewmodel.completeGoogleSignIn
import com.example.core.viewmodel.registerNewDriver
import com.example.modules.about.AboutScreen
import com.example.shared.atoms.DrgLuminousAtmosphere
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.utils.AppThemePreferences
import com.example.shared.utils.rememberDoubleBackExitHandler
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth

enum class AuthStage {
    LANDING, REGISTER, ABOUT
}

@Composable
fun AuthContainerScreen(
    viewModel: DRGViewModel
) {
    var currentStage by remember { mutableStateOf(AuthStage.LANDING) }
    var showRegisterSuccessDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val doubleBackHandler = rememberDoubleBackExitHandler()
    val savedTheme = remember { AppThemePreferences.loadVisualTheme(context) }

    BackHandler {
        when {
            showRegisterSuccessDialog -> { showRegisterSuccessDialog = false; currentStage = AuthStage.LANDING }
            currentStage != AuthStage.LANDING -> { doubleBackHandler.reset(); currentStage = AuthStage.LANDING }
            else -> doubleBackHandler.handleBackPress()
        }
    }

    LaunchedEffect(Unit) {
        if (FirebaseApp.getApps(context).isEmpty()) return@LaunchedEffect
        val existingUser = Firebase.auth.currentUser
        if (existingUser != null) {
            viewModel.completeGoogleSignIn(existingUser)
            return@LaunchedEffect
        }
        val outcome = GoogleAuthManager.attemptAutoSignIn(context)
        if (outcome is GoogleAuthOutcome.Success) {
            viewModel.completeGoogleSignIn(outcome.user)
        }
    }

    CompositionLocalProvider(LocalAppVisualTheme provides savedTheme) {
        Box(modifier = Modifier.fillMaxSize()) {
            DrgLuminousAtmosphere(theme = savedTheme, modifier = Modifier.fillMaxSize())
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .let { if (currentStage == AuthStage.REGISTER) it.windowInsetsPadding(WindowInsets.systemBars).imePadding() else it }
            ) {
                Crossfade(targetState = currentStage, label = "authTransition") { stage ->
                    when (stage) {
                        AuthStage.LANDING -> LandingScreen(
                            onNavigateToRegister = { currentStage = AuthStage.REGISTER },
                            onNavigateToAbout = { currentStage = AuthStage.ABOUT },
                            onGoogleSignedIn = { viewModel.completeGoogleSignIn(it) },
                            onAuthError = { viewModel.showToast(it) }
                        )
                        AuthStage.REGISTER -> RegisterScreen(
                            onBack = { currentStage = AuthStage.LANDING },
                            onSubmit = { name, phone, plate, model, area ->
                                viewModel.registerNewDriver(name, phone, plate, model, area)
                                showRegisterSuccessDialog = true
                            }
                        )
                        AuthStage.ABOUT -> AboutScreen(onBack = { currentStage = AuthStage.LANDING })
                    }
                }
            }
        }
        if (showRegisterSuccessDialog) {
            RegisterSuccessDialog(onDismiss = { showRegisterSuccessDialog = false; currentStage = AuthStage.LANDING })
        }
    }
}
