package com.example.modules.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.cinematic.cyberojol.CyberLauncherArcDock
import com.example.shared.atoms.cinematic.turntable.Ojol360TurntablePresetRow
import com.example.shared.atoms.cinematic.turntable.Ojol360TurntableTopBar
import com.example.shared.atoms.cinematic.turntable.Ojol360TurntableViewport
import com.example.shared.atoms.cinematic.turntable.rememberOjol360TurntableState
import com.example.ui.theme.DrgGlassSurfaceStrong
import com.example.ui.theme.DrgTextDark
import kotlinx.coroutines.delay

@Composable
fun LandingScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onQuickDemoLogin: () -> Unit = onNavigateToLogin
) {
    val turntableState = rememberOjol360TurntableState()
    var launcherVisible by remember { mutableStateOf(false) }

    // Auto-reveal floating launcher console after 5 seconds if untouched
    LaunchedEffect(Unit) {
        delay(5000L)
        launcherVisible = true
    }

    Box(modifier = Modifier.fillMaxSize().testTag("landing_screen")) {
        // 1. Persistent Photorealistic Full-Screen 360° Live Cinematic Ojol
        Ojol360TurntableViewport(
            state = turntableState,
            modifier = Modifier.fillMaxSize(),
            onTap = { launcherVisible = !launcherVisible }
        )

        // 2. Breathing Hint Pill (Visible before launcher reveals)
        AnimatedVisibility(
            visible = !launcherVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 28.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(DrgGlassSurfaceStrong)
                    .border(1.dp, Color.White.copy(alpha = 0.85f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "💡 SENTUH LAYAR // BUKA KONSOL • GESER 360°",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = DrgTextDark
                )
            }
        }

        // 3. Floating Telemetry Top Bar
        AnimatedVisibility(
            visible = launcherVisible,
            enter = fadeIn() + slideInVertically { -it },
            exit = fadeOut() + slideOutVertically { -it },
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Ojol360TurntableTopBar(
                state = turntableState,
                onAboutClick = onNavigateToAbout
            )
        }

        // 4. Floating Holographic Launcher Console (Revealed after 5s or on tap)
        AnimatedVisibility(
            visible = launcherVisible,
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Ojol360TurntablePresetRow(state = turntableState)

                CyberLauncherArcDock(
                    onLoginClick = onNavigateToLogin,
                    onRegisterClick = onNavigateToRegister,
                    onQuickDemoClick = onQuickDemoLogin,
                    onAboutClick = onNavigateToAbout
                )
            }
        }
    }
}
