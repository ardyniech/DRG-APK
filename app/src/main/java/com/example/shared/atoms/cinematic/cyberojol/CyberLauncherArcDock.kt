package com.example.shared.atoms.cinematic.cyberojol

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.modules.auth.LauncherIconItem
import com.example.ui.theme.DrgGlassSurfaceStrong

@Composable
fun CyberLauncherArcDock(
    onRegisterClick: () -> Unit,
    onHelpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(DrgGlassSurfaceStrong)
            .border(1.5.dp, Color.White.copy(alpha = 0.95f), RoundedCornerShape(26.dp))
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .testTag("cyber_launcher_arc_dock"),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LauncherIconItem(
                icon = Icons.Default.PersonAdd,
                label = "DAFTAR KTA",
                onClick = onRegisterClick,
                tag = "btn_cyber_daftar"
            )
            LauncherIconItem(
                icon = Icons.Default.SupportAgent,
                label = "BANTUAN",
                onClick = onHelpClick,
                tag = "btn_cyber_help"
            )
        }
    }
}
