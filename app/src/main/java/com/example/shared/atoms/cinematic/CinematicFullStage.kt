package com.example.shared.atoms.cinematic

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
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
    val ctaProgress by animateFloatAsState(
        targetValue = if (director.ctaVisible) 1f else 0f,
        animationSpec = tween(650),
        label = "stage-cta"
    )

    Box(modifier = modifier.fillMaxSize().testTag("landing_screen")) {
        RoadRushBackdrop(modifier = Modifier.fillMaxSize())
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to DrgNightSkyTop.copy(alpha = 0.62f),
                        0.42f to DrgAsphaltDeep.copy(alpha = 0.12f),
                        1f to DrgNightSkyTop.copy(alpha = 0.86f)
                    )
                )
        )

        FilmGrainOverlay(
            modifier = Modifier.fillMaxSize(),
            speckCount = 80,
            grainSpeed = 0.9f,
            vignetteStrength = 0.52f
        )

        CinematicSceneChrome(
            scene = director.scene,
            sceneIndex = director.sceneIndex,
            ctaVisible = director.ctaVisible,
            onSkip = director.skipToCta,
            onReplay = director.replay,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 22.dp, vertical = 148.dp)
                .scale(0.94f + ctaProgress * 0.06f)
        ) {
            CinematicCtaPanel(
                visible = director.ctaVisible,
                onQuickDemo = onQuickDemoLogin,
                onLogin = onNavigateToLogin,
                onRegister = onNavigateToRegister,
                onAbout = onNavigateToAbout
            )
        }

        val gaugeAlpha = (1f - ctaProgress * 0.55f).coerceIn(0.25f, 1f)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 132.dp, end = 18.dp)
                .alpha(gaugeAlpha)
                .clip(RoundedCornerShape(18.dp))
                .background(Color.Black.copy(alpha = 0.28f))
                .padding(8.dp)
        ) {
            SpeedometerGauge(diameter = 84.dp, maxSpeed = 120)
        }
    }
}