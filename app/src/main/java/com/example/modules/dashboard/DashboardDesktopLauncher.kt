package com.example.modules.dashboard

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.core.viewmodel.MainNavTab
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgShadowGreenAmbientStrong
import com.example.ui.theme.DrgShadowGreenSpotStrong

/**
 * Desktop-style launcher grid for quick access to app tabs.
 *
 * Renders [apps] in rows of 4 with a header badge. An empty list
 * shows only the header (no dead-end, no crash).
 *
 * @param onNavigate invoked with the tapped item's destination tab.
 * @param modifier optional layout modifier.
 * @param apps launcher items to display; must not be assumed non-empty.
 */
@Composable
fun DashboardDesktopLauncher(
    onNavigate: (MainNavTab) -> Unit,
    modifier: Modifier = Modifier,
    apps: List<DesktopAppItem> = defaultLauncherApps()
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.White.copy(alpha = 0.92f),
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(22.dp), ambientColor = DrgShadowGreenAmbientStrong, spotColor = DrgShadowGreenSpotStrong)
            .border(1.2.dp, Brush.linearGradient(listOf(Color.White, Color.White.copy(alpha = 0.6f), DrgGreenPrimary.copy(alpha = 0.15f))), RoundedCornerShape(22.dp))
            .testTag("dashboard_desktop_launcher")
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            DashboardDesktopLauncherHeader(activeCount = apps.size)
            if (apps.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    apps.chunked(4).forEach { rowApps ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                            rowApps.forEach { app ->
                                LauncherTile(app = app, onClick = { onNavigate(app.tab) }, modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}
