package com.example.shared.atoms.cinematic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import com.example.modules.auth.LandingAuthMenuDialog
import com.example.shared.atoms.cinematic.rider.RiderPovOverlay
import com.example.shared.atoms.cinematic.rider.rememberRiderSteeringState
import com.example.ui.theme.DrgAsphaltDeep
import com.example.ui.theme.DrgNightSkyTop

@Composable
fun CinematicFullStage(
    director: CinematicDirectorState,
    onNavigateToAbout: () -> Unit,
    onQuickDemoLogin: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    val riderState = rememberRiderSteeringState()

    Box(modifier = modifier.fillMaxSize().testTag("landing_screen")) {
        // 1. Dynamic city night road backdrop
        RoadRushBackdrop(
            modifier = Modifier.fillMaxSize(),
            horizonRatio = 0.335f,
            showRain = true,
            riderState = riderState
        )

        // 2. Night lighting overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to DrgNightSkyTop.copy(alpha = 0.58f),
                        0.40f to DrgAsphaltDeep.copy(alpha = 0.10f),
                        1f to DrgNightSkyTop.copy(alpha = 0.82f)
                    )
                )
        )

        // 3. Cinematic film grain texture
        FilmGrainOverlay(
            modifier = Modifier.fillMaxSize(),
            speckCount = 75,
            grainSpeed = 0.85f,
            vignetteStrength = 0.48f
        )

        // 4. Headline and scene captions
        CinematicSceneChrome(
            scene = director.scene,
            sceneIndex = director.sceneIndex,
            ctaVisible = director.ctaVisible,
            onSkip = director.openMenu,
            onReplay = director.replay,
            modifier = Modifier.fillMaxSize()
        )

        // 5. Realistic motorcycle cockpit POV with interactive dashboard button
        RiderPovOverlay(
            state = riderState,
            onOpenMenu = director.openMenu,
            modifier = Modifier.fillMaxSize()
        )

        // 6. On-demand Auth menu dialog triggered ONLY when tapping dashboard button
        LandingAuthMenuDialog(
            visible = director.ctaVisible,
            onDismiss = director.closeMenu,
            onQuickDemo = onQuickDemoLogin,
            onLogin = onNavigateToLogin,
            onRegister = onNavigateToRegister,
            onAbout = onNavigateToAbout
        )
    }
}