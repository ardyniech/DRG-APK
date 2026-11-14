package com.example.modules.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.shared.atoms.DrgScreenContainer
import com.example.shared.atoms.cinematic.ojol360.CinematicOjol360FullscreenModal
import com.example.shared.atoms.cinematic.ojol360.rememberCinematicOjol360State

@Composable
fun LandingScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onQuickDemoLogin: () -> Unit = onNavigateToLogin
) {
    val ojolState = rememberCinematicOjol360State()
    val scrollState = rememberScrollState()

    DrgScreenContainer(showAtmosphericGlow = true) {
        Box(modifier = Modifier.fillMaxSize().testTag("landing_screen")) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                LandingHeaderBar(onAboutClick = onNavigateToAbout)

                LandingHeroOjol360Card(state = ojolState)

                LandingFeatureHighlights()

                LandingActionSection(
                    onLoginClick = onNavigateToLogin,
                    onRegisterClick = onNavigateToRegister,
                    onQuickDemoClick = onQuickDemoLogin
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Fullscreen 360 Cinematic Zoomed Mode
            CinematicOjol360FullscreenModal(
                state = ojolState,
                onLoginClick = onNavigateToLogin
            )
        }
    }
}
