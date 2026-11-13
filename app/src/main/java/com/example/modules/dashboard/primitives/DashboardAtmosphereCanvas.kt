package com.example.modules.dashboard.primitives

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.modules.dashboard.theme.DashboardVisualTheme
import com.example.shared.atoms.DrgLuminousAtmosphere

@Composable
fun DashboardAtmosphereCanvas(
    theme: DashboardVisualTheme,
    modifier: Modifier = Modifier
) {
    DrgLuminousAtmosphere(theme = theme, modifier = modifier)
}
