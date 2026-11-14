package com.example.modules.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.shared.atoms.cinematic.ojol360.CinematicOjol360State
import com.example.ui.theme.DrgGlassSurfaceStrong
import com.example.ui.theme.DrgGoldReward

@Composable
fun LandingCinematicLauncherDock(
    state: CinematicOjol360State,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onQuickDemoClick: () -> Unit,
    onAboutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(DrgGlassSurfaceStrong)
                .border(1.5.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(24.dp))
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .testTag("landing_launcher_dock"),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LauncherIconItem(
                icon = Icons.Default.Login,
                label = "Masuk",
                isPrimary = true,
                onClick = onLoginClick,
                tag = "btn_dock_login"
            )
            LauncherIconItem(
                icon = Icons.Default.PersonAdd,
                label = "Daftar",
                onClick = onRegisterClick,
                tag = "btn_dock_register"
            )
            LauncherIconItem(
                icon = Icons.Default.Bolt,
                label = "Demo",
                iconTint = DrgGoldReward,
                onClick = onQuickDemoClick,
                tag = "btn_dock_demo"
            )
            LauncherIconItem(
                icon = Icons.Default.Info,
                label = "Tentang",
                onClick = onAboutClick,
                tag = "btn_dock_about"
            )
        }
    }
}
