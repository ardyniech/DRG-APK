package com.example.shared.atoms.cinematic.cyberojol

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
import com.example.modules.auth.LauncherIconItem
import com.example.ui.theme.DrgGlassSurfaceStrong
import com.example.ui.theme.DrgGoldReward

@Composable
fun CyberLauncherArcDock(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onQuickDemoClick: () -> Unit,
    onAboutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(DrgGlassSurfaceStrong)
            .border(1.5.dp, Color.White.copy(alpha = 0.95f), RoundedCornerShape(26.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("cyber_launcher_arc_dock"),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LauncherIconItem(
                icon = Icons.Default.Login,
                label = "PORTAL",
                isPrimary = true,
                onClick = onLoginClick,
                tag = "btn_cyber_portal"
            )
            LauncherIconItem(
                icon = Icons.Default.PersonAdd,
                label = "DAFTAR",
                onClick = onRegisterClick,
                tag = "btn_cyber_daftar"
            )
            LauncherIconItem(
                icon = Icons.Default.Bolt,
                label = "DEMO",
                iconTint = DrgGoldReward,
                onClick = onQuickDemoClick,
                tag = "btn_cyber_demo"
            )
            LauncherIconItem(
                icon = Icons.Default.Info,
                label = "INTEL",
                onClick = onAboutClick,
                tag = "btn_cyber_intel"
            )
        }
    }
}
