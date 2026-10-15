package com.example.modules.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.shared.atoms.DrgScreenContainer
import com.example.shared.atoms.cinematic.CinematicFullStage
import com.example.shared.atoms.cinematic.rememberCinematicDirector

@Composable
fun LandingScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onQuickDemoLogin: () -> Unit = onNavigateToLogin
) {
    val director = rememberCinematicDirector()

    DrgScreenContainer(showAtmosphericGlow = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            CinematicFullStage(
                director = director,
                onNavigateToAbout = onNavigateToAbout,
                onQuickDemoLogin = onQuickDemoLogin,
                onNavigateToLogin = onNavigateToLogin,
                onNavigateToRegister = onNavigateToRegister
            )
        }
    }
}